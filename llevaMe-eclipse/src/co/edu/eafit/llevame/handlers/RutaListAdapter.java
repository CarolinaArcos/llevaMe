package co.edu.eafit.llevame.handlers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import co.edu.eafit.llevame.R;
import co.edu.eafit.llevame.model.Ruta;

public class RutaListAdapter extends ArrayAdapter<Ruta>{
	
	Ruta[] rutas;
	Context context;
	
	public RutaListAdapter(Context context, int layoutResourceId, Ruta[] rutas) {
		super(context, layoutResourceId, rutas);
		this.rutas = rutas;
		this.context = context;
	}

	@Override
	public int getCount() {
		return rutas.length;
	}

	@Override
	public Ruta getItem(int position) {
		return rutas[position];
	}

	@Override
	public long getItemId(int position) {
		return rutas[position].getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.elemento_lista_rutas, parent,false);
		}
		
		TextView title = (TextView)convertView.findViewById(R.id.tituloRuta);
		TextView dia = (TextView)convertView.findViewById(R.id.diaRuta);
		TextView hora = (TextView)convertView.findViewById(R.id.hora);
		TextView capacidad = (TextView)convertView.findViewById(R.id.capacidad);
		
		Ruta r = rutas[position];
		
		title.setText(r.getNombre());
		dia.setText(r.getFecha().substring(0, 10));
		hora.setText(r.getFecha().substring(11));
		capacidad.setText(""+ r.getCapacidad());
		 
		return convertView;
	}
	
	

}
