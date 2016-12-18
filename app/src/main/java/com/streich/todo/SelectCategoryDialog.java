package com.streich.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.antonimuller.fhflapp.R;

import java.util.ArrayList;

import static android.R.id.list;
import static com.streich.todo.TodoListHolder.getMe;

/**
 * Created by basti on 29.11.2016.
 */

public class SelectCategoryDialog extends DialogFragment  implements DialogInterface.OnClickListener{

    public String SelectedCategory;
    ArrayList<String> x;

    private SelectionListener selectionListener;
    private boolean allButtonVisibile=false;
    public void setSelectionListener(SelectionListener l){
        selectionListener=l;
    }

    public void setAllVisibility(boolean t){
        allButtonVisibile=t;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        x = TodoListHolder.getMe().getCategorys();

        CharSequence[] cs = x.toArray(new CharSequence[x.size()]);
        builder.setTitle("Kategorie Auswählen")

                .setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedCategory = x.get(which);
                        Log.v("Todo-SelectCategory","Selected : "+ SelectedCategory);
                        if(selectionListener!=null){
                            selectionListener.OnSelectionChanged(SelectedCategory,which);
                        }
                    }
                });
        if(allButtonVisibile){builder.setNegativeButton("Alle",this);}
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(selectionListener!=null){
            selectionListener.OnSelectionChanged("",-1);
        }
    }

    public interface SelectionListener{
        public void OnSelectionChanged(String Category, int Index);
    }
}
