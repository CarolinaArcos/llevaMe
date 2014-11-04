package co.edu.eafit.llevame.services;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import co.edu.eafit.llevame.handlers.ServerHandler;
import co.edu.eafit.llevame.model.Evento;
import co.edu.eafit.llevame.model.Invitacion;
import co.edu.eafit.llevame.model.Notificacion;

public class ServiciosEvento {

	private static ServiciosEvento instancia;

	private ServiciosEvento() {
	}
	
	public static ServiciosEvento getInstancia() {
		if (instancia==null) {
			instancia = new ServiciosEvento();
		}
		return instancia;
	}
	
	public void sendDelete(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpDelete delete = new HttpDelete(url);
		try {
			httpClient.execute(delete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getServerResponse(String url){
		HttpClient httpClient = new DefaultHttpClient();

		HttpGet del = new HttpGet(url);
		del.setHeader("content-type", "application/json");

		try
		{
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			return respStr;
		}

		catch(Exception ex)
		{
			return "error: " + ex;
		}
	}
	
	public Evento[] getArregloEvento(String urlEvento) {
		Evento[] eventos;
		String url = ServerHandler.IP.concat(urlEvento);
		
		try{
			JSONArray losEventos = new JSONArray(getServerResponse(url));
			String eventoString[]= new String[losEventos.length()];
			eventos = new Evento[eventoString.length];
			
			for (int i = 0; i<losEventos.length(); i++) {
				JSONObject evento = losEventos.getJSONObject(i);
				
				if(!evento.getBoolean("esNotificacion")) { //invitacion
					Invitacion oneEvento = new Invitacion();
					oneEvento.setAceptado(evento.getBoolean("aceptado"));
					oneEvento.setIdRef(evento.getInt("idRef"));
					oneEvento.setTipo(evento.getInt("tipo"));
					oneEvento.setId(evento.getInt("id"));
					oneEvento.setMensaje(evento.getString("mensaje"));
					oneEvento.setIdUsuario(evento.getInt("idUsuario"));
					
					if(oneEvento.getTipo()==Invitacion.RUTA){
						oneEvento.setIdRef2(evento.getInt("idRef2"));
					}
					
					eventos[i] = oneEvento;
				}else { //notificacion
					Notificacion oneEvento = new Notificacion();
					oneEvento.setId(evento.getInt("id"));
					oneEvento.setMensaje(evento.getString("mensaje"));
					oneEvento.setIdUsuario(evento.getInt("idUsuario"));
					
					eventos[i] = oneEvento;
				}	
			}
			return eventos;
			
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}

	public HttpPost getServerResponsePost(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpPost post = new HttpPost(url);
		 
		post.setHeader("content-type", "application/json");
		
		return post;
	}
	
	public void ingresarInvitacion(Invitacion invitacion) {
		
		String url = ServerHandler.IP.concat("/eventos");
		HttpPost post = getServerResponsePost(url);
		
		try {
			JSONObject i = new JSONObject();
			i.put("esNotificacion", invitacion.getEsNotificacion());
			i.put("mensaje", invitacion.getMensaje());
			i.put("aceptado", invitacion.isAceptado());
			i.put("idUsuario", invitacion.getIdUsuario());
			i.put("tipo", invitacion.getTipo());
			i.put("idRef", invitacion.getIdRef());
			i.put("idRef2", invitacion.getIdRef2());
			
			StringEntity entity = new StringEntity(i.toString());
			post.setEntity(entity);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse resp = httpClient.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
		} catch (Exception ex){
			Log.e("Error", "e");
		}
	}
	
	public void ingresarNotificacion(Notificacion notificacion) {
			
		String url = ServerHandler.IP.concat("/eventos");
		HttpPost post = getServerResponsePost(url);
		try {
			JSONObject i = new JSONObject();
			i.put("esNotificacion", notificacion.getEsNotificacion());
			i.put("mensaje", notificacion.getMensaje());
			i.put("idUsuario", notificacion.getIdUsuario());
			
			StringEntity entity = new StringEntity(i.toString());
			post.setEntity(entity);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse resp = httpClient.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
		} catch (Exception ex){
			Log.e("Error", "e");
		}
	}
	
	public void borrarEvento(int idEvento){
		String url = ServerHandler.IP.concat("/eventos/"+idEvento);
		sendDelete(url);
	}
}

