package com.streich.todo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.example.antonimuller.fhflapp.R;

import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * Created by Sebastian Streich  on 25.10.2016.
 */

public class TodoFragment extends Fragment implements
        View.OnClickListener, AdapterView.OnItemClickListener,
        FabSpeedDial.MenuListener, CategoryDialog.CategoryListner,
        SelectCategoryDialog.SelectionListener
{
    final static String key ="Todo-Fragment";


    TodoListHolder  model;
    TodoListAdapter listAdapter;
    //ViewElements
    View            fragmentView;
    ListView        todoList;
    Button selectorButton;
    CategoryDialog  categoryDialog;
    SelectCategoryDialog    selectCategoryDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.todo_fragment, container, false);

        //Fetch the UI Components needed.
        todoList                    = (ListView)     fragmentView.findViewById(R.id.listV);
        selectorButton              = (Button)       fragmentView.findViewById(R.id.todo_selection_button);
        FabSpeedDial fabSpeedDial   = (FabSpeedDial) fragmentView.findViewById(R.id.todo_appButton);

        //Initalise the Dialogues
        categoryDialog          = new CategoryDialog();
        selectCategoryDialog    = new SelectCategoryDialog();

        selectCategoryDialog.setAllVisibility(true);
        //Fetch the Model
        model = TodoListHolder.getMe();
        model.fetch(getActivity());


        //Connect the Model to the UI
        listAdapter = new TodoListAdapter(getActivity(), R.layout.todo_row, model);
        todoList.setAdapter(listAdapter);

        //Set EventListners
        todoList.setOnItemClickListener(this);
        selectorButton.setOnClickListener(this);
        categoryDialog.setCategoryListner(this);
        fabSpeedDial.setMenuListener(this);
        selectCategoryDialog.setSelectionListener(this);
        return fragmentView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(key,"Clicked on an Item");
        TodoModel clickedItem = listAdapter.getItem(position);
        clickedItem.done= !clickedItem.done;
        listAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onMenuItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.todo_new_category:
                categoryDialog.show(getFragmentManager(),"");
                break;
            case R.id.todo_new_todo:
                Log.v(key,"New Todo");
                //Prepare a Add an TODO Dialog.

                break;
        }
        return false;
    }

    public void onMenuClosed(){}


    @Override
    public void onPause(){
        Log.v(key,"Pausing the Fragment");
        super.onPause();
        //Pausing the Activity.
        Context con = getActivity();
        model.commmit(con);

    }
    @Override
    public void onNewCategory(String Category) {
        Log.v("Todo-Fragment","New Category Requested:" + Category);
        model.getCategorys().add(Category);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.todo_selection_button){selectCategoryDialog.show(getFragmentManager(),"");}
    }


    @Override
    public boolean onPrepareMenu(NavigationMenu navigationMenu) {
        return true;
    }

    @Override
    public void OnSelectionChanged(String Category, int Index) {
        if(Index <0){
            selectorButton.setText("Alle");
        }else{
            selectorButton.setText(Category);
        }

    }
}

/*

Context con = getActivity();
                model.commmit(con);




                //Add a New item to the List.
                String input = title_input.getText().toString();

                if(input.length()>0){
                    Log.v(key,"Adding new Todo");
                    model.add(new TodoModel(title_input.getText().toString()));

                    title_input.setText("");
                    listAdapter.notifyDataSetChanged();
                }
 */