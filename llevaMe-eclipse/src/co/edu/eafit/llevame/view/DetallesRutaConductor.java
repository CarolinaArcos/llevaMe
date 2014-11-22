package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Notificacion;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.model.Ubicacion;
import co.edu.eafit.llevame.model.Usuario;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosRuta;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class DetallesRutaConductor extends Activity {
	
	private EditText pasajeros;
	private EditText nombre;
	private EditText fecha;
	private EditText hora;
	private EditText cupo;
	private EditText placa;
	private EditText descripcion;
	private static int lastId = -3; //id de la ultima ruta vista en detalles
	private int id = -1;
	private int idConductor = 1;
	
	private Usuario[] idPasajeros;//Pasajeros de la ruta
	
	private ImageButton mapa;
	private String [] markerSnippet;
	private double [] markerLat;
	private double [] markerLong;
	
	private int [] pointsPickUp = {0,1};
	private String [] pointsSnippet;
	private double [] pointsLat;
	private double [] pointsLong;
	
	private ProgressDialog pDialog;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles_ruta_conductor);
		
		id = getIntent().getIntExtra("id",-2);
		if (id==-2) {
			if (lastId==-3) {
				//TODO: poner mensaje de error
			}else {
				id = lastId;
			}		
		}
		lastId = id;
		
		pasajeros = (EditText) findViewById(R.id.pasajerosConductor);
		nombre = (EditText) findViewById(R.id.nombreConductor);
		fecha = (EditText) findViewById(R.id.fechaConductor);
		hora = (EditText) findViewById(R.id.horaConductor);
		cupo = (EditText) findViewById(R.id.cupoConductor);
		placa = (EditText) findViewById(R.id.placaConductor);
		descripcion = (EditText) findViewById(R.id.descripcionConductor);
		
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
		getMenuInflater().inflate(R.menu.detalles_ruta_conductor, menu);
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
	
	public void desplegarMapa() {
		Intent intent = new Intent(this,ViewMapDetailsDriver.class);
    	intent.putExtra("markerSnippet", markerSnippet);
		intent.putExtra("markerLat", markerLat);
		intent.putExtra("markerLong", markerLong);
		intent.putExtra("pointsPickUp", pointsPickUp);
		startActivity(intent);
	}
	
	public void onIniciar(View view) {
		new IniciarRuta().execute(""+id);
	}
	
	public void onFinalizar(View view) {
		new FinalizarRuta().execute(""+id);
	}
	
	public void volverAMenu() {
		finish();
	}
	
	public class TraerRuta extends AsyncTask<String, Void, Ruta> {
		
		String nombreConductor;
		
		public TraerRuta(){
			super();
		}
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(DetallesRutaConductor.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		@Override
		protected Ruta doInBackground(String...params) {
			Ruta r = ServiciosRuta.getInstancia().getRuta(""+id);
			
			idPasajeros = ServiciosUsuario.getInstancia().getPasajeros(id);
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
			if(idPasajeros!=null && idPasajeros.length!=0){
				String nombrePasajeros = "";
				for (int i=0; i<idPasajeros.length; i++) {
					
					nombrePasajeros += idPasajeros[i].getUsername();
					if(i != idPasajeros.length-1){
						nombrePasajeros += ", ";
					}
				}
				
				pasajeros.setText(nombrePasajeros);
			}
			
			
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
			
			pointsSnippet = new String[idPasajeros.length];
			pointsLong = new double[pointsSnippet.length];
			pointsLat = new double[pointsSnippet.length];
			pointsPickUp = new int[pointsSnippet.length];
			for(int i=0; i<idPasajeros.length; i++){
				int posSelected = 0; //QUEMADO TODO: obtener ubicacion de recogida del usuario
				Ubicacion selected = r.getRecorrido()[posSelected];
				
				pointsPickUp[i] = posSelected;
				
				pointsSnippet[i] = selected.getNombre();
				pointsLong[i] = selected.getLongitud();
				pointsLat[i] = selected.getLatitud();
			}
		}	
	}
	
	public class IniciarRuta extends AsyncTask<String, Void, Void> {

		public IniciarRuta(){
			super();
		}

		@Override
		protected Void doInBackground(String...params) {
			ServiciosRuta.getInstancia().iniciarRuta(id);
			return null;
			
		}

		@Override
		protected void onPostExecute(Void v){
			volverAMenu();
			
			for(Usuario u : idPasajeros){//enviar notificacion a cada pasajero
				Notificacion n = new Notificacion(-1, "La ruta "+nombre+" ha iniciado", u.getId());
				ServiciosEvento.getInstancia().ingresarNotificacion(n);
			}
		}
	}
	
	public class FinalizarRuta extends AsyncTask<String, Void, Void> {

		
		public FinalizarRuta(){
			super();
		}

		@Override
		protected Void doInBackground(String...params) {
			ServiciosRuta.getInstancia().finalizarRuta(id);
			return null;
			
		}

		@Override
		protected void onPostExecute(Void v){
			volverAMenu();
			
			for(Usuario u : idPasajeros){//enviar notificacion a cada pasajero
				Notificacion n = new Notificacion(-1, "La ruta "+nombre+" ha finalizado", u.getId());
				ServiciosEvento.getInstancia().ingresarNotificacion(n);
			}
		}
	}
}