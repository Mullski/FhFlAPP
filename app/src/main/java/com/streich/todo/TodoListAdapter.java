package com.streich.todo;

import android.content.Context;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.antonimuller.fhflapp.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Sebastian Streich on 25.10.2016.
 * This Class is Providing the Connection Between a ListView and a List of Todos provided by the Fragment
 */

public class TodoListAdapter extends ArrayAdapter<TodoModel>{

    public TodoListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TodoListAdapter(Context context, int resource, List<TodoModel> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the View for a Row
        View v = convertView;


        if (v == null) {
            //Inflate the Layout
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.todo_row, null);

        }

        //Get the Asscociated Model
        TodoModel p = getItem(position);

        //Populate the UI with Content.
        if(p != null){
            // Fill the TextView
            TextView t = (TextView) v.findViewById(R.id.todo_row_title);
            t.setText(p.title);
            if(p.done){
                //Make the Text Stiked THrough if done.
                t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                t.setPaintFlags(t.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            // Fill the Checkbox.
            ((CheckBox) v.findViewById(R.id.todo_row_checkBox)).setChecked(p.done);



        }

        return v;
    }
}
