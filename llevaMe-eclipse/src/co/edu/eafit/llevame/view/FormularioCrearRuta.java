package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Ruta;

public class FormularioCrearRuta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario_crear_ruta);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.formulario_crear_ruta, menu);
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
	
	public void onAceptar(View view){
		enviarFormulario();
		lanzarListaRutas();
	}
	
	public void enviarFormulario() {
		EditText hora = (EditText) findViewById(R.id.horaFormulario);
		EditText fecha = (EditText) findViewById(R.id.fechaFormulario);
		EditText cupo = (EditText) findViewById(R.id.cupoFormulario);
		EditText placa = (EditText) findViewById(R.id.placaFormulario);
		EditText descripcion = (EditText) findViewById(R.id.descripcionFormulario);
		EditText nombre = (EditText) findViewById(R.id.nombreRutaFormulario);
		
		String dataHora = hora.getText().toString();
		String dataFecha = fecha.getText().toString();
		String dataCupo = cupo.getText().toString();
		int numeroCupo = -1;
		if(!dataCupo.equals("")) numeroCupo = Integer.parseInt(dataCupo);
		String dataPlaca = placa.getText().toString();
		String dataDescripcion = descripcion.getText().toString();
		String dataName = nombre.getText().toString();
		
		//TODO: obtener el nombre de usuario que inicio sesion
		
		//TODO: obtener mapa
		
		Ruta ruta = new Ruta(dataName, dataFecha, dataHora, numeroCupo, dataPlaca, dataDescripcion);
		
		ruta.insertarEnDB(this);
	}
	
	public void lanzarListaRutas(){
		Intent lista = new Intent(this, ListaRutasDisponibles.class);
		startActivity(lista);
	}
}
