package co.edu.eafit.llevame.model;

public class Ruta {
	private int id;
	private String nombre;
	private String fecha;
	private int capacidad;
	private String descripcion;
	private String placa;
	private boolean estado;
	private Ubicacion[] recorrido;
	
	private int conductor; //id del conductor
	private Usuario[] pasajeros;
	
	public Ruta(){
		
	}

	public Ruta(int id, String nombre, String fecha, int capacidad, String placa){
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.capacidad = capacidad;
		this.setPlaca(placa);
		this.estado = false;
		this.descripcion = "";
	}
	
	public Ruta(int id, String nombre, String fecha, int capacidad, String descripcion, String placa){
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.setPlaca(placa);
		this.capacidad = capacidad;
		this.descripcion = descripcion;
		this.estado = false;
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

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Ubicacion[] getRecorrido() {
		return recorrido;
	}

	public void setRecorrido(Ubicacion[] recorrido) {
		this.recorrido = recorrido;
	}

	public int getConductor() {
		return conductor;
	}

	public void setConductor(int conductor) {
		this.conductor = conductor;
	}

	public Usuario[] getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(Usuario[] pasajeros) {
		this.pasajeros = pasajeros;
	}
	
	public boolean getEstado(){
		return estado;
	}
	
	public void setEstado(boolean estado){
		this.estado = estado;
	}
}