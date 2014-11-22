package co.edu.eafit.llevame.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import co.edu.eafit.llevame.handlers.ServerHandler;
import co.edu.eafit.llevame.model.Ruta;
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
	
	public Usuario getUsuario(int id){
		String url = "/usuarios/"+id;
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
	
	public Usuario[] getUsuariosLike(String username){
		String url = "/usuarios/like?usr="+username;
		return getUsuariosList(url);
	}
	
	public Usuario[] getUsuariosLike(String username, int id) {
		String url = "/usuarios/like?usr="+username+"&idUsr="+id;
		return getUsuariosList(url);
	}
	
	public Usuario[] getPasajeros(int id) {
		String url = "/rutas/pasajeros?ruta="+id;
		return getUsuariosList(url);
	}
	
	
	private Usuario[] getUsuariosList(String url){
		Usuario[] usrs;
		
		try{
			String serverResp = ServerHandler.getServerResponse(url);
			if(serverResp!=null && !serverResp.isEmpty()){
				JSONArray losUsuarios = new JSONArray(serverResp);
				String usrsString[]= new String[losUsuarios.length()];
				usrs = new Usuario[usrsString.length];
				
				for (int i = 0; i<losUsuarios.length(); i++) {
					JSONObject usuario = losUsuarios.getJSONObject(i);
					Usuario u = new Usuario();
					
					u.setId(usuario.getInt("id"));
					u.setUsername(usuario.getString("username"));
					u.setPassword(usuario.getString("password"));
					u.setPuntos(usuario.getInt("puntos"));
					try{
						u.setPickUp(usuario.getInt("pickUp"));
					} catch(JSONException ex){
						
					}
					
					
					usrs[i] = u;
					
				}
				return usrs;
			} else {
				return null;
			}
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}
	
	public void agregarAmistad(int id1, int id2){
		String url = "/usuarios/amigos?usr1="+id1+"&usr2="+id2;
		ServerHandler.getServerResponsePost(url);
	}
}
