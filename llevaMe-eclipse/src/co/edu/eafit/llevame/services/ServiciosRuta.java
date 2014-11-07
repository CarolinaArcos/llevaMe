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

	public HttpPost getServerResponsePost(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpPost post = new HttpPost(url);
		 
		post.setHeader("content-type", "application/json");
		
		return post;
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

	public HttpDelete getServerResponseDelete(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpDelete delete = new HttpDelete(url);
		
		return delete;
	}
	
	public Ruta getRuta(String id) {
		Ruta ruta = new Ruta();

		String url = ServerHandler.IP.concat("/rutas/").concat(id);

		try {
			JSONObject laRuta = new JSONObject(getServerResponse(url));	
			ruta.setId(Integer.parseInt(id));
			ruta.setNombre(laRuta.getString("nombre"));
			ruta.setFecha(laRuta.getString("fecha"));
			ruta.setDescripcion(laRuta.getString("descripcion"));
			ruta.setCapacidad(laRuta.getInt("capacidad"));
			ruta.setPlaca(laRuta.getString("placa"));

		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}

		return ruta;
	}
	
	public Ruta[] getArregloRutas(String urlRuta) {
		
		Ruta[] rutas;
		String url = ServerHandler.IP.concat(urlRuta);
		
		try{
			JSONArray lasRutas = new JSONArray(getServerResponse(url));
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
		
		String url = ServerHandler.IP.concat("/rutas");
		HttpPost post = getServerResponsePost(url);
		try {
			JSONObject r = new JSONObject();
			r.put("nombre", ruta.getNombre());
			r.put("fecha", ruta.getFecha());
			r.put("capacidad", ruta.getCapacidad());
			r.put("descripcion", ruta.getDescripcion());
			r.put("conductor", 1);
			r.put("placa", ruta.getPlaca());
			
			StringEntity entity = new StringEntity(r.toString());
			post.setEntity(entity);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse resp = httpClient.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONObject rutaNueva = new JSONObject(respStr);
			ruta.setId(rutaNueva.getInt("id"));
		} catch (Exception ex){
			Log.e("Error", "e");
		}
	}
	
	public void dejarRuta(String urlRuta) {
		
		String url = ServerHandler.IP.concat("/rutas/").concat(urlRuta);
		sendDelete(url);
	}
	
	public void iniciarRuta(String urlRuta) {
		String url = ServerHandler.IP.concat("/rutas/").concat(urlRuta);
		HttpPost post = getServerResponsePost(url);
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			httpClient.execute(post);
		} catch (Exception ex){
			Log.e("Error", "e");
		}	
	}
	
	public void finalizarRuta(String urlRuta) {
		String url = ServerHandler.IP.concat("/rutas/").concat(urlRuta);
		HttpDelete delete = getServerResponseDelete(url);
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			httpClient.execute(delete);
		} catch (Exception ex){
			Log.e("Error", "e");
		}
		
	}
	
	public void ingresarRecorrido(Ubicacion[] recorrido, int idRuta){
		String url = ServerHandler.IP.concat("/rutas/"+idRuta+"/ubicaciones");
		HttpPost post = getServerResponsePost(url);
		try {
			JSONArray listaUbicaciones = new JSONArray();
			
			for(Ubicacion u : recorrido){
				JSONObject jsonU = new JSONObject();
				jsonU.put("nombre", u.getNombre());
				jsonU.put("longitud", u.getLongitud());
				jsonU.put("latitud", u.getLatitud());
				
				listaUbicaciones.put(jsonU);
			}
			
			Log.d("recorrido", listaUbicaciones.toString());
			
			StringEntity entity = new StringEntity(listaUbicaciones.toString());
			post.setEntity(entity);
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse resp = httpClient.execute(post);
//			String respStr = EntityUtils.toString(resp.getEntity());
		} catch (Exception ex){
			Log.e("Error", "e");
		}
	}
	
	public void vincularPasajero(int idRuta, int idPasajero) {
		
		String url = ServerHandler.IP.concat("/rutas/pasajeros?ruta="+idRuta+"&usuario="+idPasajero);
		HttpPost post = getServerResponsePost(url);
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse resp = httpClient.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
		} catch (Exception ex){
			Log.e("Error", "e");
		}
	}
}