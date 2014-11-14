package co.edu.eafit.llevame.view;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Invitacion;
import co.edu.eafit.llevame.model.Usuario;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class Resultados extends ListFragment {

	private Usuario[] datosResultados;
	private String username;
	private int position;
	
	
	public static Resultados newInstance(String username) {
		Resultados r = new Resultados();
		r.username = username;
		
		
		return r;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		String[] strs = {};
		setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, strs));
		
		if(username!=null && !username.isEmpty()) {
			new BuscarUsuariosLike().execute(username);
		}
		
	}
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d("Pos Selected", position+"");
    	
    	Usuario usr = datosResultados[position];
    	
    	//preguntar por confirmacion de enviar invitacion
    	new AlertDialog.Builder(getActivity())
	        .setIcon(R.drawable.ic_action_add_group)
	        .setTitle("Invitar amigo")
	        .setMessage("Desea invitar al usuario '"+usr.getUsername()+"' como amigo?")
	        .setPositiveButton("Si", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	//TODO: enviar invitacion
	        }
	
	    })
	    .setNegativeButton("No", null)
	    .show();
    	
    	
    }
	
	public class BuscarUsuariosLike extends AsyncTask<String, Void, Usuario[]> {
    	
		ProgressDialog pDialog;
		
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Buscando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    	
    	
    	public BuscarUsuariosLike(){
    		super();
    	}
    	
    	@Override
    	protected Usuario[] doInBackground(String... params) {
    		return ServiciosUsuario.getInstancia().getUsuariosLike(params[0]);
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
			ServiciosEvento.getInstancia().ingresarInvitacion(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void v){
			
		}

	}
}
