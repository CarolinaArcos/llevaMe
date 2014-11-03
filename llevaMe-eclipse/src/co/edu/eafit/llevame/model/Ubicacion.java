package co.edu.eafit.llevame.model;

public class Ubicacion {
	private int id;
	private String titulo;
	private double latitud;
	private double longitud;
	
	public Ubicacion() {
		
	}

	public Ubicacion(int id, String titulo, double latitud, double longitud){
		this.id = id;
		this.titulo = titulo;
		this.longitud = longitud;
		this.latitud = latitud;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
}