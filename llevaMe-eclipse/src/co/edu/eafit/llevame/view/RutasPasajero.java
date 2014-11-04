package co.edu.eafit.llevame.view;

import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.R.id;
import co.edu.eafit.llevame.R.layout;
import co.edu.eafit.llevame.R.menu;
import co.edu.eafit.llevame.handlers.RutaListAdapter;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosRuta;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RutasPasajero extends Activity {
	
	private ListView lista;
	private int idUsuario = 1; //TODO: obtener usuario

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rutas_pasajero);
		
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
		new TraerListaRutaPasajero().execute();
	}
	
	private class TraerListaRutaPasajero extends AsyncTask<Void, Void, Ruta[]> {


    	public TraerListaRutaPasajero(){
    		super();
    	}

    	@Override
    	protected Ruta[] doInBackground(Void... params) {
    		return ServiciosRuta.getInstancia().getArregloRutas("/rutas/pasajero/"+idUsuario);
    	}

    	@Override
    	protected void onPostExecute(Ruta[] r){

    			RutaListAdapter adapter = new RutaListAdapter(RutasPasajero.this, R.layout.elemento_lista_rutas, r);
        		lista.setAdapter(adapter);
    	}

    }
}
