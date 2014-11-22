package co.edu.eafit.llevame.view;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Invitacion;
import co.edu.eafit.llevame.model.Usuario;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class Resultados extends ListFragment {

	private Usuario[] datosResultados;
	private String username;
	private int position;
	private int idUsrLoggedIn;
	
	
	public static Resultados newInstance(String username) {
		Resultados r = new Resultados();
		r.username = username;
		
		
		return r;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		SharedPreferences settings = getActivity().getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
		idUsrLoggedIn = settings.getInt(SharedPreferencesHandler.LOGIN_KEY, -1);
		
		String[] strs = {};
		setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, strs));
		
		if(username!=null && !username.isEmpty()) {
			new BuscarUsuariosLike(username).execute();
		}
		
	}
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d("Pos Selected", position+"");
    	
    	final Usuario usr = datosResultados[position];
    	
    	//preguntar por confirmacion de enviar invitacion
    	new AlertDialog.Builder(getActivity())
	        .setIcon(R.drawable.ic_action_add_group)
	        .setTitle("Invitar amigo")
	        .setMessage("Desea invitar al usuario '"+usr.getUsername()+"' como amigo?")
	        .setPositiveButton("Si", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	// enviar invitacion TODO: cambiar el id al nombre de usuario
	        	Invitacion inv = new Invitacion(-1, "",
	        			usr.getId(), false, Invitacion.USUARIO, idUsrLoggedIn);
	        	
	        	new EnviarInvitacionUsuario().execute(inv);
	        }
	
	    })
	    .setNegativeButton("No", null)
	    .show();
    	
    	
    }
	
	public class BuscarUsuariosLike extends AsyncTask<Void, Void, Usuario[]> {
    	
		ProgressDialog pDialog;
		String username;
		
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Buscando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    	
    	
    	public BuscarUsuariosLike(String username){
    		super();
    		this.username = username;
    	}
    	
    	@Override
    	protected Usuario[] doInBackground(Void... params) {
    		return ServiciosUsuario.getInstancia().getUsuariosLike(username, idUsrLoggedIn);
    	}

    	@Override
    	protected void onPostExecute(final Usuario[] u){
    		pDialog.dismiss();
    		String[] nombres = {};
    		
    		if(u != null){
    			nombres = new String[u.length];
    			for(int i = 0; i<u.length; i++){
    				nombres[i] = u[i].getUsername();
    			}
    	    	
    		}
    		
    		//Poner datos en lista
	    	ArrayAdapter<String> adapter 
	    	= new ArrayAdapter<String>(getActivity(),
	    			android.R.layout.simple_list_item_1, nombres);
	    	
	    	
	    	setListAdapter(adapter);	
                
    		//Actualizar lista de usuarios
    		datosResultados = u;
    	}	
    }
	
	
	public class EnviarInvitacionUsuario extends AsyncTask<Invitacion, Void, Void> {
		
		public EnviarInvitacionUsuario(){
			super();
		}

		@Override
		protected Void doInBackground(Invitacion...params) {
			Invitacion inv = params[0];
			String nombre = ServiciosUsuario.getInstancia().getUsuario(inv.getIdRef()).getUsername();
			inv.setMensaje(nombre +" te desea agregar como amigo");
			
			ServiciosEvento.getInstancia().ingresarInvitacion(inv);
			return null;
		}

		@Override
		protected void onPostExecute(Void v){
			getActivity().finish();
		}

	}
}
