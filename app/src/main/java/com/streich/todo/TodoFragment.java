package com.streich.todo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.antonimuller.fhflapp.R;

import java.util.ArrayList;

/**
 * Created by Sebastian Streich and Antoni Müller on 25.10.2016.
 */

public class TodoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    final static String key ="Todo-Fragment";


    TodoListHolder model;
    TodoListAdapter x;
    //ViewElements
    View fragmentView;
    ListView todoList;
    TextView title_input;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.todo_fragment, container, false);

        //Fetch the UI Components needed.
        todoList = (ListView) fragmentView.findViewById(R.id.listV);
        title_input = (TextView) fragmentView.findViewById(R.id.todo_title_input);

        //Fetch the Model
        model = new TodoListHolder();
        //Loading stuff out of the LocalStorage.
        model.fetch(getActivity());


        //Connect the Model to the UI
        x = new TodoListAdapter(getActivity(), R.layout.todo_row, model);
        todoList.setAdapter(x);

        //Set EventListners
        fragmentView.findViewById(R.id.AddButton).setOnClickListener(this);
        todoList.setOnItemClickListener(this);


        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.AddButton:
                //Add a New item to the List.
                String input = title_input.getText().toString();

                if(input.length()>0){
                    Log.v(key,"Adding new Todo");
                    model.add(new TodoModel(title_input.getText().toString()));

                    title_input.setText("");
                    x.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Some Click on an item.
        Log.v(key,"Clicked on an Item");

        TodoModel clickedItem = x.getItem(position);
        clickedItem.done= !clickedItem.done;

        x.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        Log.v(key,"Pausing the Fragment");
        super.onPause();
        //Pausing the Activity.

        Context con = getActivity();
        model.commmit(con);

    }

}

/*

Context con = getActivity();
                model.commmit(con);
 */