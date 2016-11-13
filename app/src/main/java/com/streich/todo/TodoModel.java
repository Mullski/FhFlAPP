package com.streich.todo;

import android.text.format.Time;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Sebastian Streich and Antoni Müller on 25.10.2016.
 */

public class TodoModel {
    public String title;
    public boolean done;

    public TodoModel(){
        //Leer Initalisieren.
        title="";
        done=false;
    }
    public TodoModel(String title){
        //Leer Initalisieren.
        this.title=title;
        done=false;
    }

    @Override
    public String toString(){
        return title;
    }
}
