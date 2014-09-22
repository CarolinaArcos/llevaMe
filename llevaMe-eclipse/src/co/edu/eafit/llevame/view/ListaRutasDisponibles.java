package co.edu.eafit.llevame.view;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.database.DatabaseHandler;


public class ListaRutasDisponibles extends Activity {

	//lista en la UI
	private ListView lista;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rutas);
        
        //lista de rutas
        lista = (ListView) findViewById(R.id.listaRutas);
        
        //ejemplo con array adapter---------------------
//        TODO: borrar y coger datos de la BDs
//        String[] test = {"hola", "mundo", "otro", "uno "};
//        
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, test);
//        
        
        //handler
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        
        //base de datos
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        
        
        
        String[] selectColumns = {DatabaseHandler.KEY_ID,
				        		DatabaseHandler.KEY_NAME,
				        		DatabaseHandler.KEY_DATE,
				        		DatabaseHandler.KEY_HOUR,
				        		DatabaseHandler.KEY_CAPACITY};
        
        //resultado del query de la BDs
        Cursor cursor = db.query(DatabaseHandler.TABLE_ROUTES,
        			selectColumns, null, null, null, null, null);
        
        //columnas del query a mostrar
        String[] fromColumns = {DatabaseHandler.KEY_NAME,
				        		DatabaseHandler.KEY_DATE,
				        		DatabaseHandler.KEY_HOUR,
				        		DatabaseHandler.KEY_CAPACITY};
        
        //a que vistas van los datos del query
        int[] toView = {R.id.tituloRuta,
        				R.id.diaRuta,
        				R.id.hora,
        				R.id.capacidad};
        
        //adaptador de datos a lista
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.elemento_lista_rutas,
        													cursor, fromColumns, toView, 0);
        
        lista.setAdapter(adapter);

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
        } else if(id == R.id.crear_ruta){
        	Intent formularioRuta = new Intent(this, FormularioCrearRuta.class);
        	startActivity(formularioRuta);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
}
