package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Notificacion;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosRuta;

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
	private int idUsuario = 1;
	
	private int[] idPasajeros = {1, 2};//TODO: obtener los pasajeros de la ruta
	
	private ImageButton mapa;

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
		
		mapa = (ImageButton) findViewById(R.id.image);
//		mapa.setOnClickListener(new OnClickListener() {
//			 
//			@Override
//			public void onClick(View arg0) {
//				desplegarMapa();
//			}
//		});
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		new TraerRuta(this).execute(""+id);
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
    	Intent intent = new Intent(this,ViewMap.class);
    	startActivity(intent);
	}
	
	public void onIniciar(View view) {
		new IniciarRuta(this).execute(""+id);
	}
	
	public void onFinalizar(View view) {
		new FinalizarRuta(this).execute(""+id);
	}
	
	public void volverAMenu() {
		finish();
	}
	
	public class TraerRuta extends AsyncTask<String, Void, Ruta> {

		private Activity activity;
		
		public TraerRuta(Activity activity){
			super();
			this.activity = activity;
		}

		@Override
		protected Ruta doInBackground(String...params) {
			return ServiciosRuta.obtenerInstancia().getRuta(""+id);
			
		}

		@Override
		protected void onPostExecute(Ruta r){

			Log.d("id in post", ""+id);
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
			//pasajeros.setText();
			
		}
		
	}
	
	public class IniciarRuta extends AsyncTask<String, Void, Void> {

		private Activity activity;
		
		public IniciarRuta(Activity activity){
			super();
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(String...params) {
			ServiciosRuta.obtenerInstancia().iniciarRuta(id+"?estado=true");
			return null;
			
		}

		@Override
		protected void onPostExecute(Void v){
			volverAMenu();
			
			for(int idP : idPasajeros){//enviar notificacion a cada pasajero
				Notificacion n = new Notificacion(-1, "La ruta "+nombre+" ha iniciado", idP);
				ServiciosEvento.obtenerInstancia().ingresarNotificacion(n);
			}
		}
	}
	
	public class FinalizarRuta extends AsyncTask<String, Void, Void> {

		private Activity activity;
		
		public FinalizarRuta(Activity activity){
			super();
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(String...params) {
			ServiciosRuta.obtenerInstancia().finalizarRuta(id+"");
			return null;
			
		}

		@Override
		protected void onPostExecute(Void v){
			volverAMenu();
			
			for(int idP : idPasajeros){//enviar notificacion a cada pasajero
				Notificacion n = new Notificacion(-1, "La ruta "+nombre+" ha finalizado", idP);
				ServiciosEvento.obtenerInstancia().ingresarNotificacion(n);
			}
		}
	
	}


}
