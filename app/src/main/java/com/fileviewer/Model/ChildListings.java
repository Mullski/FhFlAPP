package com.fileviewer.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Donny on 18.12.2016.
 */
public class ChildListings extends ArrayList<File> {
    private static ChildListings ourInstance = new ChildListings();

    public static ChildListings getInstance() {
        return ourInstance;
    }

    private ChildListings() {
        super();
    }

    /**
     * Sorts List to make folder appear first
     */
    public void sort() {
        Collections.sort(this, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if(!file2.isFile() && file1.isFile()) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
    }
}
