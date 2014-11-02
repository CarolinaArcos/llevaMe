package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Evento;
import co.edu.eafit.llevame.services.ServiciosEvento;

public class ListEvents extends Activity {

    private ListView listEvents;
    private ProgressDialog pDialog;
    private int id = 1; //QUEMADO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        listEvents = (ListView) findViewById(R.id.listEvents);

//        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getBaseContext(), items[i].getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
        
        listEvents.setOnItemClickListener(new OnItemClickListener() {
        	
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
            	Log.d("Id", ""+id);
            	Log.d("Posicion", ""+posicion);
            }
            
         });
        
        new TraerListaNotificaciones().execute();

    }
    
	public void onAceptar(View view) {
		Log.d("boton", "aceptado");
		Log.d("Id view", ""+view.getId());
	}
	
	public void onRechazar(View view) {
		Log.d("boton", "rechazado");
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
    		return ServiciosEvento.obtenerInstancia().getArregloEvento("/usuarios/"+id+"/eventos");
    	}

    	@Override
    	protected void onPostExecute(final Evento[] e){
    		pDialog.dismiss();
    		runOnUiThread(new Runnable() {
                public void run() {
                	ingresarElementosLista(e);
                }
            });

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

                if (listViewItemType == TYPE_NOTIFICATION) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_notification, null);                 
                } else if (listViewItemType == TYPE_INVITATION) {
                	convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_invitation, null);
                    
                }
                
                TextView textView = (TextView) convertView.findViewById(R.id.msj);
                viewHolder = new ViewHolder(textView);

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
        
        public ViewHolder(TextView text) {
            this.text = text;
        }
        public TextView getText() {
            return text;
        }
        public void setText(TextView text) {
            this.text = text;
        }
    }
}