package co.edu.eafit.llevame.view;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.PartialRegexInputFilter;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.model.Ubicacion;
import co.edu.eafit.llevame.services.ServiciosRuta;

public class FormularioCrearRuta extends Activity {
	
	private ImageButton mapa;
	private String [] markerSnippet = {""};
	private double [] markerLat = {0.0};
	private double [] markerLong = {0.0};
	private Calendar calendar;
	private EditText fecha;
	private EditText hora;
	private EditText placa;
	private int year, month, day, hour, min;

	protected static final int REQUEST_CODE = 10;
	
	private int idUsrLoggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario_crear_ruta);
		
		SharedPreferences settings = getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
		idUsrLoggedIn = settings.getInt(SharedPreferencesHandler.LOGIN_KEY, -1);
		
		mapa = (ImageButton) findViewById(R.id.image);
		mapa.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				desplegarMapa();
			}
		});
	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		hora = (EditText) findViewById(R.id.horaFormulario);
		fecha = (EditText) findViewById(R.id.fechaFormulario);
		placa = (EditText) findViewById(R.id.placaFormulario);       
		final String regex = "[A-Z]{3}[0-9]{3}";  
        
		placa.setFilters(  
		    new InputFilter[] {  
		        new PartialRegexInputFilter(regex)  
		    }  
		);  
		placa.addTextChangedListener(  
		    new TextWatcher(){  
		  
		            @Override  
		            public void afterTextChanged(Editable s) {  
		                String value  = s.toString();
		                
		                if(value.matches(regex))  
		                    placa.setTextColor(Color.BLACK);  
		                else  
		                    placa.setTextColor(Color.RED);  
		            }  
		  
		            @Override  
		            public void beforeTextChanged(CharSequence s, int start,  
		                int count, int after) {}  
		  
		            @Override  
		            public void onTextChanged(CharSequence s, int start,  
		               int before, int count) {}  
		             
		         }  
		);  
		
		calendar = Calendar.getInstance();
	    year = calendar.get(Calendar.YEAR);
	    month = calendar.get(Calendar.MONTH);
	    day = calendar.get(Calendar.DAY_OF_MONTH);
	    hour = calendar.get(Calendar.HOUR_OF_DAY);
	    min = calendar.get(Calendar.MINUTE);
	    showTime(hour, min);
	    showDate(year, month+1, day);	    
	}
	
	@SuppressWarnings("deprecation")
	public void setDate(View view) {
		showDialog(999);
	}
	@SuppressWarnings("deprecation")
	public void setTime(View view) {
		showDialog(998);
	}

	private String numberToString(int number) {
		String num = "";
		if(number<10) num="0"+number;
		else num=""+number;
		return num;
	}
	
	   @Override
	   protected Dialog onCreateDialog(int id) {
	      if (id == 999) {
	         return new DatePickerDialog(this, myDateListener, year, month, day);
	       }
	      if (id == 998) {
	    	  return new TimePickerDialog(this, myTimeListener, hour, min,true);
		   }
	      return null;
	   }

	   private DatePickerDialog.OnDateSetListener myDateListener
	   = new DatePickerDialog.OnDateSetListener() {

	   @Override
	   public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
	      // TODO Auto-generated method stub
	      // arg1 = year
	      // arg2 = month
	      // arg3 = day
	      showDate(arg1, arg2+1, arg3);
	   }
	   };
	   
	   private TimePickerDialog.OnTimeSetListener myTimeListener
	   = new TimePickerDialog.OnTimeSetListener() {

	@Override
	public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		// arg1 = hour
	    // arg2 = minute
		showTime(arg1,arg2);
	}
	   };

	   private void showTime(int hour, int min) {
		   this.hour = hour;
		   this.min = min;
		   
		   hora.setText(numberToString(hour).concat(":").concat(numberToString(min)));
		}   
	   
	private void showDate(int year, int month, int day) {
		this.year = year;
		this.month = month-1;
		this.day = day;
		
		fecha.setText(numberToString(day).concat("/").concat(numberToString(month)).concat("/").concat(numberToString(year)));
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
    		Toast toast = Toast.makeText(this, "Su ruta ha sido creada exitosamente", Toast.LENGTH_SHORT);
    		toast.show(); 	
		} else {
			Toast toast = Toast.makeText(this, "Formulario Invalido. Favor ingrese los datos correctos", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	public Ruta crearRuta() {
		EditText hora = (EditText) findViewById(R.id.horaFormulario);
		EditText fecha = (EditText) findViewById(R.id.fechaFormulario);
		EditText cupo = (EditText) findViewById(R.id.cupoFormulario);		
		EditText descripcion = (EditText) findViewById(R.id.descripcionFormulario);
		EditText nombre = (EditText) findViewById(R.id.nombreRutaFormulario);
		EditText placa = (EditText) findViewById(R.id.placaFormulario);
		String dataHora = hora.getText().toString();
		String dataFecha = fecha.getText().toString();
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
		ruta.setConductor(idUsrLoggedIn);
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
		EditText nombre = (EditText) findViewById(R.id.nombreRutaFormulario);
		
		String dataName = nombre.getText().toString();
		String dataFecha = fecha.getText().toString(); //AAAA/MM/DD
		String dataHora = hora.getText().toString(); //HH:mm
		String dataCupo = cupo.getText().toString();
		String dataPlaca = placa.getText().toString();
		
		final String regex = "[A-Z]{3}[0-9]{3}";
		
		//Validacion que no sean vacios
		if(!validarContenido(dataName)) {
			toast("El Nombre no puede ser vacío");
			return false;
		}
		if(!validarContenido(dataFecha)) {
			toast("La Fecha no puede ser vacío");
			return false;
		}
		if(!validarContenido(dataHora)) {
			toast("La Hora no puede ser vacío");
			return false;
		}
		if(!validarContenido(dataCupo)) {
			toast("El Cupo no puede ser vacío");
			return false;
		}
		if(!validarContenido(dataPlaca)) {
			toast("La Placa no puede ser vacío");
			return false;
		}
		
		//Validacion numeros		
		if(!isNumeric(dataCupo)) {
			toast("El cupo debe ser un número");
			return false;
		}
		
		//Validacion rangos
		if (Integer.parseInt(dataCupo)<=0){
			toast("Valor del Cupo incorrecto");
			return false;
		}
		
		//Validacion placa
		if(!dataPlaca.matches(regex)) {
			toast("Formato de Placa incorrecto");
			return false;
		}
		
		//Validacion fecha y hora futuras
		if (validarFechaFutura(year, month, day, hour, min)) {
			toast("Hora y/o Fecha incorrecta, la hora y/o el dia ingresado ya paso");
			return false;
		}
		
		//Validar que se haya trazado una ruta
		if (markerSnippet.length<2) {
			toast("Debe ingresar un recorrido para la ruta");
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
	
	public boolean validarFechaFutura(int year, int month, int day, 
			int hour, int minute) {

		Calendar tiempoActual = Calendar.getInstance();
		Calendar dateTime = Calendar.getInstance();
		dateTime.set(year, month, day, hour, minute);
		if(tiempoActual.before(dateTime)) return false;
		return true;
	}
	
	public void toast(String mensaje) {
		Toast toast = Toast.makeText(this,mensaje, Toast.LENGTH_SHORT);
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
