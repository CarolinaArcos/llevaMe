package co.edu.eafit.llevame.model;

public class Invitacion extends Evento{
	//PENDIENTE = false		ACEPTADO = true
		private boolean aceptado;
		private Integer tipo;
		private Integer idRef;

		public Invitacion(){
			super();
			esNotificacion = false;
			
		}
		
		public Invitacion(int id, String msj, int idUsr, boolean aceptado, int tipo, int idRef){
			super(id, msj, idUsr, false);
			this.aceptado = aceptado;
			this.setTipo(tipo);
			this.setIdRef(idRef);
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
}
