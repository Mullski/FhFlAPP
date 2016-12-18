package com.streich.todo;

import android.text.format.Time;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Sebastian Streich  on 25.10.2016.
 */

public class TodoModel {
    public String title;
    public String category;
    public boolean done;

    public TodoModel(){
        //Leer Initalisieren.
        title="";
        category="Default";
        done=false;
    }
    public TodoModel(String title){
        this.title=title;
        category="Default";
        done=false;
    }
    public TodoModel(String title,String category){
        //Leer Initalisieren.
        this.title=title;
        done=false;
        this.category= category;
    }


}
