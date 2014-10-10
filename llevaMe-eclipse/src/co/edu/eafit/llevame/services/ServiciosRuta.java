package co.edu.eafit.llevame.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;
import co.edu.eafit.llevame.handlers.ServerHandler;
import co.edu.eafit.llevame.model.Ruta;

public class ServiciosRuta {

	private static ServiciosRuta instancia;

	private ServiciosRuta() {

	}

	public static ServiciosRuta obtenerInstancia() {
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

		} catch(Exception ex) {
			Log.e("ServicioRest","Error!", ex);
		}

		return ruta;



	}

}