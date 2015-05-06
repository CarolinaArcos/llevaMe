package co.edu.eafit.llevame.view;

import co.edu.eafit.llevame.handlers.DirectionsJSONParser;
import co.edu.eafit.llevame.handlers.HttpConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;
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
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_view_map);
		// Initializing
		nombresDestinos = new ArrayList<String>();
		lines = new ArrayList<Polyline>();
		markers = new ArrayList<Marker>();
		latti = 0.0;
		longi = 0.0;
		current = new LatLng(0, 0);
		markerSnippet = getIntent().getStringArrayExtra("markerSnippet");
		markerLat = getIntent().getDoubleArrayExtra("markerLat");
		markerLong = getIntent().getDoubleArrayExtra("markerLong");
		
		LocationManager lm = null;
	    boolean gps_enabled = false,network_enabled = false;
	    final Activity context = this;
		if(lm==null)
	           lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	       try{
	       gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	       }catch(Exception ex){}
	       try{
	       network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	       }catch(Exception ex){}

	      if(!gps_enabled && !network_enabled){
	           AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	           dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
	           dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {

	               @Override
	               public void onClick(DialogInterface paramDialogInterface, int paramInt) {
	                   // TODO Auto-generated method stub
	                   Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                   context.startActivity(myIntent);
	                   //get gps
	               }
	           });
	           dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

	               @Override
	               public void onClick(DialogInterface paramDialogInterface, int paramInt) {
	                   // TODO Auto-generated method stub
	            	   returnParams();
	               }
	           });
	           dialog.show();

	       }
	
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
		
		if(markerSnippet.length >=2) {
			addMarker();
			cleanMap();
			drawRoute();
		}
	}

	public void moveCamera(View view) {
		current = new LatLng(latti, longi);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
	}

	public void guardar(View view) {
		if(markers.size() >= 2) {
			if(lines.size() >= 1)  returnParams();
			else 
				Toast.makeText(this, "Debe trazar una ruta primero", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(this, "Necesita al menos dos ubicaciones", Toast.LENGTH_LONG).show();
	}
	
	protected void returnParams() {
	      Intent intent = new Intent();
	      intent.putExtra("markersSnippet", snippets());
	      intent.putExtra("markersLat", lats());
	      intent.putExtra("markersLong", longs());
	      setResult(OK_RESULT_CODE, intent);
	      finish();
	   }

	public void drawRoute(View view) {
		// Checks, whether start and end locations are captured
		cleanMap();
		if(markers.size() >= 2){					
			drawRoute();
		} else
			Toast.makeText(this, "Necesita al menos dos ubicaciones", Toast.LENGTH_LONG).show();
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
            
            pDialog = new ProgressDialog(ViewMap.this);
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
		cleanMap();
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
				String ubicationName = name.getText().toString();
				if (ubicationName.equals("")) 
					launchToast();
				else {
					nombresDestinos.add(ubicationName);

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
					cleanMap();
				}
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
	
	public void launchToast(){
		Toast.makeText(this, "Debe ingresar una descripción", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onInfoWindowClick(Marker marker){
		marker.hideInfoWindow();
	}

	@Override
	public void onMarkerDrag(Marker arg0) {}

	@Override
	public void onMarkerDragEnd(Marker arg0) {}
	
	public double [] lats() {
		double [] lats = new double [markers.size()];
		for(int i=0; i<markers.size(); ++i) lats[i] = markers.get(i).getPosition().latitude;
		return lats;
	}

	public double [] longs() {
		double [] longs = new double [markers.size()];
		for(int i=0; i<markers.size(); ++i) longs[i] = markers.get(i).getPosition().longitude;
		return longs;
	}
	
	public String [] snippets() {
		String [] snippet = new String [markers.size()];
		for(int i=0; i<markers.size(); ++i) snippet[i] = markers.get(i).getSnippet();
		return snippet;
	}
	
	public void cleanMap() {
		for(int i=0; i<lines.size(); ++i) lines.get(i).remove();
		lines.clear();
	}
	@Override
	public void onBackPressed() {
	    // do nothing.
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
			marker.setDraggable(true);
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