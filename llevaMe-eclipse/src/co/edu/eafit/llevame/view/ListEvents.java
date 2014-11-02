package co.edu.eafit.llevame.view;

import android.app.ListActivity;
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

public class ListEvents extends ListActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_events);

        listView = (ListView) findViewById(R.id.listEvents);

        final ListViewItem[] items = new ListViewItem[12];

        for (int i = 0; i < items.length; i++) {
            if (i % 2 == 0) {
                items[i] = new ListViewItem("Notification " + i, CustomAdapter.TYPE_NOTIFICATION);
            } else {
                items[i] = new ListViewItem("Invitation " + i, CustomAdapter.TYPE_INVITATION);
            }
        }

        CustomAdapter customAdapter = new CustomAdapter(this, R.id.msj, items);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), items[i].getMsj(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class ListViewItem {
        private String msj;
        private int type;

        public ListViewItem(String msj, int type) {
            this.msj = msj;
            this.type = type;
        }

        public String getMsj() {
            return msj;
        }

        public void setMsj(String msj) {
            this.msj = msj;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

    public class CustomAdapter extends ArrayAdapter<ListViewItem> {

        public static final int TYPE_NOTIFICATION = 0;
        public static final int TYPE_INVITATION = 1;
        
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
            viewHolder.getMsj().setText(listViewItem.getMsj());
            return convertView;
        }
    }

    public class ViewHolder {
        TextView msj;

        public ViewHolder(TextView msj) {
            this.msj = msj;
        }

        public TextView getMsj() {
            return msj;
        }

        public void setMsj(TextView msj) {
            this.msj = msj;
        }
    }
}