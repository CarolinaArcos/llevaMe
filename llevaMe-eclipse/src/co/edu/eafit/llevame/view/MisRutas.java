package co.edu.eafit.llevame.view;

import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.R.id;
import co.edu.eafit.llevame.R.layout;
import co.edu.eafit.llevame.R.menu;
import android.app.Activity;
import android.content.Intent;
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
		getMenuInflater().inflate(R.menu.mis_rutas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onConductor(View view) {
		Intent rutasConductor = new Intent(this, RutasConductor.class);
    	startActivity(rutasConductor);
	}
	
	public void onPasajero(View view) {
		Intent rutasPasajero = new Intent(this, RutasPasajero.class);
    	startActivity(rutasPasajero);
	}
	
	public void onCrearRuta(View view) {
		Intent crearRuta = new Intent(this, FormularioCrearRuta.class);
    	startActivity(crearRuta);
	}
	@Override
    public void onBackPressed() {
    	
    }
}
