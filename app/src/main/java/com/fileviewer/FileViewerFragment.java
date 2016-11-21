package com.fileviewer;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonimuller.fhflapp.R;
import com.fileviewer.Adapter.ListViewAdapter;
import com.fileviewer.Model.ListingsModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Donny on 17.11.2016.
 */

public class FileViewerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener , ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "FHFLAPP: FileViewerFragment";

    private static final String CURRENT_DIR_DIR = "current-dir";

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private View fragmentView;
    public File currentDir;
    private ListingsModel files;
    private ListView fileList;
    private ListViewAdapter x;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Allowed");
        }
        else {
            Toast toast = Toast.makeText(getActivity(), R.string.fileviewer_missingpermissions, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        }


        fragmentView = inflater.inflate(R.layout.filev_fragment, container, false);
        fileList = (ListView) fragmentView.findViewById(R.id.listFiles);

        files = new ListingsModel(new ArrayList<File>());
        x = new ListViewAdapter(getActivity(), R.layout.filev_entity, files.children);

        fileList.setAdapter(x);
        fileList.setOnItemClickListener(this);
        fileList.setOnItemLongClickListener(this);

        initDir(savedInstanceState);

        return fragmentView;
    }

    public void initDir(Bundle savedInstanceState) {
        if (savedInstanceState!=null && savedInstanceState.getSerializable(CURRENT_DIR_DIR) != null) {
            currentDir = new File(savedInstanceState
                    .getSerializable(CURRENT_DIR_DIR).toString());
        }
        else
        {
            currentDir = new File("/");
        }
        updateDir(currentDir);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(CURRENT_DIR_DIR, currentDir.getAbsolutePath());
    }

    private void initCurrentDirAndChilren() {
        String[] children = currentDir.list();
        files.children.clear();

        for(String fileName : children) {
            File child = new File(getPathInfo(fileName));
            files.children.add(child);
        }
        Collections.sort(files.children, new Comparator<File>() {
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

        if(currentDir.getParent() != null) {
            files.children.add(0, currentDir.getParentFile());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "Clicked on an Item");

        File clickedItem = x.getItem(i);
        if(!clickedItem.canRead() && !clickedItem.canWrite()) {
            Toast toast = Toast.makeText(getActivity(), R.string.fileviewer_missingpermissions, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(clickedItem.isDirectory()){
            updateDir(clickedItem);
        }
        else {
            openFile(clickedItem);
        }
    }

    public void onPause() {
        Log.v(TAG, "Pausing the Fragment");
        super.onPause();
    }

    public void updateDir(File newDir){
        if(newDir.isDirectory() && newDir.canRead()) {
            String filePath = newDir.getAbsolutePath();
            currentDir = new File(filePath);
            initCurrentDirAndChilren();

            fileList.setSelectionAfterHeaderView();
            fileList.setSelection(0);
        }
        x.notifyDataSetChanged();
    }

    public String getPathInfo(String fileName) {
        if(fileName == File.separator)
            return fileName;

        return currentDir.getAbsolutePath() + File.separator + fileName;
    }

    private void openFile(File file) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
        intent.setDataAndType(uri, type == null ? "*/*" : type);
        startActivity((Intent.createChooser(intent,
                getString(R.string.fileviewer_openfileusing))));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "Clicked on an Item Long");

        return true;
    }
}
