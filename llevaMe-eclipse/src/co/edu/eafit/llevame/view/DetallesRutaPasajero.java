package co.edu.eafit.llevame.view;

import android.app.Activity;
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
import android.widget.Toast;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Notificacion;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosRuta;
import co.edu.eafit.llevame.view.DetallesRuta.TraerRuta;

public class DetallesRutaPasajero extends Activity {
	
	private EditText conductor;
	private EditText nombre;
	private EditText fecha;
	private EditText hora;
	private EditText cupo;
	private EditText placa;
	private EditText descripcion;
	private static int lastId = -3; //id de la ultima ruta vista en detalles
	private int id = -1;
	
	//TODO: obtener (desquemar) los valores
	private int idUsuario = 1; //id del usuario registrado
	private int idConductor = 1;
	
	private ImageButton mapa;
	private String [] markerSnippet = {"Eafit", "Estacion Poblado", "CC SantaFe"};
	private double [] markerLat = {6.200696,6.21211476,6.19790767};
	private double [] markerLong = {-75.578433,-75.57809091,-75.57431436};
	private String pointSnippet;
	private double pointLat;
	private double pointLong;
	protected static final int REQUEST_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles_ruta_pasajero);
		
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
		intent.putExtra("pointSnippet", pointSnippet);
	    intent.putExtra("pointLat", pointLat);
	    intent.putExtra("pointLong", pointLong);
        startActivityForResult(intent, REQUEST_CODE);
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
			ServiciosRuta.getInstancia().dejarRuta("pasajeros?ruta="+id+"&usuario="+idUsuario);
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
		
		public TraerRuta(){
			super();
		}

		@Override
		protected Ruta doInBackground(String...params) {
			return ServiciosRuta.getInstancia().getRuta(""+id);
		}

		@Override
		protected void onPostExecute(Ruta r){

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
			//TODO: conductor.setText();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
	    	pointSnippet = data.getStringExtra("pointSnippet");
	    	pointLat = data.getDoubleExtra("pointLat",0);
	    	pointLong = data.getDoubleExtra("pointLong",0);
	    	Toast.makeText(this, "Snippet: "+pointSnippet+" Lat: "+ pointLat+" Long: "+pointLong, Toast.LENGTH_LONG).show();
	    }
	}
}
