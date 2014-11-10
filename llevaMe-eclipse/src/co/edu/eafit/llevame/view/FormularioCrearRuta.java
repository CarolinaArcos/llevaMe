package co.edu.eafit.llevame.view;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
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
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.model.Ubicacion;
import co.edu.eafit.llevame.services.ServiciosRuta;

public class FormularioCrearRuta extends Activity {
	
	private ImageButton mapa;
	private String [] markerSnippet = {""};
	private double [] markerLat = {0.0};
	private double [] markerLong = {0.0};
	protected static final int REQUEST_CODE = 10;

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
		getMenuInflater().inflate(R.menu.formulario_crear_ruta, menu);
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
	
	public void onAceptar(View view){
		boolean correct = validarFormulario();
		if (correct ==true) {
			Ruta r = crearRuta();
			new addRuta(r).execute();
    		Toast toast = Toast.makeText(this, "Su ruta ha sido creada exitosamente", 3);
    		toast.show(); 	
		} else {
			Toast toast = Toast.makeText(this, "Formulario Invalido. Favor ingrese los datos correctos", 3);
			toast.show();
		}
	}
	
	public Ruta crearRuta() {
		EditText hora = (EditText) findViewById(R.id.horaFormulario);
		EditText fecha = (EditText) findViewById(R.id.fechaFormulario);
		EditText cupo = (EditText) findViewById(R.id.cupoFormulario);
		EditText placa = (EditText) findViewById(R.id.placaFormulario);
		EditText descripcion = (EditText) findViewById(R.id.descripcionFormulario);
		EditText nombre = (EditText) findViewById(R.id.nombreRutaFormulario);
		
		String dataHora = hora.getText().toString();
		String dataFecha = fecha.getText().toString();
		dataFecha.replace('/', '-');//pasar a AAAA-MM-DD
		String dataCupo = cupo.getText().toString();
		int numeroCupo = -1;
		if(!dataCupo.equals("") && (Integer.parseInt(dataCupo)>0)) numeroCupo = Integer.parseInt(dataCupo);
		String dataPlaca = placa.getText().toString();
		String dataDescripcion = descripcion.getText().toString();
		String dataName = nombre.getText().toString();
		
		String fechaHora = dataFecha.concat(" ").concat(dataHora);
		
		//TODO: obtener el nombre de usuario que inicio sesion
		
		//obtener recorrido
		Ubicacion[] recorrido = new Ubicacion[markerSnippet.length];
		for(int i = 0; i< recorrido.length; i++){
			Ubicacion u = new Ubicacion();
			
			u.setNombre(markerSnippet[i]);
			u.setLatitud(markerLat[i]);
			u.setLongitud(markerLong[i]);
			
			recorrido[i] = u;
		}
		
		
		Ruta ruta = new Ruta(0, dataName, fechaHora, numeroCupo, dataDescripcion, dataPlaca);
		ruta.setRecorrido(recorrido);
		return ruta;
	}
	
	public void desplegarMapa() {
		Intent intent = new Intent(this,ViewMap.class);
		intent.putExtra("markerSnippet", markerSnippet);
		intent.putExtra("markerLat", markerLat);
		intent.putExtra("markerLong", markerLong);
        startActivityForResult(intent, REQUEST_CODE);
	}
	
