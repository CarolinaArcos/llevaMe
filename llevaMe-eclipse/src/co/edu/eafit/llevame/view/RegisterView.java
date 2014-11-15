package co.edu.eafit.llevame.view;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Usuario;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class RegisterView extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_view, menu);
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
	
	public void onRegister(View view) {
		EditText userName = (EditText) findViewById(R.id.user);
		EditText password = (EditText) findViewById(R.id.password);
		
		String dataUsername = userName.getText().toString();
		String dataPassword = password.getText().toString();
		
		validarInformacion();
	
	}
	
	public void onCancelar(View view) {
		finish();
	}
	
	public boolean validarInformacion()  {
		EditText userName = (EditText) findViewById(R.id.user);
		EditText password = (EditText) findViewById(R.id.password);
		EditText password2 = (EditText) findViewById(R.id.password2);
		EditText correo = (EditText) findViewById(R.id.email);
		
		String dataUsername = userName.getText().toString();
		String dataPassword = password.getText().toString();
		String dataPassword2 = password2.getText().toString();
		String dataCorreo = correo.getText().toString();
		
		//Validacion no null
		if (!validarContenido(dataUsername)) {
			toast("El campo Usuario no puede ser vacio");
			return false;
		}
		if (!validarContenido(dataPassword) || !validarContenido(dataPassword2)) {
			toast("Los campos Contraseña Nueva y Confirme Constraseña no puede ser vacio");
			return false;
		}
		if (!validarContenido(dataCorreo)) {
			toast("El campo Correo Electronico no puede ser vacio");
			return false;
		}
		
		//Validacion contraseñas iguales
		if(!dataPassword.equals(dataPassword2)){
			toast("Las contrasenas no coinciden");
			return false;
		}
		
		//Validar Longitud
		if (!validarLongitud(dataUsername)) {
			toast("El campo Usuario debe tener minimo 4 caracteres y maximo 20");
			return false;
		}
		
		if (!validarLongitud(dataPassword)) {
			toast("La contraseña debe tener minimo 4 caracteres y maximo 20");
			return false;
		}
		
		if (dataUsername.contains(" ") || dataPassword.contains(" ")) {
			toast("El nombre de usuario no puede contener espacios");
			return false;
		}
		
		if (dataUsername.contains("'") || dataUsername.contains("\"") 
					|| dataPassword.contains("'") || dataPassword.contains("\"")) {
			toast("Ni el Usuario ni la contraseña pueden contener comillas");
			return false;
		}
		
		//Validacion no mayusculas username
		if (!dataUsername.toLowerCase().equals(dataUsername)) {
			toast("El Usuario no puede tener mayusculas");
			return false;
		}
		
		//TODO: validar correo
		
		//validar si existe usuario
		Usuario usr = new Usuario(dataUsername, dataPassword);
		new revisarUsuario(usr).execute();
		return true;
	}
	
	public boolean validarContenido(String info) {
		if (info.matches("")) {
			return false;
		}
		return true;
	}
	
	public boolean validarLongitud(String word) {
		if(word.length() < 4 || word.length() > 20) return false;
		return true;
	}
	
	public void toast(String mensaje) {
		Toast toast = Toast.makeText(this,mensaje, 3);
		toast.show();
	}
	
	private class addUsuario extends AsyncTask<Void, Void, Void> {

		Usuario usr = new Usuario();
		ProgressDialog pDialog;
		
    	public addUsuario(Usuario usr){
    		super();
    		this.usr = usr;
    	}
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
		        pDialog = new ProgressDialog(RegisterView.this);
		        pDialog.setMessage("Cargando...");
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(false);
		        pDialog.show();
        }

    	@Override
    	protected Void doInBackground(Void...params) {
    		ServiciosUsuario.getInstancia().registrarUsuario(usr);
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void v){
			//TODO: logear el usuario (guardar el login en el cel)
		
    		Intent main = new Intent(RegisterView.this, MenuTab.class);
	    	startActivity(main);
	    	pDialog.dismiss();
    	}
    }
	
	private class revisarUsuario extends AsyncTask<Void, Void, Boolean> {

		Usuario usr = new Usuario();
		ProgressDialog pDialog;
		
    	public revisarUsuario(Usuario usr){
    		super();
    		this.usr = usr;
    	}
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
		        pDialog = new ProgressDialog(RegisterView.this);
		        pDialog.setMessage("Cargando...");
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(false);
		        pDialog.show();
        }

    	@Override
    	protected Boolean doInBackground(Void...params) {
    		return ServiciosUsuario.getInstancia().getUsuario(usr.getUsername())!=null;
    	}

    	@Override
    	protected void onPostExecute(Boolean valido){
    		pDialog.dismiss();
    		if (valido) {
    			toast("El nombre de usuario ya existe");
    		} else {
    			new addUsuario(usr).execute();
    		}
    		
    	}
    }
}
