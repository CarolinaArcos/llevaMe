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
import co.edu.eafit.llevame.handlers.*;
import co.edu.eafit.llevame.model.Usuario;
import co.edu.eafit.llevame.services.ServiciosUsuario;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginView extends Activity {

	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "849154470486";

    static final String TAG = "llevaMe";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;

	
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
		
		context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
	}
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	        // Check device for Play Services APK.
	        checkPlayServices();
	    }

	    private boolean checkPlayServices() {
	        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	        if (resultCode != ConnectionResult.SUCCESS) {
	            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
	            } else {
	                Log.i(TAG, "This device is not supported.");
	                finish();
	            }
	            return false;
	        }
	        return true;
	    }
	    
	    private void storeRegistrationId(Context context, String regId) {
	        final SharedPreferences prefs = getGcmPreferences(context);
	        int appVersion = getAppVersion(context);
	        Log.i(TAG, "Saving regId on app version " + appVersion);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(PROPERTY_REG_ID, regId);
	        editor.putInt(PROPERTY_APP_VERSION, appVersion);
	        editor.commit();
	    }
	
	    private String getRegistrationId(Context context) {
	        final SharedPreferences prefs = getGcmPreferences(context);
	        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	        if (registrationId.isEmpty()) {
	            Log.i(TAG, "Registration not found.");
	            return "";
	        }
	        // Check if app was updated; if so, it must clear the registration ID
	        // since the existing regID is not guaranteed to work with the new
	        // app version.
	        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	        int currentVersion = getAppVersion(context);
	        if (registeredVersion != currentVersion) {
	            Log.i(TAG, "App version changed.");
	            return "";
	        }
	        return registrationId;
	    }
	    
	    private void registerInBackground() {
	        new AsyncTask<Void, Void, String>() {
	            @Override
	            protected String doInBackground(Void... params) {
	                String msg = "";
	                try {
	                    if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(context);
	                    }
	                    regid = gcm.register(SENDER_ID);
	                    msg = "Device registered, registration ID=" + regid;

	                    // You should send the registration ID to your server over HTTP, so it
	                    // can use GCM/HTTP or CCS to send messages to your app.
	                    sendRegistrationIdToBackend();

	                    // For this demo: we don't need to send it because the device will send
	                    // upstream messages to a server that echo back the message using the
	                    // 'from' address in the message.

	                    // Persist the regID - no need to register again.
	                    storeRegistrationId(context, regid);
	                } catch (IOException ex) {
	                    msg = "Error :" + ex.getMessage();
	                    // If there is an error, don't just keep trying to register.
	                    // Require the user to click a button again, or perform
	                    // exponential back-off.
	                }
	                return msg;
	            }

	            @Override
	            protected void onPostExecute(String msg) {}
	            
	        }.execute(null, null, null);
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
		new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    Bundle data = new Bundle();
                    data.putString("my_message", "Notificacion de llevaMe");
                    data.putString("my_action", "co.edu.llevame.handlers.ECHO_NOW");
                    String id = Integer.toString(msgId.incrementAndGet());
                    gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                    msg = "Sent message";
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	Log.d("Notification", msg);
            }
        }.execute(null, null, null);
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
	
	private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginView.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    private void sendRegistrationIdToBackend() {
      // Your implementation here.
    }
}

