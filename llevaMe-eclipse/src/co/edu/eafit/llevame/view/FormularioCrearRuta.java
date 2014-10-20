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
import co.edu.eafit.llevame.handlers.RutaListAdapter;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosRuta;

public class FormularioCrearRuta extends Activity {
	
	private ImageButton mapa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario_crear_ruta);
		
		
		mapa = (ImageButton) findViewById(R.id.image);
		mapa.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				desplegarMapa();
			}
		});
	
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
		boolean correct = validarFormulario();
		if (correct ==true) {
			enviarFormulario();
			lanzarListaRutas();	
		} else {
			Log.d("error", "formulario invalido");
			Toast toast = Toast.makeText(this, "Favor ingrese los datos correctos", 3);
			toast.show();
		}

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
		if(!dataCupo.equals("") && (Integer.parseInt(dataCupo)>0)) numeroCupo = Integer.parseInt(dataCupo);
		String dataPlaca = placa.getText().toString();
		String dataDescripcion = descripcion.getText().toString();
		String dataName = nombre.getText().toString();
		
		//TODO: obtener el nombre de usuario que inicio sesion
		
		//TODO: obtener mapa
		
		//Ruta ruta = new Ruta(dataName, dataFecha, dataHora, numeroCupo, dataPlaca, dataDescripcion);
		
		//ruta.insertarEnDB(this);
	}
	
	public void lanzarListaRutas(){
		Intent lista = new Intent(this, ListaRutasDisponibles.class);
		startActivity(lista);
	}
	
	public void desplegarMapa() {
    	Intent intent = new Intent(this,ViewMap.class);
    	startActivity(intent);
	}
	
	public boolean validarFormulario() {
		EditText hora = (EditText) findViewById(R.id.horaFormulario);
		EditText fecha = (EditText) findViewById(R.id.fechaFormulario);
		EditText cupo = (EditText) findViewById(R.id.cupoFormulario);
		EditText placa = (EditText) findViewById(R.id.placaFormulario);
		EditText descripcion = (EditText) findViewById(R.id.descripcionFormulario);
		EditText nombre = (EditText) findViewById(R.id.nombreRutaFormulario);
		
		String dataName = nombre.getText().toString();
		//Validacion nombre
		if(dataName.matches("")) {
			Toast toast = Toast.makeText(this, "Nombre incorrecto (no puede estar vacio)", 3);
			toast.show();
			return false;			
		}
		
		String dataFecha = fecha.getText().toString();
		//Validacion Fecha
		if(dataFecha.matches("")) { //Validacion nulo
			Toast toast = Toast.makeText(this, "Fecha incorrecta (formato DD/MM/AAAA)", 3);
			toast.show();
			return false;			
		} else if (dataFecha.length()!=10) { //Validacion longitud
			Toast toast = Toast.makeText(this, "Fecha incorrecta, el formato debe ser DD/MM/AAAA - tamaño", 3);
			toast.show();
			return false;
		} 
		else {
			//Validacion formato DD/MM/AAAA
			String dia = dataFecha.substring(0,2);
			String mes = dataFecha.substring(3,5);
			String año = dataFecha.substring(6);
			if((!isNumeric(dia)) || (!isNumeric(mes)) || (!isNumeric(año))) {
				Toast toast = Toast.makeText(this, "Fecha incorrecta, el formato debe ser DD/MM/AAAA - numeros", 3);
				toast.show();
				return false;	
			}
			if((dataFecha.charAt(2)!='/') ||(dataFecha.charAt(5)!='/')){
				Toast toast = Toast.makeText(this, "Fecha incorrecta, el formato debe ser DD/MM/AAAA - /", 3);
				toast.show();
				return false;
			} 
		}
		//TODO: validar que la fecha no haya pasado
		
		String dataHora = hora.getText().toString();
		//TODO:Validacion Hora
		if(dataHora.matches("")) { //Validacion nulo
			Toast toast = Toast.makeText(this, "Hora incorrecta (formato HH:MM)", 3);
			toast.show();
			return false;			
		}else if (dataHora.length()!=5) { //Validacion longitud
			Toast toast = Toast.makeText(this, "Hora incorrecta, debe ser en formato HH:MM - tamaño", 3);
			toast.show();
			return false;
		}else {
			//validacion formato HH:MM
			String hour = dataHora.substring(0,2);
			String minutes = dataHora.substring(3);
			if((!isNumeric(hour)) || (!isNumeric(minutes))) {
				Toast toast = Toast.makeText(this, "Hora incorrecta, debe ser en formato HH:MM - numeros", 3);
				toast.show();
				return false;
			}else if (Integer.parseInt(hour)>24) {
				Toast toast = Toast.makeText(this, "Hora incorrecta, la hora no debe ser mayor a 24", 3);
				toast.show();
				return false;
			}else if (Integer.parseInt(minutes)>59) {
				Toast toast = Toast.makeText(this, "Hora incorrecta, los minutos no debe ser mayores a 59", 3);
				toast.show();
				return false;
			}else if (dataHora.charAt(2)!=':') {
				Toast toast = Toast.makeText(this, "Hora incorrecta, debe ser en formato HH:MM - :", 3);
				toast.show();
				return false;
			}
		}
		//TODO:validar que la hora no haya pasado si la fecha es actual
		
		String dataCupo = cupo.getText().toString();
		//Validacion cupo
		if(dataCupo.matches("")) { //Validacion nulo
			Toast toast = Toast.makeText(this, "Cupo incorrecto (numero mayor a cero)", 3);
			toast.show();
			return false;			
		}else if(!isNumeric(dataCupo)) { //Validacion numero
			Toast toast = Toast.makeText(this, "Cupo incorrecto, debe ser un número", 3);
			toast.show();
			return false;
		} else if (Integer.parseInt(dataCupo)<=0) { //Validacion numero mayor a 0
			Toast toast = Toast.makeText(this, "Cupo incorrecto, debe ser un número mayor a cero", 3);
			toast.show();
			return false;
		}
		
		String dataPlaca = placa.getText().toString();
		//TODO: validacion placa-vehiculo
		if(dataPlaca.matches("")) {
			Toast toast = Toast.makeText(this, "Placa incorrecta", 3);
			toast.show();
			return false;			
		}
		
		String dataDescripcion = descripcion.getText().toString();
		
		return true;
	}
	
	//TODO: cambiar isNumeric
	private static boolean isNumeric(String cadena){
		 try {
		  Integer.parseInt(cadena);
		  return true;
		 } catch (NumberFormatException nfe){
		  return false;
		 }
		}
	
//	private class añadirRuta extends AsyncTask<Ruta, Void, Ruta[]> {
//
//
//    	public añadirRuta(){
//    		super();
//    	}
//
//    	//@Override
//    	protected Ruta[] doInBackground(Ruta...params) {
//    		
//    	}
//
//    	@Override
//    	protected void onPostExecute(Ruta[] r){
//
//    			
//    	}
//
//    }
}
