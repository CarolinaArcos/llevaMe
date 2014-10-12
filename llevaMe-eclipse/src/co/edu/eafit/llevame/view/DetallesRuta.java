package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosRuta;



public class DetallesRuta extends Activity{

	EditText conductor;
	EditText nombre;
	EditText fecha;
	EditText hora;
	EditText cupo;
	EditText placa;
	EditText descripcion;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalles_ruta);
		
		conductor = (EditText) findViewById(R.id.conductorDetalles);
		nombre = (EditText) findViewById(R.id.nombreDetalles);
		fecha = (EditText) findViewById(R.id.fechaDetalles);
		hora = (EditText) findViewById(R.id.horaDetalles);
		cupo = (EditText) findViewById(R.id.cupoDetalles);
		placa = (EditText) findViewById(R.id.placaDetalles);
		descripcion = (EditText) findViewById(R.id.descripcionDetalles);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalles_ruta, menu);
		new TraerRuta().execute("1");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	private class TraerRuta extends AsyncTask<String, Void, Ruta> {

		public TraerRuta(){
			super();
		}

		@Override
		protected Ruta doInBackground(String...params) {
			// TODO Auto-generated method stub
			return ServiciosRuta.obtenerInstancia().getRuta(params[0]);
		}

		@Override
		protected void onPostExecute(Ruta r){

			//Tomar valores de r
			String name = r.getNombre();
			String date = r.getFecha();
			String capacity = Integer.toString(r.getCapacidad());
			//String pla = r.getPlaca();
			String desctiption = r.getDescripcion();

			nombre.setText(name);
			fecha.setText(date);
			//hora.setText(hour);
			cupo.setText(capacity);
			//placa.setText(pla);
			descripcion.setText(desctiption);
			//conductor.setText();
		}


	}
}
