package co.edu.eafit.llevame.model;

public class Ruta {
	private int id;
	private String nombre;
	private String fecha;
	private int capacidad;
	private String descripcion;
	
	public Ruta(){
		
	}

	public Ruta(int id, String nombre, String fecha, int capacidad){
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.capacidad = capacidad;
		
		this.descripcion = "";
	}
	
	public Ruta(int id, String nombre, String fecha, int capacidad, String descripcion){
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.capacidad = capacidad;
		this.descripcion = descripcion;
	}
	
	public void Iniciar(){
		
	}
	
	public void Terminar(){
		
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
}