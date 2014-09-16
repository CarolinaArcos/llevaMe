package co.edu.eafit.llevame.model;

import java.util.ArrayList;

public class Ruta {
	
	//nombre de la ruta
	private String name;
	
	//fecha en que ocurre la ruta (TODO: definir el tipo de dato)
	private Object date;
	
	//hora en que ocurre la ruta (TODO: verificar si en fecha se incluye la hora)
	private Object hour;
	
	//capacidad maxima de la ruta
	private int capacity;
	
	//placa del vehiculo
	private String placa;
	
	//ruta como tal a llevar a cabo
	private ArrayList<Ubicacion> ruta;
	
	
}
