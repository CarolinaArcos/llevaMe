package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import co.edu.eafit.llevame.R;

public class LoginView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_view);
	}
	
	public void onRegister(View view) {
		Intent register = new Intent(this, RegisterView.class);
    	startActivity(register);
	}
	
	public void onLogin(View view) {
		Intent menu = new Intent(this, MenuTab.class);
    	startActivity(menu);
	}
	
	public void onUser(View view) {
		
	}
	
	public void onPassword(View view) {
		
	}
}

