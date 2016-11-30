package com.streich.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.antonimuller.fhflapp.R;

/**
 * Created by basti on 29.11.2016.
 */

public class CreateCategoryDialog extends DialogFragment  implements DialogInterface.OnClickListener{

    View thisView;
    private CategoryListner categoryListner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        thisView = inflater.inflate(R.layout.todo_new_category, null);
        builder.setView(thisView)
                .setMessage("Neue Kategorie erstellen")
                .setPositiveButton("Speichern", this)
                .setNegativeButton("Abbruch", this);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d("Todo-Dialogue","no "+which);
        if(which == -1){
            //Pressed Yes
            EditText input = (EditText) thisView.findViewById(R.id.todo_editText_newCategory);
            String t = input.getText().toString();
            Log.v("Todo-CreateCategoryDialog","New Category "+t);
            if(t.length() >0 && categoryListner != null){
                categoryListner.onNewCategory(t);
            }
        } else {
            //Pressed no

        }
    }

    public void setCategoryListner(CategoryListner i){ categoryListner = i;}
    public interface CategoryListner{
        public void onNewCategory(String Category);
    }
}
