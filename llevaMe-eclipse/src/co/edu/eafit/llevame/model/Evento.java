package co.edu.eafit.llevame.model;

public class Evento {
	private String mensaje;
	private int id;
	private int idUsuario;
	protected boolean esNotificacion;

	public boolean getEsNotificacion() {
		return esNotificacion;
	}

	public void setEsNotificacion(boolean esNotificacion) {
		this.esNotificacion = esNotificacion;
	}

	public Evento(){
		
	}
	
	public Evento(int id, String msj, int idUsr, boolean esNotificacion){
		this.id = id;
		mensaje = msj;
		idUsuario = idUsr;
		this.esNotificacion= esNotificacion;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
}
