package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.handlers.SharedPreferencesHandler;
import co.edu.eafit.llevame.model.Evento;
import co.edu.eafit.llevame.model.Invitacion;
import co.edu.eafit.llevame.model.Notificacion;
import co.edu.eafit.llevame.services.ServiciosEvento;
import co.edu.eafit.llevame.services.ServiciosRuta;
import co.edu.eafit.llevame.services.ServiciosUsuario;

public class ListEvents extends Activity {

    private ListView listEvents;
    private ProgressDialog pDialog;
    private int id;
    private Evento[] eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        
        SharedPreferences settings = getSharedPreferences(SharedPreferencesHandler.PREFS_NAME, 0);
		id = settings.getInt(SharedPreferencesHandler.LOGIN_KEY, -1);

        listEvents = (ListView) findViewById(R.id.listEvents);
        
        new TraerListaNotificaciones().execute();

    }
    
	public void aceptarInvitacion(int pos) {
		 Invitacion inv = (Invitacion) eventos[pos];
		
		 new AceptarInvitacion().execute(inv);
	}
	
	public void rechazarInvitacion(int pos) {
		Invitacion inv = (Invitacion) eventos[pos];
		
		 new RechazarInvitacion().execute(inv);
	}
	
	public class AceptarInvitacion extends AsyncTask<Invitacion, Void, Void> {
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListEvents.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    	
    	@Override
    	protected Void doInBackground(Invitacion... params) {
    		Invitacion inv = params[0];
    		switch (inv.getTipo()){
    			case Invitacion.RUTA: //ruta
    				ServiciosRuta.getInstancia().vincularPasajero(inv.getIdRef2(), inv.getIdRef(),
    						inv.getIdRef3());
    				Log.d("idRef2", inv.getIdRef2()+"");
    				Log.d("idRef", inv.getIdRef()+"");
    				Log.d("idRef3", inv.getIdRef3()+"");
    				
    				String nombreConductor = ServiciosUsuario.getInstancia().getUsuario(inv.getIdUsuario()).getUsername();
    				String nombreRuta = ServiciosRuta.getInstancia().getRuta(inv.getIdRef2()+"").getNombre();
    				
    				//notificar usuario que se acepto
    				Notificacion nR = new Notificacion(-1, 
    						nombreConductor+" acepto tu solicitud para la ruta "+nombreRuta,
    						inv.getIdRef());
    				ServiciosEvento.getInstancia().ingresarNotificacion(nR);

    				
    				break;
    			case Invitacion.USUARIO:
    				//Implementar aceptar invitacion usuario
    				ServiciosUsuario.getInstancia().agregarAmistad(inv.getIdUsuario(), inv.getIdRef());

    				//notificar usuario que se acepto
    				String nombreAmigo = ServiciosUsuario.getInstancia().getUsuario(inv.getIdUsuario()).getUsername();
    				Notificacion nU = new Notificacion(-1, 
    						nombreAmigo+" acepto tu solicitud de amistad",
    						inv.getIdRef());
    				ServiciosEvento.getInstancia().ingresarNotificacion(nU);
    				
    				break;
    			case Invitacion.COMUNIDAD:
    				//TODO: Implementar aceptar invitacion comunidad
    				break;
				default:
					//TODO: ERROR
    		}
    		
    		// eliminar Invitacion
    		ServiciosEvento.getInstancia().borrarEvento(inv.getId());
    		
    		return null;
    	}

    	@Override
    	protected void onPostExecute(final Void v){
    		pDialog.dismiss();
    		
    		runOnUiThread(new Runnable() {
                public void run() {
                	new TraerListaNotificaciones().execute();
                }
            });
    	}
    	
    }
	
	public class RechazarInvitacion extends AsyncTask<Invitacion, Void, Void> {
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListEvents.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    	
    	@Override
    	protected Void doInBackground(Invitacion... params) {
    		Invitacion inv = params[0];
    		
    		switch (inv.getTipo()){
    			case Invitacion.RUTA: //ruta
    	    		// eliminar Invitacion
    				ServiciosEvento.getInstancia().borrarEvento(inv.getId());
    				
    				break;
    			case Invitacion.USUARIO:
    				//TODO: Implementar rechazar invitacion usuario 
    				break;
    			case Invitacion.COMUNIDAD:
    				//TODO: Implementar rechazar invitacion comunidad
    				break;
				default:
					//TODO: ERROR
    		}

    		return null;
    	}

    	@Override
    	protected void onPostExecute(final Void v){
    		pDialog.dismiss();
    		
    		runOnUiThread(new Runnable() {
                public void run() {
                	new TraerListaNotificaciones().execute();
                }
            });
    	}
    	
    }
	
	public class TraerListaNotificaciones extends AsyncTask<Void, Void, Evento[]> {
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListEvents.this);
            pDialog.setMessage("Cargando Eventos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    	
    	public void ingresarElementosLista(Evento[] e){
    		final ListViewItem[] items = new ListViewItem[e.length];
	        for (int i = 0; i < items.length; i++) {
	            if (e[i].getEsNotificacion()) {
	                items[i] = new ListViewItem(e[i].getMensaje(), CustomAdapter.TYPE_NOTIFICATION);
	            } else {
	                items[i] = new ListViewItem(e[i].getMensaje(), CustomAdapter.TYPE_INVITATION);
	            }
	        }

	        CustomAdapter customAdapter = new CustomAdapter(ListEvents.this, R.id.msj, items);
	        listEvents.setAdapter(customAdapter);
    	}
    	
    	public TraerListaNotificaciones(){
    		super();
    	}
    	
    	@Override
    	protected Evento[] doInBackground(Void... params) {
    		return ServiciosEvento.getInstancia().getArregloEvento(id);
    	}

    	@Override
    	protected void onPostExecute(final Evento[] e){
    		pDialog.dismiss();
    		runOnUiThread(new Runnable() {
                public void run() {
                	ingresarElementosLista(e);
                }
            });
    		eventos = e;
    	}	
    }
    
    public class ListViewItem {
        private String text;
        private int type;

        public ListViewItem(String text, int type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public class CustomAdapter extends ArrayAdapter<ListViewItem> {

        public static final int TYPE_INVITATION = 0;
        public static final int TYPE_NOTIFICATION = 1;
        

        private ListViewItem[] objects;

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return objects[position].getType();
        }

        public CustomAdapter(Context context, int resource, ListViewItem[] objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            ListViewItem listViewItem = objects[position];
            int listViewItemType = getItemViewType(position);

            if (convertView == null) {

                if (listViewItemType == TYPE_NOTIFICATION) {//notificacion
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_notification, null);
                    
                    TextView textView = (TextView) convertView.findViewById(R.id.msj);
                    
                    viewHolder = new ViewHolder(textView);
                } else if (listViewItemType == TYPE_INVITATION) {//invitacion
                	convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_invitation, null);
                	
                	TextView textView = (TextView) convertView.findViewById(R.id.msj);
                	
                	Button btnA = (Button) convertView.findViewById(R.id.aceptar);
                	Button btnR = (Button) convertView.findViewById(R.id.rechazar);
                	btnA.setTag(position);
                	btnR.setTag(position);
                    btnA.setOnClickListener(new OnClickListener() {
        				
        				@Override
        				public void onClick(View v) {
        					aceptarInvitacion(Integer.parseInt(v.getTag().toString()));
        				}
        			});
            		btnR.setOnClickListener(new OnClickListener() {
        				
        				@Override
        				public void onClick(View v) {
        					rechazarInvitacion(Integer.parseInt(v.getTag().toString()));
        				}
        			});
                    
                    
                    viewHolder = new ViewHolder(textView, btnA, btnR);
                }
                
                convertView.setTag(viewHolder);
                
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.getText().setText(listViewItem.getText());

            return convertView;
        }
    }

    public class ViewHolder {
        TextView text;
        Button btnA;
        Button btnR;
        
        public ViewHolder(TextView text, Button btnA, Button btnR) {
            this.text = text;
            this.btnA = btnA;
            this.btnR = btnR;
        }
        
        public ViewHolder(TextView text){
        	this.text = text;
        }
        
        public TextView getText() {
            return text;
        }
        public void setText(TextView text) {
            this.text = text;
        }
        
        public Button getBtnA() {
            return btnA;
        }
        public void setBtnA(Button btnA) {
            this.btnA = btnA;
        }
        
        public Button getBtnR() {
            return btnR;
        }
        public void setBtnR(Button btnR) {
            this.btnR = btnR;
        }
    }
    @Override
    public void onBackPressed() {
    	
    }
}