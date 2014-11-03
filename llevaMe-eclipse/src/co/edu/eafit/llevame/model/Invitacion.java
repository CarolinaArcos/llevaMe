package co.edu.eafit.llevame.model;

public class Invitacion extends Evento{
	public static final int RUTA = 0;
	public static final int USUARIO = 1;
	public static final int COMUNIDAD = 2;
	
	
	//PENDIENTE = false		ACEPTADO = true
	private boolean aceptado;
	private Integer tipo;
	private Integer idRef;//id del usuario que envia la invitacion
	private Integer idRef2;//solo para tipo ruta, el id de la ruta involucrada

	public Invitacion(){
		super();
		esNotificacion = false;
		
	}
	
	public Invitacion(int id, String msj, int idUsr, boolean aceptado, int tipo, int idRef){
		super(id, msj, idUsr, false);
		this.aceptado = aceptado;
		this.tipo = tipo;
		this.idRef = idRef;
	}
	
	//Solicitud de cupo para una ruta
	public Invitacion(int id, String msj, int idUsr, boolean aceptado, int tipo,
			int idRef, int idRef2){
		
		super(id, msj, idUsr, false);
		this.aceptado = aceptado;
		this.tipo = tipo;
		this.idRef = idRef;
		this.idRef2 = idRef2;
	}
	
	public boolean isAceptado() {
		return aceptado;
	}

	public void setAceptado(boolean aceptado) {
		this.aceptado = aceptado;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getIdRef() {
		return idRef;
	}

	public void setIdRef(Integer idRef) {
		this.idRef = idRef;
	}

	public Integer getIdRef2() {
		return idRef2;
	}

	public void setIdRef2(Integer idRef2) {
		this.idRef2 = idRef2;
	}
}