	public boolean validarFormulario() {
		EditText hora = (EditText) findViewById(R.id.horaFormulario);
		EditText fecha = (EditText) findViewById(R.id.fechaFormulario);
		EditText cupo = (EditText) findViewById(R.id.cupoFormulario);
		EditText placa = (EditText) findViewById(R.id.placaFormulario);
		EditText descripcion = (EditText) findViewById(R.id.descripcionFormulario);
		EditText nombre = (EditText) findViewById(R.id.nombreRutaFormulario);
		
		String dataName = nombre.getText().toString();
		String dataFecha = fecha.getText().toString(); //AAAA/MM/DD
		String dataHora = hora.getText().toString(); //HH:mm
		String dataCupo = cupo.getText().toString();
		String dataPlaca = placa.getText().toString();
		String dataDescripcion = descripcion.getText().toString();
		
		//Validacion que no sean vac�o
		if(!validarContenido(dataName)) {
			toast("El Nombre no puede ser vac�o");
			return false;
		}
		if(!validarContenido(dataFecha)) {
			toast("La Fecha no puede ser vac�o");
			return false;
		}
		if(!validarContenido(dataHora)) {
			toast("La Hora no puede ser vac�o");
			return false;
		}
		if(!validarContenido(dataCupo)) {
			toast("El Cupo no puede ser vac�o");
			return false;
		}
		if(!validarContenido(dataPlaca)) {
			toast("La Placa no puede ser vac�o");
			return false;
		}
		//Validacion longitud
		if (!validarLongitudHoraFecha(dataHora,dataFecha)) {
			toast("El formato de la Fecha o la Hora no es correcto");
			return false;
		}
		
		String dia = dataFecha.substring(8);
		String mes = dataFecha.substring(5,7);
		String year = dataFecha.substring(0,4);
		String hour = dataHora.substring(0,2);
		String minute = dataHora.substring(3);
		
		//Validacion numeros		
		if(!isNumeric(dataCupo)) {
			toast("El cupo debe ser un n�ero");
			return false;
		}
		if ((!isNumeric(dia)) || (!isNumeric(mes)) || (!isNumeric(year))) {
			toast("El dia, el mes y el year de la fecha deben ser numeros");
			return false;
		}
		if (!isNumeric(hour) || !isNumeric(minute)) {
			toast("La hora y los minutos deben ser numeros");
			return false;
		}
		//Validacion rangos
		if (Integer.parseInt(dia)>31 || Integer.parseInt(mes)>12 || 
				Integer.parseInt(hour)>24 || Integer.parseInt(minute)>59 || 
				Integer.parseInt(dataCupo)<=0){
			toast("Valores en Hora, Fecha y/o Cupo incorrectos");
			return false;
		}
		//Validacion caracteres
		if ((dataFecha.charAt(4)!='/' && dataFecha.charAt(4)!='-')
			|| (dataFecha.charAt(7)!='/' && dataFecha.charAt(7)!='-')
			|| dataHora.charAt(2)!=':') {
			toast("Formato de Hora y/o Fecha incorrecto");
			return false;
		}
		//Validacion fecha y hora futuras
		if (validarFechaFuruta(year, mes, dia, hour, minute)) {
			toast("Fecha incorrecta, el dia ingresado ya paso");
			return false;
		}
		
		return true;
	}

	public boolean validarContenido(String info) {
		if (info.matches("")) {
			return false;
		}
		return true;
	}
	
	public boolean validarFechaFuruta(String year, String mes, String dia, 
			String hour, String minute) {
		
		Date tiempoActual = Calendar.getInstance().getTime();
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(mes),
				Integer.parseInt(dia), Integer.parseInt(hour), Integer.parseInt(minute));
		Date calIngresado = cal.getTime();
		boolean after = tiempoActual.after(calIngresado);
		if (after == false) {
			return false;
		}
		return true;
	}

	public boolean validarLongitudHoraFecha(String hora, String fecha) {
		if(hora.length()!=5) return false;
		if(fecha.length()!=10) return false;
		return true;
	}
	
	public void toast(String mensaje) {
		Toast toast = Toast.makeText(this,mensaje, 3);
		toast.show();
	}
	
	private static boolean isNumeric(String cadena){
		 try {
		  Integer.parseInt(cadena);
		  return true;
		 } catch (NumberFormatException nfe){
		  return false;
		 }
	}
	
	private class addRuta extends AsyncTask<Void, Void, Void> {

		Ruta ruta = new Ruta();
		
    	public addRuta(Ruta ruta){
    		super();
    		this.ruta = ruta;
    	}

    	@Override
    	protected Void doInBackground(Void...params) {
    		ServiciosRuta.getInstancia().addRuta(ruta);
    		ServiciosRuta.getInstancia().ingresarRecorrido(ruta.getRecorrido(), ruta.getId());
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void v){
    		finish();
    	}
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
	    	markerSnippet = data.getStringArrayExtra("markersSnippet");
	    	markerLat = data.getDoubleArrayExtra("markersLat");
	    	markerLong = data.getDoubleArrayExtra("markersLong");
	    }
	}
	
	@Override
    public void onBackPressed() {}
}
