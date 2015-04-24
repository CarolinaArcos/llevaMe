package co.edu.eafit.llevame.services;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import co.edu.eafit.llevame.handlers.ServerHandler;
import co.edu.eafit.llevame.model.Ruta;
import co.edu.eafit.llevame.model.Ubicacion;

public class ServiciosRuta {

	private static ServiciosRuta instancia;

	private ServiciosRuta() {

	}

	public static ServiciosRuta getInstancia() {
		if (instancia==null) {
			instancia = new ServiciosRuta();
		}
		return instancia;
	}


	
	public Ruta getRuta(String id) {
		Ruta ruta = new Ruta();

		String url = "/rutas/" + id;

		try {
			String serverResp = ServerHandler.getServerResponse(url);
			
			//obtener la ruta que se busco
			JSONObject laRuta = new JSONObject(serverResp);	
			ruta.setId(Integer.parseInt(id));
			ruta.setNombre(laRuta.getString("nombre"));
			ruta.setFecha(laRuta.getString("fecha"));
			ruta.setDescripcion(laRuta.getString("descripcion"));
			ruta.setCapacidad(laRuta.getInt("capacidad"));
			ruta.setPlaca(laRuta.getString("placa"));
			ruta.setConductor(laRuta.getInt("conductor"));
			
			
			ruta.setPasajeros(ServiciosUsuario.getInstancia().getPasajeros(Integer.parseInt(id)));
			
			ruta.setRecorrido(getArregloUbicaciones(Integer.parseInt(id)));

		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}

		return ruta;
	}
	
	public Ruta[] getArregloRutasConductor(int idUsuario){
		return getArregloRutas("/usuarios/"+idUsuario+"/rutas/conductor");
	}
	
	public Ruta[] getArregloRutasPasajero(int idUsuario){
		return getArregloRutas("/usuarios/"+idUsuario+"/rutas/pasajero/");
	}
	
	public Ruta[] getArregloRutas(int idUsuario){
		return getArregloRutas("/usuarios/"+idUsuario+"/rutas");
	}
	
	private Ruta[] getArregloRutas(String url) {
		Ruta[] rutas;
		
		try{
			String serverResp = ServerHandler.getServerResponse(url);
			JSONArray lasRutas = new JSONArray(serverResp);
			String rutasString[]= new String[lasRutas.length()];
			rutas = new Ruta[rutasString.length];
			
			for (int i = 0; i<lasRutas.length(); i++) {
				JSONObject ruta = lasRutas.getJSONObject(i);
				Ruta oneRuta = new Ruta();
				
				oneRuta.setId(ruta.getInt("id"));
				oneRuta.setNombre(ruta.getString("nombre"));
				oneRuta.setFecha(ruta.getString("fecha"));
				oneRuta.setDescripcion(ruta.getString("descripcion"));
				oneRuta.setCapacidad(ruta.getInt("capacidad"));
				oneRuta.setPlaca(ruta.getString("placa"));
				rutas[i] = oneRuta;
				
			}
			return rutas;
			
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}
	
	public void addRuta(Ruta ruta) {
		String url = "/rutas";
		
		try {
			//pasar ruta a JSON
			JSONObject r = new JSONObject();
			r.put("nombre", ruta.getNombre());
			r.put("fecha", ruta.getFecha());
			r.put("capacidad", ruta.getCapacidad());
			r.put("descripcion", ruta.getDescripcion());
			r.put("conductor", ruta.getConductor());
			r.put("placa", ruta.getPlaca());
			
			String serverResp = ServerHandler.getServerResponsePost(url, r);
			
			JSONObject rutaNueva = new JSONObject(serverResp);
			ruta.setId(rutaNueva.getInt("id"));
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
	}
	
	public void dejarRuta(int idRuta, int idUsuario) {
		String url = "/rutas/"+idRuta+"/pasajeros/"+idUsuario;
		ServerHandler.sendDelete(url);
	}
	
	public void iniciarRuta(int idRuta) {
		String url = "/rutas/"+idRuta+"?estado=true";
		ServerHandler.getServerResponsePost(url);
	}
	
	public void finalizarRuta(int idRuta) {
		String url = "/rutas/" + idRuta;
		ServerHandler.sendDelete(url);
	}
	
	public void ingresarRecorrido(Ubicacion[] recorrido, int idRuta){
		String url = "/rutas/"+idRuta+"/ubicaciones";
		
		try {
			//pasar arreglo de ubicaciones a json
			JSONArray listaUbicaciones = new JSONArray();
			
			for(Ubicacion u : recorrido){
				JSONObject jsonU = new JSONObject();
				jsonU.put("nombre", u.getNombre());
				jsonU.put("longitud", u.getLongitud());
				jsonU.put("latitud", u.getLatitud());
				
				listaUbicaciones.put(jsonU);
			}
			
//			Log.d("recorrido", listaUbicaciones.toString());
			
			ServerHandler.getServerResponsePost(url, listaUbicaciones);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
	}
	
	public Ubicacion[] getArregloUbicaciones(int idRuta){
		Ubicacion[] recorrido;
		String url = "/rutas/"+idRuta+"/ubicaciones";
		
		try{
			String serverResp = ServerHandler.getServerResponse(url);
			
			Log.d("getArregloUbicaciones", serverResp);
			
			JSONArray lasUbicaciones = new JSONArray(serverResp);
			String recorridoString[]= new String[lasUbicaciones.length()];
			recorrido = new Ubicacion[recorridoString.length];
			
			for (int i = 0; i<lasUbicaciones.length(); i++) {
				JSONObject u = lasUbicaciones.getJSONObject(i);
				Ubicacion oneUbicacion = new Ubicacion();
				
				oneUbicacion.setId(u.getInt("id"));
				oneUbicacion.setNombre(u.getString("nombre"));
				oneUbicacion.setLongitud(u.getDouble("longitud"));
				oneUbicacion.setLatitud(u.getDouble("latitud"));
				
				recorrido[i] = oneUbicacion;
				
			}
			return recorrido;
			
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}
	
	public void vincularPasajero(int idRuta, int idPasajero, int idUbicacion) {
		
		String url = "/rutas/"+idRuta+"/pasajeros/"+idPasajero+"?ubicacion="+idUbicacion;
		
		ServerHandler.getServerResponsePost(url);
	}
}