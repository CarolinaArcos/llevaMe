package co.edu.eafit.llevame.view;

import java.util.ArrayList;

import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Ruta;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class ListaRutasDisponibles extends Activity {

	//lista en la UI
	ListView lista;

	//lista de la BDs
	ArrayList<Ruta> rutas;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //lista de rutas
        lista = (ListView) findViewById(R.id.listaRutas);
        
//        lista.add
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    
    
}
