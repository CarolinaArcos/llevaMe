package co.edu.eafit.llevame.view;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import co.edu.eafit.llevame.model.Usuario;
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
    		
                	
	    	String[] nombres = new String[u.length];
			for(int i = 0; i<u.length; i++){
				nombres[i] = u[i].getUsername();
				Log.d("nombres["+i+"]", nombres[i]);
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
	
}
