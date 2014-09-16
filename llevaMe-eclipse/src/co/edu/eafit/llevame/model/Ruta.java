package co.edu.eafit.llevame.model;

import java.util.ArrayList;

import co.edu.eafit.llevame.database.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Ruta {
	
	//nombre de la ruta
	private String name;
	
	//fecha en que ocurre la ruta (TODO: definir el tipo de dato)
	private String date;
	
	//hora en que ocurre la ruta (TODO: verificar si en fecha se incluye la hora)
	private String hour;
	
	//capacidad maxima de la ruta
	private int capacity;
	
	//placa del vehiculo
	private String placa;
	
	//descripcion de la ruta
	private String descripcion;
	
	//ruta como tal a llevar a cabo
	private ArrayList<Ubicacion> ruta;
	
	public Ruta(String name, String date, String hour, int capacity,
			String placa, String descripcion) {
		
		this.name = name;
		this.date = date;
		this.hour = hour;
		this.capacity = capacity;
		this.placa = placa;
		this.descripcion = descripcion;
	}




	public void insertarEnDB(Context context) {
		
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, this.name);
		values.put(DatabaseHandler.KEY_DATE, this.date);
		values.put(DatabaseHandler.KEY_HOUR, this.hour);
		values.put(DatabaseHandler.KEY_CAPACITY, this.capacity);
		values.put(DatabaseHandler.KEY_NUMBERPLATE, this.placa);
		values.put(DatabaseHandler.KEY_DETAILS, this.descripcion);
		
		//TODO: put mapa
		// TODO: put puntos mapa
		
		
		db.insert(DatabaseHandler.TABLE_ROUTES, null, values);
		
		
	}
	
}
