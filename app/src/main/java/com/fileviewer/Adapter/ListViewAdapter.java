package com.fileviewer.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.antonimuller.fhflapp.R;
import com.fileviewer.Model.ListingsModel;
import com.fileviewer.Util;
import com.streich.todo.TodoModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Adapter to display Files in current Directiry.
 */
public class ListViewAdapter extends ArrayAdapter<File> {
    private static final String TAG = "FHFLAPP: ListViewAdapter";

    public ListViewAdapter(Context context, int resource) {
        super(context, resource);
    }
    public ListViewAdapter(Context context, int resource, List<File> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.filev_entity, null);
        }
        File p = getItem(position);

        if(p != null) {
            TextView fileName = (TextView) view.findViewById(R.id.file_name);
            TextView fileMeta = (TextView) view.findViewById(R.id.file_meta);
            TextView folderElements = (TextView) view.findViewById(R.id.file_folderelements);
            ImageView fileIcon = (ImageView) view.findViewById(R.id.file_icon);

            /*if(p.getName().length() == 0)
                fileName.setText(R.string.fileviewer_homedirectory);
            else
                fileName.setText(p.getName());


            fileMeta.setText(new SimpleDateFormat("MM/dd/yyyy").format(new Date(p.lastModified())));

            fileIcon.setImageDrawable(Util.getFileTypeIcon(getContext(), p));

            if(p.isDirectory() && p.list() != null) {
                String eleString = getContext().getResources().getString(R.string.fileviewer_folderfiles);
                folderElements.setText(p.list().length + " " + eleString);
            }*/
        }

        return view;
    }
}
