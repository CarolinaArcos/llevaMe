package co.edu.eafit.llevame.view;

import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.R.id;
import co.edu.eafit.llevame.R.layout;
import co.edu.eafit.llevame.R.menu;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DetallesRutaConductor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles_ruta_conductor);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalles_ruta_conductor, menu);
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
	
	public void onIniciar(View view) {
		//TODO: iniciar ruta
	}
	
	public void onllevaMe(View view) {
		//TODO: finalziar ruta
	}
	
	public void volverAMenu() {
		finish();
	}
	
	//TODO: En asyncTask poner volverAMenu()
}
