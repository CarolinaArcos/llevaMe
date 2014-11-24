package co.edu.eafit.llevame.view;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
 
@SuppressWarnings("deprecation")
public class MenuTab extends TabActivity {
	
	private int curTab = 0;
	private TabHost tabHost;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tab);
         
        tabHost = getTabHost();
        
        SharedPreferences userPrefs = getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
	    curTab = userPrefs.getInt(SharedPreferencesHandler.TAB_KEY, 0);
         
        // Home Tab
        TabSpec homeSpec = tabHost.newTabSpec(null);
        // Tab Icon
        homeSpec.setIndicator(null, getResources().getDrawable(R.drawable.ic_action_view_as_list));
        Intent listRoutes = new Intent(this, ListaRutasDisponibles.class);
        // Tab Content
        homeSpec.setContent(listRoutes);
         
        // Routes Tab
        TabSpec routesSpec = tabHost.newTabSpec(null);
        routesSpec.setIndicator(null, getResources().getDrawable(R.drawable.ic_action_place));
        Intent myRoutes = new Intent(this, MisRutas.class);
        routesSpec.setContent(myRoutes);
         
        // Social Tab
        TabSpec socialSpec = tabHost.newTabSpec(null);
        socialSpec.setIndicator(null, getResources().getDrawable(R.drawable.ic_action_group));
        Intent social = new Intent(this, Amigos.class);
        socialSpec.setContent(social);
        
     // Points Tab
        TabSpec pointsSpec = tabHost.newTabSpec(null);
        // Tab Icon
        pointsSpec.setIndicator(null, getResources().getDrawable(R.drawable.ic_action_important));
        Intent points = new Intent(this, Puntuacion.class);
        // Tab Content
        pointsSpec.setContent(points);
         
        // Notifications Tab
        TabSpec notificationsSpec = tabHost.newTabSpec(null);
        notificationsSpec.setIndicator(null, getResources().getDrawable(R.drawable.ic_action_web_site));
        Intent notifications = new Intent(this, ListEvents.class);
        notificationsSpec.setContent(notifications);
         
        // Settings Tab
        TabSpec settingsSpec = tabHost.newTabSpec(null);
        settingsSpec.setIndicator(null,getResources().getDrawable(R.drawable.ic_action_settings));
        Intent settings = new Intent(this, Ajustes.class);
        settingsSpec.setContent(settings);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(homeSpec); // Adding Inbox tab
        tabHost.addTab(routesSpec); // Adding Outbox tab
        tabHost.addTab(socialSpec); // Adding Profile tab
        tabHost.addTab(pointsSpec); // Adding Inbox tab
        tabHost.addTab(notificationsSpec); // Adding Outbox tab
        tabHost.addTab(settingsSpec); // Adding Profile tab
        
        tabHost.setCurrentTab(curTab);
    }
    
    
    @Override
    public void onResume() {
    	super.onResume();
    	tabHost.setCurrentTab(curTab);
    	
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	
    	SharedPreferences settings = getSharedPreferences(
				SharedPreferencesHandler.PREFS_NAME, 0);
		
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(SharedPreferencesHandler.TAB_KEY, tabHost.getCurrentTab());
	    editor.commit();
    }
    
    
    @Override
    public void onBackPressed() {
    	
    }
}
