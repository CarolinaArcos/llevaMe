package co.edu.eafit.llevame;

import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.view.LoginView;
import co.edu.eafit.llevame.view.MenuTab;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Ajustes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajustes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajustes, menu);
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
	
	public void onLogout(View view) {
		SharedPreferences settings = getSharedPreferences(
				SharedPreferencesHandler.PREFS_NAME, 0);
		
	    SharedPreferences.Editor editor = settings.edit();
	    editor.remove(SharedPreferencesHandler.LOGIN_KEY);
	    editor.commit();
	    
	    Intent login = new Intent(this, LoginView.class);
    	startActivity(login);
	}
}
