package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import co.edu.eafit.llevame.R;

public class BuscarUsuario extends Activity {
	
	ListView lista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscar_usuario);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buscar_usuario, menu);
		return true;
	}

	public void onSearch(View v){
		EditText search = (EditText) findViewById(R.id.campoBusqueda);
		String username = search.getText().toString();		

        // Check what fragment is currently shown, replace if needed.
        Resultados listResult = (Resultados)
                getFragmentManager().findFragmentById(R.id.listResult);
        
//        if (listResult == null) {
            // Make new fragment to show this selection.
            listResult = Resultados.newInstance(username);

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            
            ft.replace(R.id.listResult, listResult);
            
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
//        }
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
