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
            	//TODO: pasar parametro
            	int theId = (int) id;
            	//desplegarDetalles(theId);
            	DialogdesplegarDetalles((int)id);
            }
            
         });

    }
    
    public void DialogdesplegarDetalles(int id){
    	Intent intent = new Intent(this,DetallesRuta.class);
    	int theId = intent.getIntExtra("id", 10);
    	intent.putExtra("id", id);
    	startActivity(intent);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        
    	getMenuInflater().inflate(R.menu.main, menu);
		new TraerListaRuta().execute();
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
    		return ServiciosRuta.obtenerInstancia().getArregloRutas();
    	}

    	@Override
    	protected void onPostExecute(Ruta[] r){

    			RutaListAdapter adapter = new RutaListAdapter(ListaRutasDisponibles.this, R.layout.elemento_lista_rutas, r);
        		lista.setAdapter(adapter);
    	}

    }

}


