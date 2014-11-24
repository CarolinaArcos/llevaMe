package co.edu.eafit.llevame.handlers;

import org.apache.http.HttpResponse;
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

public class ServerHandler {

  public static final String IP = "http://54.164.131.208";
//	public static final String IP = "http://private-986d0-llevameapi.apiary-mock.com";
//  public static final String IP = "http://10.0.50.177:8083";
	
	
	
	public static HttpClient httpClient = new DefaultHttpClient();
	
	/**
	 * Peticion GET al servidor
	 * @param url direccion del recurso, ej: "/rutas"
	 * @return String del JSON que responde el servidor
	 */
	public static String getServerResponse(String url){
		url = IP + url;
		
		Log.d("URL", url);
		
		HttpGet get = new HttpGet(url);
		
		try
		{
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			return respStr;
		} catch(Exception ex)
		{
			Log.e("getServerResponse","Error!", ex);
			return null;
		}
	}
	
	/**
	 * Peticion POST sin json
	 * @param url direccion del recurso, ej: "/rutas"
	 * @return Respuesta del servidor
	 */
	public static String getServerResponsePost(String url) {
		url = IP + url;
		
		HttpPost post = new HttpPost(url);
		 
		post.setHeader("content-type", "application/json");
		
		try {
			HttpResponse resp = httpClient.execute(post);
			return EntityUtils.toString(resp.getEntity());
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}

	/**
	 * Peticion POST con un objeto JSON
	 * @param url direccion del recurso, ej "/rutas"
	 * @param json objeto JSON a enviar en el post
	 * @return String del JSON que responde el servidor
	 */
	public static String getServerResponsePost(String url, JSONObject json) {
		url = IP + url;
		
		HttpPost post = new HttpPost(url);
		 
		post.setHeader("content-type", "application/json");
		
		try {
			StringEntity entity = new StringEntity(json.toString());
			post.setEntity(entity);
			
			HttpResponse resp = httpClient.execute(post);
			return EntityUtils.toString(resp.getEntity());
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}
	
	/**
	 * Peticion POST con un arreglo JSON
	 * @param url direccion del recurso, ej: "/rutas"
	 * @param json Arreglo JSON a enviar
	 * @return Respuesta del servidor
	 */
	public static String getServerResponsePost(String url, JSONArray json) {
		url = IP + url;
		
		HttpPost post = new HttpPost(url);
		 
		post.setHeader("content-type", "application/json");
		
		try {
			StringEntity entity = new StringEntity(json.toString());
			post.setEntity(entity);
			
			HttpResponse resp = httpClient.execute(post);
			return EntityUtils.toString(resp.getEntity());
		} catch (Exception ex) {
			Log.e("ServicioRest","Error!", ex);
			return null;
		}
	}

	public static void sendDelete(String url) {
		url = IP + url;
		
		HttpDelete delete = new HttpDelete(url);
		
		try {
			httpClient.execute(delete);
		} catch(Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}
	}
}
