package co.edu.eafit.llevame.services;

import org.json.JSONObject;

import android.util.Log;
import co.edu.eafit.llevame.handlers.ServerHandler;
import co.edu.eafit.llevame.model.Usuario;

public class ServiciosUsuario {

	private static ServiciosUsuario instancia;

	private ServiciosUsuario() {

	}
	
	public static ServiciosUsuario getInstancia() {
		if (instancia==null) {
			instancia = new ServiciosUsuario();
		}
		return instancia;
	}
	
	
	public void registrarUsuario(Usuario usr){
		String url = "/usuarios";
		
		try {
			//pasar usr a JSON
			JSONObject u = new JSONObject();
			u.put("username", usr.getUsername());
			u.put("password", usr.getPassword());
			
			String serverResp = ServerHandler.getServerResponsePost(url, u);
			
			JSONObject usrNuevo = new JSONObject(serverResp);
			usr.setId(usrNuevo.getInt("id"));
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
	}
	
	public Usuario getUsuario(String username){
		String url = "/usuarios?usr="+username;
		Usuario usr = new Usuario();
		
		try {
			String serverResp = ServerHandler.getServerResponse(url);
			Log.d("serverResp",""+serverResp);
			
			JSONObject u = new JSONObject(serverResp);
			usr.setId(u.getInt("id"));
			usr.setUsername(u.getString("username"));
			usr.setPassword(u.getString("password"));
			usr.setPuntos(u.getInt("puntos"));
			
			return usr;
		} catch (Exception ex) {
			Log.e("getUsuario","Error!", ex);
			return null;
		}
	}
}
