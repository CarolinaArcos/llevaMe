package co.edu.eafit.llevame.model;

public class Notificacion extends Evento{
	
	public Notificacion() {
		super();
		esNotificacion = true;
	}
	
	public Notificacion(int id, String msj, int idUsr){
		super(id,msj,idUsr, true);
	}
}
