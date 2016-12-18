package com.streich.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.antonimuller.fhflapp.R;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by basti on 30.11.2016.
 */

public class CreateTodoDialog extends DialogFragment  implements DialogInterface.OnClickListener, View.OnClickListener, SelectCategoryDialog.SelectionListener{

    View thisView;
    Button categoryButton;
    SelectCategoryDialog catSel;

    String selectedCategory;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        thisView = inflater.inflate(R.layout.todo_newtodo_dialogue, null);

        categoryButton =(Button) thisView.findViewById(R.id.todo_createTodoDialogue_selectCategory);
        categoryButton.setOnClickListener(this);

        catSel = new SelectCategoryDialog();
        catSel.setAllVisibility(false);
        catSel.setSelectionListener(this);

        selectedCategory = "Default";

        builder.setView(thisView)
                .setMessage("Neue Todo erstellen")
                .setPositiveButton("Speichern", this)
                .setNegativeButton("Abbruch", this);
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void onClick(DialogInterface dialog, int which) {
        Log.d("Todo-Dialogue","no "+which);
        if(which == -1){
            //Pressed Yes
            EditText input = (EditText) thisView.findViewById(R.id.todo_createTodoDialogue_todoInput);
            String t = input.getText().toString();

            if(t.length() >0){
                TodoModel x = new TodoModel(t,selectedCategory);
                if(todoListener != null){
                    todoListener.OnTodoCreated(x);
                }
            }

        } else {
            //Pressed no

        }
    }

    @Override
    public void onClick(View v) {
            catSel.show(getFragmentManager(),"");
    }

    @Override
    public void OnSelectionChanged(String Category, int Index) {
        selectedCategory = Category;
        categoryButton.setText(Category);
    }

    private CreateTodoListener todoListener;
    public void setCreateTodoListener(CreateTodoListener l){ todoListener =l;}
    public interface CreateTodoListener{
        public void OnTodoCreated(TodoModel t);
    }


}
