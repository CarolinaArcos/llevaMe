package co.edu.eafit.llevame.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import co.edu.eafit.llevame.R;

public class ListEvents extends Activity {

    private ListView listEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_events);

        listEvents = (ListView) findViewById(R.id.listEvents);

        final ListViewItem[] items = new ListViewItem[8];

        for (int i = 0; i < items.length; i++) {
            if (i % 2 == 0) {
                items[i] = new ListViewItem("#Usuario ha aceptado tu solicitud para la #ruta #Inicio - #Fin", CustomAdapter.TYPE_NOTIFICATION);
            } else {
                items[i] = new ListViewItem("#Usuario te ha agregado como amigo", CustomAdapter.TYPE_INVITATION);
            }
        }

        CustomAdapter customAdapter = new CustomAdapter(this, R.id.msj, items);
        listEvents.setAdapter(customAdapter);
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), items[i].getText(), Toast.LENGTH_SHORT).show();
            }
        });

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