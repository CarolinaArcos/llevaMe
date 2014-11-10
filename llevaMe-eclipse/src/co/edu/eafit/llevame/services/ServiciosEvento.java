package co.edu.eafit.llevame.services;

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
	
	public Evento[] getArregloEvento(int id) {
		Evento[] eventos;
		String url = "/usuarios/"+id+"/eventos";
		
		try{
			String serverResp = ServerHandler.getServerResponse(url);
			JSONArray losEventos = new JSONArray(serverResp);
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
						oneEvento.setIdRef3(evento.getInt("idRef3"));
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
	
	public void ingresarInvitacion(Invitacion invitacion) {
		String url = "/eventos";
		
		try {
			JSONObject i = new JSONObject();
			i.put("esNotificacion", invitacion.getEsNotificacion());
			i.put("mensaje", invitacion.getMensaje());
			i.put("aceptado", invitacion.isAceptado());
			i.put("idUsuario", invitacion.getIdUsuario());
			i.put("tipo", invitacion.getTipo());
			i.put("idRef", invitacion.getIdRef());
			i.put("idRef2", invitacion.getIdRef2());
			
			ServerHandler.getServerResponsePost(url, i);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
	}
	
	public void ingresarNotificacion(Notificacion notificacion) {
		String url = "/eventos";
		try {
			JSONObject i = new JSONObject();
			i.put("esNotificacion", notificacion.getEsNotificacion());
			i.put("mensaje", notificacion.getMensaje());
			i.put("idUsuario", notificacion.getIdUsuario());
			
			ServerHandler.getServerResponsePost(url, i);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
	}
	
	public void borrarEvento(int idEvento){
		String url = "/eventos/"+idEvento;
		ServerHandler.sendDelete(url);
	}
}

