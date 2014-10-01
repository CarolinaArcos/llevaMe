package co.edu.eafit.llevame.model;

import java.util.ArrayList;

import co.edu.eafit.llevame.database.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Ruta {
	
	//nombre de la ruta
	private String nombre;
	
	//fecha en que ocurre la ruta (TODO: definir el tipo de dato)
	private String fecha;
	
	//hora en que ocurre la ruta (TODO: verificar si en fecha se incluye la hora)
	private String hora;
	
	//capacidad maxima de la ruta
	private int capacidad;
	
	//placa del vehiculo
	private String placa;
	
	//descripcion de la ruta
	private String descripcion;
	
	//ruta como tal a llevar a cabo
	private ArrayList<Ubicacion> ruta;
	
	private int id;
	
	public Ruta(String name, String date, String hour, int capacity,
			String placa, String descripcion) {
		
		this.nombre = name;
		this.fecha = date;
		this.hora = hour;
		this.capacidad = capacity;
		this.placa = placa;
		this.descripcion = descripcion;
	}
	

	public void insertarEnDB(Context context) {
		
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, this.nombre);
		values.put(DatabaseHandler.KEY_DATE, this.fecha);
		values.put(DatabaseHandler.KEY_HOUR, this.hora);
		values.put(DatabaseHandler.KEY_CAPACITY, this.capacidad);
		values.put(DatabaseHandler.KEY_NUMBERPLATE, this.placa);
		values.put(DatabaseHandler.KEY_DETAILS, this.descripcion);
		
		//TODO: put mapa
		// TODO: put puntos mapa
		
		
		db.insert(DatabaseHandler.TABLE_ROUTES, null, values);

	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getHora() {
		return hora;
	}
	
	public String getPlaca() {
		return placa;
	}
}
