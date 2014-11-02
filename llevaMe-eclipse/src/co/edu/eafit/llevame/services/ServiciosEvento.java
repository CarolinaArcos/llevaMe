package co.edu.eafit.llevame.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
	
	public static ServiciosEvento obtenerInstancia() {
		if (instancia==null) {
			instancia = new ServiciosEvento();
		}
		return instancia;
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
				
				if(!evento.getBoolean("esNotificacion")) {
					Invitacion oneEvento = new Invitacion();
					oneEvento.setAceptado(evento.getBoolean("aceptado"));
					oneEvento.setIdRef(evento.getInt("idRef"));
					oneEvento.setTipo(evento.getInt("enum"));
					oneEvento.setId(evento.getInt("id"));
					oneEvento.setMensaje(evento.getString("mensaje"));
					oneEvento.setIdUsuario(evento.getInt("usuario"));
					
					eventos[i] = oneEvento;
				}else {
					Notificacion oneEvento = new Notificacion();
					oneEvento.setId(evento.getInt("id"));
					oneEvento.setMensaje(evento.getString("mensaje"));
					oneEvento.setIdUsuario(evento.getInt("usuario"));
					
					eventos[i] = oneEvento;
				}	
			}
			return eventos;
			
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}
}

