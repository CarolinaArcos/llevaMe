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
import co.edu.eafit.llevame.model.Ruta;
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
	private int idUsuario = 1;
	
	private ImageButton mapa;

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
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mapa = (ImageButton) findViewById(R.id.image);
//		mapa.setOnClickListener(new OnClickListener() {
//			 
//			@Override
//			public void onClick(View arg0) {
//				desplegarMapa();
//			}
//		});
		
		new TraerRuta(this).execute(""+id);
				
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
	
	public void setTheId(int id) {
		this.id = id;
	}

	public void onDejarRuta(View view) {
		new DejarRutaPasajero(this).execute(""+id,""+idUsuario);
		Toast toast = Toast.makeText(this, "Ha dejado esta ruta", 3);
		toast.show();
	}
	
	public void desplegarMapa() {
    	Intent intent = new Intent(this,ViewMap.class);
    	startActivity(intent);
	}
	
	public void volverAMenu() {
		finish();
	}
	
	private class DejarRutaPasajero extends AsyncTask<String, Void, Void> {

		private Activity activity;
		
		public DejarRutaPasajero(Activity activity){
			super();
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(String...params) {
			//TODO: pasar usuario
			ServiciosRuta.obtenerInstancia().dejarRuta("pasajeros?ruta="+id+"&usuario="+idUsuario);
			return null;
		}

		@Override
		protected void onPostExecute(Void v){
			volverAMenu();
		}
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
			//conductor.setText();
			
			
		}

	}
	
}
