package com.fileviewer.Model;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Donny on 18.11.2016.
 */

public class ListingsModel {
    public ArrayList<File> children;
    private boolean isExcludeFromMedia = false;

    public ListingsModel(ArrayList<File> children) {
        super();
        this.children = children;
    }

    public ArrayList<File> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<File> children) {
        this.children = children;
    }
}
