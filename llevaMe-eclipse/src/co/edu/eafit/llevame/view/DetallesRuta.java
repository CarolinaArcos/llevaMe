package co.edu.eafit.llevame.view;

import android.R.id;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Ruta;



public class DetallesRuta extends Activity{

	Ruta r = new Ruta("hola", "septiembre 10", "10:10", 3, "AAA111", "hola mundo");;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalles_ruta);
		//Guardar referencia
		EditText conductor = (EditText) findViewById(R.id.conductorDetalles);
		EditText nombre = (EditText) findViewById(R.id.nombreDetalles);
		EditText fecha = (EditText) findViewById(R.id.fechaDetalles);
		EditText hora = (EditText) findViewById(R.id.horaDetalles);
		EditText cupo = (EditText) findViewById(R.id.cupoDetalles);
		EditText placa = (EditText) findViewById(R.id.placaDetalles);
		EditText descripcion = (EditText) findViewById(R.id.descripcionDetalles);
		
		//Tomar valores de r
		String name = r.getNombre();
		String date = r.getFecha();
		String hour = r.getHora();
		String capacity = r.getCapacidad() + "";
		String pla = r.getPlaca();
		String desctiption = r.getDescripcion();
		
		//mostrar informacion
		nombre.setText(name);
		fecha.setText(date);
		hora.setText(hour);
		cupo.setText(capacity);
		placa.setText(pla);
		descripcion.setText(desctiption);
		conductor.setText("NN");
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalles_ruta, menu);
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
}
