package co.edu.eafit.llevame.model;

public class Usuario {
	private int id;
	private String username;
	private String password;
	private int puntos;
	
	private Integer pickUp;
	
	public Usuario(){
		
	}
	
	public Usuario(String username, String password){
		this.username = username;
		this.password = password;
		puntos = 0;
	}
	
	public Usuario(int id, String username, String password, int puntos){
		this.id = id;
		this.username = username;
		this.password = password;
		this.puntos = puntos;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getPickUp() {
		return pickUp;
	}

	public void setPickUp(Integer pickUp) {
		this.pickUp = pickUp;
	}
}
