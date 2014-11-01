package co.edu.eafit.llevame.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import co.edu.eafit.llevame.R;
 
public class MenuTab extends TabActivity {
    // TabSpec Names
    private static final String HOME = "Home";
    private static final String SOCIAL = "Social";
    private static final String NOTIFICATIONS = "Notifications";
    private static final String SETTINGS = "Settings";
    private static final String ROUTES = "Routes";
    private static final String POINTS = "Points";
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tab);
         
        TabHost tabHost = getTabHost();
         
        // Home Tab
        TabSpec homeSpec = tabHost.newTabSpec(HOME);
        // Tab Icon
        homeSpec.setIndicator(HOME);
        Intent listRoutes = new Intent(this, ListaRutasDisponibles.class);
        // Tab Content
        homeSpec.setContent(listRoutes);
         
        // Routes Tab
        TabSpec routesSpec = tabHost.newTabSpec(ROUTES);
        routesSpec.setIndicator(ROUTES);
        Intent myRoutes = new Intent(this, MisRutas.class);
        routesSpec.setContent(myRoutes);
         
        // Social Tab
        TabSpec socialSpec = tabHost.newTabSpec(SOCIAL);
        socialSpec.setIndicator(SOCIAL);
        Intent social = new Intent(this, FormularioCrearRuta.class); // Change to social view
        socialSpec.setContent(social);
        
     // Points Tab
        TabSpec pointsSpec = tabHost.newTabSpec(POINTS);
        // Tab Icon
        pointsSpec.setIndicator(POINTS);
        Intent points = new Intent(this, FormularioCrearRuta.class); // Change to points view
        // Tab Content
        pointsSpec.setContent(points);
         
        // Notifications Tab
        TabSpec notificationsSpec = tabHost.newTabSpec(NOTIFICATIONS);
        notificationsSpec.setIndicator(NOTIFICATIONS);
        Intent notifications = new Intent(this, NotificationsView.class);
        routesSpec.setContent(myRoutes);
         
        // Settings Tab
        TabSpec settingsSpec = tabHost.newTabSpec(SETTINGS);
        settingsSpec.setIndicator(SETTINGS);
        Intent settings = new Intent(this, FormularioCrearRuta.class); // Change to settings view
        settingsSpec.setContent(settings);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(homeSpec); // Adding Inbox tab
        tabHost.addTab(routesSpec); // Adding Outbox tab
        tabHost.addTab(socialSpec); // Adding Profile tab
        tabHost.addTab(pointsSpec); // Adding Inbox tab
        tabHost.addTab(notificationsSpec); // Adding Outbox tab
        tabHost.addTab(settingsSpec); // Adding Profile tab
    }
}
