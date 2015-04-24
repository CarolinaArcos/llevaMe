package co.edu.eafit.llevame.view;

import co.edu.eafit.llevame.handlers.DirectionsJSONParser;
import co.edu.eafit.llevame.handlers.HttpConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import co.edu.eafit.llevame.R;

public class ViewMapDetails extends FragmentActivity implements OnMarkerClickListener, OnInfoWindowClickListener{
	
	private GoogleMap mapa;
	private ArrayList<Polyline> lines;
	private ArrayList<String> nombresDestinos;
	private ArrayList<Marker> markers;
	private double latti;
	private double longi;
	private LatLng current;
	private static final int OK_RESULT_CODE = 1;
	private String [] markerSnippet;
	private double [] markerLat;
	private double [] markerLong;
	private ProgressDialog pDialog;
	private int pointPickUp;
	private int indexPoint;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_view_map_details);
		// Initializing
		nombresDestinos = new ArrayList<String>();
		lines = new ArrayList<Polyline>();
		markers = new ArrayList<Marker>();
		markerSnippet = getIntent().getStringArrayExtra("markerSnippet");
		markerLat = getIntent().getDoubleArrayExtra("markerLat");
		markerLong = getIntent().getDoubleArrayExtra("markerLong");
		pointPickUp = getIntent().getIntExtra("pointPickUp", -1);
		latti = markerLat[0];
		longi = markerLong[0];
		current = new LatLng(latti, longi);
		indexPoint = -1;
	
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);
		mapa.setOnMarkerClickListener(this);
		mapa.setOnInfoWindowClickListener(this);
		
		if(markerSnippet.length >=2) {
			addMarker();
			cleanMap();
			if(pointPickUp >= 0) {
				pickUp(markers.get(pointPickUp));
			}
			drawRoute();
		}
	}

	public void aceptar(View view) {
		if(indexPoint > -1) {
			pointPickUp = indexPoint;
			returnParams();
		}
		else
			Toast.makeText(this, "Necesita señalar un punto de encuentro", Toast.LENGTH_LONG).show();
	}
	
	public void back(View view) {
		if(indexPoint > -1)	pointPickUp = indexPoint;
		returnParams();
	}
	
	protected void returnParams() {
		Intent intent = new Intent();
	    intent.putExtra("pointPickUp", pointPickUp);
	    setResult(OK_RESULT_CODE, intent);
		finish();
	   }

	private String getDirectionsUrl(LatLng origin,LatLng dest){

		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;

		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		

		// Sensor enabled
		String sensor = "sensor=false";			

		// Waypoints
		String waypoints = "";
		for(int i=1;i<markers.size()-1;i++){
			LatLng point = (LatLng) markers.get(i).getPosition();
			if(i==1) waypoints = "waypoints=";
			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

		return url;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{	
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(ViewMapDetails.this);
            pDialog.setMessage("Trazando Ruta...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try{
				// Fetching the data from web service
				HttpConnection http = new HttpConnection();
				data = http.downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(final String result) {
			pDialog.dismiss();
			super.onPostExecute(result);
    		runOnUiThread(new Runnable() {
                public void run() {		
        			ParserTask parserTask = new ParserTask();
        			// Invokes the thread for parsing the JSON data
        			parserTask.execute(result);
                }
            });
		}		
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

		// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           

			try{
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);    
			}catch(Exception e){
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);					

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	

					points.add(position);						
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(3);
				lineOptions.color(Color.RED);				
			}

			// Drawing polyline in the Google Map for the i-th route
			Polyline line = mapa.addPolyline(lineOptions);
			lines.add(line);
		}			
	}   

	@Override
	public boolean onMarkerClick(final Marker delMarker){
		delMarker.showInfoWindow();
		return true;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker){
		marker.hideInfoWindow();
		if(!markers.get(markers.size()-1).equals(marker)) pickUp(marker);
		else
			Toast.makeText(this, "El punto de recogida no puede ser el destino", Toast.LENGTH_LONG).show();
	}
	
	public void pickUp(final Marker marker) {
		for(int i = 0; i<markers.size(); ++i) {
			if(markers.get(i).equals(marker)) {
				markers.get(i).setIcon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
				indexPoint = i;
			}
			else markers.get(i).setIcon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		}
	}
	
	public void cleanMap() {
		for(int i=0; i<lines.size(); ++i) lines.get(i).remove();
		lines.clear();
	}
	
	@Override
	public void onBackPressed() {
		returnParams();
	}
	
	public void addMarker() {
		markers.clear();
		nombresDestinos.clear();
		for(int i=0; i<markerSnippet.length; ++i) {
			MarkerOptions options = new MarkerOptions();
			LatLng position = new LatLng(markerLat[i], markerLong[i]);
			nombresDestinos.add(markerSnippet[i]);
			// Setting the position of the marker
			options.position(position);
			options.
			icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
					.title("Punto de Encuentro")
					.snippet(nombresDestinos.get(nombresDestinos.size()-1));			
			
			// Add new marker to the Google Map Android API V2
			Marker marker = mapa.addMarker(options);
			markers.add(marker);
		}
	}
	public void drawRoute() {
		LatLng origin = markers.get(0).getPosition();
		LatLng dest = markers.get(markers.size()-1).getPosition();

		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);				

		DownloadTask downloadTask = new DownloadTask();

		// Start downloading json data from Google Directions API
		downloadTask.execute(url);
	}
}