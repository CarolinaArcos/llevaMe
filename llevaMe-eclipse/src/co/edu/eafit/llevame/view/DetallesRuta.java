package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Invitacion;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosRuta;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class DetallesRuta extends Activity{

	private EditText conductor;
	private EditText nombre;
	private EditText fecha;
	private EditText hora;
	private EditText cupo;
	private EditText placa;
	private EditText descripcion;
	private static int lastId = -3; //id de la ultima ruta vista en detalles
	private int id = -1; //id de la ruta
	private int idUsuario; //Usuario logeado
	private int idConductor;//id del conductor de la ruta
	private int puntoRecogida = 1;//QUEMADO id de la ubicacion en la que se recoger el usr
	
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
		setContentView(R.layout.activity_detalles_ruta);
		
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
		
		conductor = (EditText) findViewById(R.id.conductorDetalles);
		nombre = (EditText) findViewById(R.id.nombreDetalles);
		fecha = (EditText) findViewById(R.id.fechaDetalles);
		hora = (EditText) findViewById(R.id.horaDetalles);
		cupo = (EditText) findViewById(R.id.cupoDetalles);
		placa = (EditText) findViewById(R.id.placaDetalles);
		descripcion = (EditText) findViewById(R.id.descripcionDetalles);
		
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
		getMenuInflater().inflate(R.menu.detalles_ruta, menu);
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

	public void onllevaMe(View view) {
		Invitacion invitacion = new Invitacion(-1, "",
				idConductor, false, Invitacion.RUTA, idUsuario, id, puntoRecogida);
		
		new SolicitarCupo().execute(invitacion);	
	}
	
	public void desplegarMapa() {
		Intent intent = new Intent(this,ViewMapDetails.class);
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
	
	public class TraerRuta extends AsyncTask<String, Void, Ruta> {

		String nombreConductor;
		
		public TraerRuta(){
			super();
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
			
		}
	}
	
	public class SolicitarCupo extends AsyncTask<Invitacion, Void, Void> {
		
		public SolicitarCupo(){
			super();
		}

		@Override
		protected Void doInBackground(Invitacion...params) {
			Invitacion inv = params[0];
			String nombreUsuario = ServiciosUsuario.getInstancia().getUsuario(inv.getIdRef()).getUsername();
			String nombreRuta = nombre.getText().toString(); 
			inv.setMensaje(nombreUsuario +" ha solicitado un cupo en tu ruta "+nombreRuta);
			
			ServiciosEvento.getInstancia().ingresarInvitacion(inv);
			return null;
		}

		@Override
		protected void onPostExecute(Void v){
			
			
			Toast toast = Toast.makeText(DetallesRuta.this, "Has solicitado un cupo para esta ruta", 3);
			toast.show();
			
			volverAMenu();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
	    	pointSnippet = data.getStringExtra("pointSnippet");
	    	pointLat = data.getDoubleExtra("pointLat",0);
	    	pointLong = data.getDoubleExtra("pointLong",0);
	    }
	}
}
