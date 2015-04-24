package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Usuario;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class LoginView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_view);
		
		SharedPreferences settings = getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
	    int logedInID = settings.getInt(SharedPreferencesHandler.LOGIN_KEY, -1);
	    
		Log.d("sharedPrefID", logedInID + "");
		if(logedInID != -1){
			Intent menu = new Intent(LoginView.this, MenuTab.class);
        	startActivity(menu);
		}
	}
	
	public void onRegister(View view) {
		Intent register = new Intent(this, RegisterView.class);
    	startActivity(register);
	}
	
	public void onLogin(View view) {
		EditText username = (EditText) findViewById(R.id.user);
		EditText password = (EditText) findViewById(R.id.password);
		
		String datoUsername = username.getText().toString();
		String datoPassword = password.getText().toString();
		
		Usuario usr = new Usuario(datoUsername, datoPassword);
		
		new RevisarLogin(usr).execute();
	}
	
	public void onUser(View view) {
		
	}
	
	public void onPassword(View view) {
		
	}
	
	public void toast(String mensaje) {
		Toast toast = Toast.makeText(this,mensaje, 3);
		toast.show();
	}
	
	private class RevisarLogin extends AsyncTask<Void, Void, Boolean> {

		Usuario usr = new Usuario();
		ProgressDialog pDialog;
		
    	public RevisarLogin(Usuario usr){
    		super();
    		this.usr = usr;
    	}
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
		        pDialog = new ProgressDialog(LoginView.this);
		        pDialog.setMessage("Cargando...");
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(false);
		        pDialog.show();
        }

    	@Override
    	protected Boolean doInBackground(Void...params) {
    		Usuario usrServer = ServiciosUsuario.getInstancia().getUsuario(usr.getUsername());
    		if(usrServer==null){
    			return false;
    		} else {
    			if(!usrServer.getPassword().equals(usr.getPassword())){
    				return false;
    			}
    		}
    		
    		usr = usrServer;
    		
    		return true;
    	}

    	@Override
    	protected void onPostExecute(Boolean valido){
    		pDialog.dismiss();
    		
    		if (valido) {
    			//ingresar
    			
    			// guardar login en local
    			SharedPreferences settings = getSharedPreferences(
    					SharedPreferencesHandler.PREFS_NAME, 0);
    			
    		    SharedPreferences.Editor editor = settings.edit();
    		    editor.putInt(SharedPreferencesHandler.LOGIN_KEY, usr.getId());
    		    editor.commit();
    			
    			Intent menu = new Intent(LoginView.this, MenuTab.class);
            	startActivity(menu);
    		} else {
    			toast("Usuario o Contraseña incorrecto");
    		}
    		
    		
    		
    		
    	}
    }
}

