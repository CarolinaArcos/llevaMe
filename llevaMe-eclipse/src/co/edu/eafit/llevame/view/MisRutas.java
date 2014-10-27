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

public class MisRutas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mis_rutas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mis_rutas, menu);
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
	
	public void onConductor(View view) {
		//TODO: ir a lista rutas como conductor
	}
	
	public void onPasajero(View view) {
		//TODO: ir a lista rutas como pasajero
	}
	
	public void onCrearRuta(View view) {
		//TODO: ir a FormularioCrearRuta
	}
}
