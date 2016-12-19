package de.rp_byte.neroazure.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.antonimuller.fhflapp.R;

import java.util.List;

/**
 * Adapter für die Listview in ChatFragment
 * Created by neroazure
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.message, null);
        }

        Message m = getItem(position);

        if(m != null){
            TextView n = (TextView) v.findViewById(R.id.name);
            n.setText(m.sender+" : ");

            TextView me = (TextView) v.findViewById(R.id.message);
            me.setText(m.message);
        }

        return v;
    }
}
