package co.edu.eafit.llevame.view;

import co.edu.eafit.llevame.handlers.DirectionsJSONParser;
import co.edu.eafit.llevame.handlers.HttpConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import co.edu.eafit.llevame.R;

public class ViewMap extends FragmentActivity implements OnMapClickListener, OnMarkerClickListener, OnMarkerDragListener, OnInfoWindowClickListener{
	private final LatLng EAFIT = new LatLng(6.200696,-75.578433); 
	private GoogleMap mapa;
	ArrayList<Polyline> lines;
	ArrayList<String> nombresDestinos;
	ArrayList<Marker> markers;
	double latti;
	double longi;
	LatLng current;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_map);
		// Initializing
		nombresDestinos = new ArrayList<String>();
		lines = new ArrayList<Polyline>();
		markers = new ArrayList<Marker>();
		latti = 0.0;
		longi = 0.0;
		current = new LatLng(0, 0);
	
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListner = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}

			@Override
			public void onProviderEnabled(String provider) {}

			@Override
			public void onProviderDisabled(String provider) {}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if(latti == 0.0 && longi == 0.0) {
					latti = location.getLatitude();
					longi = location.getLongitude();
					LatLng current = new LatLng(latti, longi);
					mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
				}
			}
		};
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(EAFIT, 1));
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);
		mapa.setOnMapClickListener(this);
		mapa.setOnMarkerClickListener(this);
		mapa.setOnMarkerDragListener(this);
		mapa.setOnInfoWindowClickListener(this);
	}

	public void moveCamera(View view) {
		current = new LatLng(latti, longi);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
	}

	public void animateCamera(View view) {
		if (mapa.getMyLocation() != null)
			mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng( mapa.getMyLocation().getLatitude(), 
							mapa.getMyLocation().getLongitude()), 17));
	}

	public void drawRoute(View view) {
		// Checks, whether start and end locations are captured
		cleanMap();
		if(markers.size() >= 2){					
			LatLng origin = markers.get(0).getPosition();
			LatLng dest = markers.get(markers.size()-1).getPosition();

			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(origin, dest);				

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}
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
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

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
	public void onMapClick(LatLng puntoPulsado) {
		// Already 10 locations with 8 waypoints and 1 start location and 1 end location. 
		// Upto 8 waypoints are allowed in a query for non-business users
		if(markers.size()>=10) return;
		showName(puntoPulsado);
	}

	@Override
	public boolean onMarkerClick(final Marker delMarker){
		delMarker.showInfoWindow();
		return true;
	}
	@Override
	public void onMarkerDragStart(final Marker marker) {
		marker.remove();
		int index = markers.indexOf(marker);
		markers.remove(marker);
		nombresDestinos.remove(index);
	}

	public void showName(LatLng point) {
		final LatLng puntoPulsado = point;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		View regisText = inflater.inflate(R.layout.nombre_destino, null);
		builder.setView(regisText)
		// Add action buttons
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Dialog f = (Dialog) dialog;
				EditText name = (EditText) f.findViewById(R.id.namePoint);
				nombresDestinos.add(name.getText().toString());

				// Creating MarkerOptions
				MarkerOptions options = new MarkerOptions();

				// Setting the position of the marker
				options.position(puntoPulsado);

				options.
				icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
						.title("Punto de Encuentro")
						.snippet(nombresDestinos.get(nombresDestinos.size()-1));			

				// Add new marker to the Google Map Android API V2
				Marker marker = mapa.addMarker(options);
				markers.add(marker);
				marker.setDraggable(true);
			}
		})
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.create();
		builder.show();
	}
	@Override
	public void onInfoWindowClick(Marker marker){
		marker.hideInfoWindow();
	}

	@Override
	public void onMarkerDrag(Marker arg0) {}

	@Override
	public void onMarkerDragEnd(Marker arg0) {}

	public void cleanMap() {
		for(int i=0; i<lines.size(); ++i) lines.get(i).remove();
		lines.clear();
	}
}