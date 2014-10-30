package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.RutaListAdapter;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.services.ServiciosRuta;

public class ListaRutasDisponibles extends Activity {

	//lista en la UI
	private ListView lista;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rutas);
        
        
        lista = (ListView) findViewById(R.id.listaRutas);
        
        lista.setOnItemClickListener(new OnItemClickListener() {
        	
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
            	int theId = (int) id;
            	DialogdesplegarDetalles((int)id);
            }
            
         });

    }
    
    public void DialogdesplegarDetalles(int id){
    	Intent intent = new Intent(this,DetallesRuta.class);
    	int theId = intent.getIntExtra("id", -1);
    	intent.putExtra("id", id);
    	startActivity(intent);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {       
    	getMenuInflater().inflate(R.menu.main, menu);
		new TraerListaRuta().execute();
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
    
    private class TraerListaRuta extends AsyncTask<Void, Void, Ruta[]> {


    	public TraerListaRuta(){
    		super();
    	}

    	@Override
    	protected Ruta[] doInBackground(Void... params) {
    		return ServiciosRuta.obtenerInstancia().getArregloRutas("/rutas");
    	}

    	@Override
    	protected void onPostExecute(Ruta[] r){

    			RutaListAdapter adapter = new RutaListAdapter(ListaRutasDisponibles.this, R.layout.elemento_lista_rutas, r);
        		lista.setAdapter(adapter);
    	}

    }

}


