package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.RutaListAdapter;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosRuta;

public class RutasPasajero extends Activity {
	
	private ListView lista;
	private int idUsuario;
	private ProgressDialog pDialog;
	private boolean cargado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rutas_pasajero);
		
		SharedPreferences settings = getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
		idUsuario = settings.getInt(SharedPreferencesHandler.LOGIN_KEY, -1);
		
		lista = (ListView) findViewById(R.id.listaRutasPasajero);
		
        lista.setOnItemClickListener(new OnItemClickListener() {
        	
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
            	int theId = (int) id;
            	DialogdesplegarDetalles((int)id);
            }
            
         });
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		new TraerListaRutaPasajero().execute();
	}

	public void DialogdesplegarDetalles(int id){
		Intent intent = new Intent(this,DetallesRutaPasajero.class);
		int theId = intent.getIntExtra("id", -1);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rutas_pasajero, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if(id == R.id.crear_ruta){
        	Intent formularioRuta = new Intent(this, FormularioCrearRuta.class);
        	startActivity(formularioRuta);
        	return true;
        }
        return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (cargado) {
			cargado = false;
		} else {
			new TraerListaRutaPasajero().execute();
		}
	}
	
	private class TraerListaRutaPasajero extends AsyncTask<Void, Void, Ruta[]> {

		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(RutasPasajero.this);
            pDialog.setMessage("Cargando Rutas...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            cargado = true;
        }

    	public TraerListaRutaPasajero(){
    		super();
    	}

    	@Override
    	protected Ruta[] doInBackground(Void... params) {
    		return ServiciosRuta.getInstancia().getArregloRutasPasajero(idUsuario);
    	}

    	@Override
    	protected void onPostExecute(final Ruta[] r){
    		Log.d("Post exc", "entro");
    		pDialog.dismiss();
    		runOnUiThread(new Runnable() {
                public void run() {
                	RutaListAdapter adapter = new RutaListAdapter(RutasPasajero.this, R.layout.elemento_lista_rutas, r);
            		lista.setAdapter(adapter);
                }
            });
    	}

    }
}
