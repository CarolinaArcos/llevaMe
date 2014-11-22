package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Notificacion;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.model.Ubicacion;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosRuta;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class DetallesRutaPasajero extends Activity {
	
	private EditText conductor;
	private EditText nombre;
	private EditText fecha;
	private EditText hora;
	private EditText cupo;
	private EditText placa;
	private EditText descripcion;
	private static int lastId = -3; //id de la ultima ruta vista en detalles
	private int id = -1;//id ruta
	
	private int idUsuario; //id del usuario registrado
	private int idConductor = 1;
	
	private ImageButton mapa;
	private String [] markerSnippet;
	private double [] markerLat;
	private double [] markerLong;
	private String pointSnippet;
	private double pointLat;
	private double pointLong;
	private int pointPickUp = 0; //Punto quemado

	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles_ruta_pasajero);
		
		SharedPreferences settings = getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
		idUsuario = settings.getInt(SharedPreferencesHandler.LOGIN_KEY, -1);
		
		id = getIntent().getIntExtra("id",-2);
		if (id==-2) {
			if (lastId==-3) {
				//TODO: poner mensaje de error
			}else {
				id = lastId;
			}
				
		}
		lastId = id;
		
		conductor = (EditText) findViewById(R.id.conductorPasajero);
		nombre = (EditText) findViewById(R.id.nombrePasajero);
		fecha = (EditText) findViewById(R.id.fechaPasajero);
		hora = (EditText) findViewById(R.id.horaPasajero);
		cupo = (EditText) findViewById(R.id.cupoPasajero);
		placa = (EditText) findViewById(R.id.placaPasajero);
		descripcion = (EditText) findViewById(R.id.descripcionPasajero);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mapa = (ImageButton) findViewById(R.id.image);
		mapa.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) { 
				desplegarMapa();
			}
		});
		
		new TraerRuta().execute(""+id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detalles_ruta_pasajero, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onDejarRuta(View view) {
		new DejarRutaPasajero().execute(""+id,""+idUsuario);
		Toast toast = Toast.makeText(this, "Ha dejado esta ruta", 3);
		toast.show();
	}
	
	public void desplegarMapa() {
		Intent intent = new Intent(this,ViewMapDetailsPassenger.class);
    	intent.putExtra("markerSnippet", markerSnippet);
		intent.putExtra("markerLat", markerLat);
		intent.putExtra("markerLong", markerLong);
		intent.putExtra("pointPickUp", pointPickUp);
	    startActivity(intent);
	}
	
	public void volverAMenu() {
		finish();
	}
	
	private class DejarRutaPasajero extends AsyncTask<String, Void, Void> {

		public DejarRutaPasajero(){
			super();
		}

		@Override
		protected Void doInBackground(String...params) {
			//TODO: pasar usuario
			ServiciosRuta.getInstancia().dejarRuta(id, idUsuario);
			return null;
		}

		@Override
		protected void onPostExecute(Void v){
			volverAMenu();
			
			Notificacion n = new Notificacion(-1, idUsuario+" ha dejado la ruta "+nombre, idConductor);
			ServiciosEvento.getInstancia().ingresarNotificacion(n);
		}
	}
	
	public class TraerRuta extends AsyncTask<String, Void, Ruta> {
		
		String nombreConductor;
		
		public TraerRuta(){
			super();
		}
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(DetallesRutaPasajero.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		@Override
		protected Ruta doInBackground(String...params) {
			Ruta r = ServiciosRuta.getInstancia().getRuta(""+id);
			idConductor = r.getConductor();
			
			nombreConductor = ServiciosUsuario.getInstancia().getUsuario(idConductor).getUsername();
			
			return r;
		}

		@Override
		protected void onPostExecute(Ruta r){
			pDialog.dismiss();
			
			//Tomar valores de r
			String name = r.getNombre();
			String date = r.getFecha().substring(0, 10);
			String hour = r.getFecha().substring(10);
			String capacity = Integer.toString(r.getCapacidad());
			String pla = r.getPlaca();
			String desctiption = r.getDescripcion();

			nombre.setText(name);
			fecha.setText(date);
			hora.setText(hour);
			cupo.setText(capacity);
			placa.setText(pla);
			descripcion.setText(desctiption);
			conductor.setText(nombreConductor);
			
			markerSnippet = new String[r.getRecorrido().length];
			markerLong = new double[markerSnippet.length];
			markerLat = new double[markerSnippet.length];
			
			
			for(int i = 0; i<r.getRecorrido().length; i++){
				Ubicacion u = r.getRecorrido()[i];
				
				Log.d("nombre "+i,u.getNombre());
				Log.d("longitud "+i,u.getLongitud()+"");
				Log.d("latitud "+i,u.getLatitud()+"");
				
				markerSnippet[i] = u.getNombre();
				markerLong[i] = u.getLongitud();
				markerLat[i] = u.getLatitud();
			}
			
			 int idUbicacion = -1;
			for(int i = 0; i<r.getPasajeros().length; i++) {
				if (r.getPasajeros()[i].getId() == idUsuario) {
					idUbicacion = r.getPasajeros()[i].getPickUp();
					break;
				}
			}
			
			for(int j=0; j<r.getRecorrido().length; j++){
				if(r.getRecorrido()[j].getId() == idUbicacion){
					pointPickUp = j;
					break;
				}
			}
			
			Ubicacion selected = r.getRecorrido()[pointPickUp];
			pointSnippet = selected.getNombre();
			pointLat = selected.getLatitud();
			pointLong = selected.getLongitud();
		}
	}
}
