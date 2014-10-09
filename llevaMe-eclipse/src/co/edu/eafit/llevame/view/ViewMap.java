package co.edu.eafit.llevame.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import co.edu.eafit.llevame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

public class ViewMap extends FragmentActivity implements OnMapClickListener, OnMarkerClickListener {
		   private final LatLng EAFIT = new LatLng(6.200696,-75.578433); 
		   private GoogleMap mapa;
		   double latti = 0.0;
		   double longi = 0.0;
		   LatLng current = new LatLng(0, 0);
		   private ArrayList<Marker> fixedMarkersList = new ArrayList<Marker>();
		  
		   @Override protected void onCreate(Bundle savedInstanceState) {
		      super.onCreate(savedInstanceState);
		      setContentView(R.layout.activity_view_map);
		   // Acquire a reference to the system Location Manager
		      LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		      
		      // Define a listener that responds to location updates
		      LocationListener locationListner = new LocationListener() {
		       
		       @Override
		       public void onStatusChanged(String provider, int status, Bundle extras) {
		        // TODO Auto-generated method stub
		        
		       }
		       
		       @Override
		       public void onProviderEnabled(String provider) {
		        // TODO Auto-generated method stub
		        
		       }
		       
		       @Override
		       public void onProviderDisabled(String provider) {
		        // TODO Auto-generated method stub
		        
		       }
		       
		       @Override
		       public void onLocationChanged(Location location) {
		    	   mapa.clear();
		        // TODO Auto-generated method stub
		    	   if(latti == 0.0 && longi == 0.0) {
		    		   latti = location.getLatitude();
		    		   longi = location.getLongitude();
		    		   LatLng current = new LatLng(latti, longi);
		    		   mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 65));
		    	   }
		        System.out.print("latti :"+latti+"\n"+"longi :"+longi);
		        
		       }
		      };
		      // Register the listener with the Location Manager to receive location updates
		      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);
		      //mapa = ((SupportMapFragment) getSupportFragmentManager()
		      //      .findFragmentById(R.id.map)).getMap();
		      mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		      mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		      mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(EAFIT, 15));
		      mapa.setMyLocationEnabled(true);
		      
		      mapa.getUiSettings().setZoomControlsEnabled(false);
		      mapa.getUiSettings().setCompassEnabled(true);
		      mapa.addMarker(new MarkerOptions()
		            .position(EAFIT)
		            .title("EAFIT")
		            .snippet("Universidad EAFIT")
		            .anchor(0.5f, 0.5f));
		      mapa.setOnMapClickListener(this);
		      mapa.setOnMarkerClickListener(this);
		   }
		 
		   public void moveCamera(View view) {
			   current = new LatLng(latti, longi);
			   mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 65));
		   }
		 
		   public void animateCamera(View view) {
		      if (mapa.getMyLocation() != null)
		         mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
		            new LatLng( mapa.getMyLocation().getLatitude(), 
		            		mapa.getMyLocation().getLongitude()), 15));
		   }
		 
		   public void addMarker(View view) {
		      mapa.addMarker(new MarkerOptions().position(
		           new LatLng(mapa.getCameraPosition().target.latitude,
		      mapa.getCameraPosition().target.longitude)));

		   }
		 
		   @Override
		   public void onMapClick(LatLng puntoPulsado) {
			   Marker point = mapa.addMarker(new MarkerOptions().position(puntoPulsado).
		         icon(BitmapDescriptorFactory
		            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
		      listMarkers(point);
		   }
		   @Override
		    public boolean onMarkerClick(final Marker delMarker){
		    		delMarker.remove(); //removes the marker by pressing it
		    		return true;
		    }
		   public void listMarkers(Marker fixedMarker){
		    	fixedMarkersList.add(fixedMarker);
		   }
		}
	/*// Google Map
	private GoogleMap googleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_map);

		try {
			// Loading map
			initilizeMap();

			// Changing map type
			//googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			 googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			
			//MarkerOptions marker = new MarkerOptions().position(new LatLng(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude()));
			LatLng UniEafit = new LatLng(6.200696,-75.578433);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UniEafit, 1000));
			Marker marker = googleMap.addMarker(new MarkerOptions()
	        .position(UniEafit));  

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * 
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}*/
