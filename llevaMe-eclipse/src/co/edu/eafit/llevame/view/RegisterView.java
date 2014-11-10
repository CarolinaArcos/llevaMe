package co.edu.eafit.llevame.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
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
import co.edu.eafit.llevame.services.ServiciosRuta;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class RegisterView extends Activity {
	
	 private Pattern pattern;
	 private static final String PASSWORD_PATTERN = 
             "(?=.*[^\'])(?=.*[^\t\n\r\f]).*";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register_view);
		
		pattern = Pattern.compile(PASSWORD_PATTERN);
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
		
		if (validarInformacion()) {
			Usuario usr = new Usuario(dataUsername, dataPassword);
	    	new addUsuario(usr).execute();
		}
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
		
		if(!dataPassword.equals(dataPassword2)){
			toast("Las contrasenas no coinciden");
			return false;
		}
		
//		if (dataUserName.contains(" ")) {
//			toast("El nombre de usuario no puede contener espacios");
//			return false;
//		}
		
		Matcher matcher = pattern.matcher(dataUsername);
		if (!matcher.matches()) {
			toast("El nombre de usuario no puede contener espacios ni caracteres especiales");
			return false;
		}
		
		//TODO: validar correo
		return true;
	}
	
	public void toast(String mensaje) {
		Toast toast = Toast.makeText(this,mensaje, 3);
		toast.show();
	}
	
	private class addUsuario extends AsyncTask<Void, Void, Void> {

		Usuario usr = new Usuario();
		
    	public addUsuario(Usuario usr){
    		super();
    		this.usr = usr;
    	}

    	@Override
    	protected Void doInBackground(Void...params) {
    		ServiciosUsuario.getInstancia().registrarUsuario(usr);
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Void v){
    		Intent login = new Intent(RegisterView.this, MenuTab.class);
	    	startActivity(login);
    	}
    }
}
