package com.fileviewer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.antonimuller.fhflapp.R;
import com.fileviewer.Adapter.ListViewAdapter;
import com.fileviewer.Model.ChildListings;

import java.io.File;
import java.io.IOException;

import static com.fileviewer.Util.*;

/**
 * Created by Donny on 17.11.2016.
 */
public class FileViewerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "FHFLAPP: FileViewer";

    private static final String CURRENT_DIR_DIR = "current-dir";

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private View fragmentView;

    public File currentDir;
    public File copyFile = null;
    private ChildListings files;
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
        Log.v(TAG, "onCreateView():");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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

        files = ChildListings.getInstance();
        x = new ListViewAdapter(getActivity(), R.layout.filev_entity, files);

        fileList.setAdapter(x);
        fileList.setOnItemClickListener(this);

        initDir(savedInstanceState);

        registerForContextMenu(fileList);
        return fragmentView;
    }

    /**
     * When Fragment gets created. It sets the default path
     * @param savedInstanceState
     */
    public void initDir(Bundle savedInstanceState) {
        Log.v(TAG, "initDir():");
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.v(TAG, "onCreateContextMenu():");
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.fileviewer_context_menu, menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v(TAG, "onCreateOptionsMenu():");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fileviewer_menu, menu);
    }

    /**
     * Override onOptionsItemSelected.
     * Gets fired when an Item is selected from Menu
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected():");
        switch (item.getItemId()) {
            case R.id.action_createfile:
                Log.d(TAG, "action_createfile():");
                if(!checkWritePermissions(currentDir)) {
                    return true;
                }

                final EditText editTextName = new EditText(getActivity());
                createBuilder(R.string.fileviewer_filename, editTextName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = editTextName.getText().toString();
                        try {
                            File newFile = new File(getPathInfo(currentDir, inputText));
                            Log.d(TAG, "NewFilePath " + getPathInfo(currentDir, inputText));
                            newFile.createNewFile();
                            initCurrentDirAndChilren();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                return true;
            case R.id.action_createfolder:
                Log.d(TAG, "action_createfolder():");

                if(!checkWritePermissions(currentDir)) {
                    return true;
                }

                final EditText editTextFolderName = new EditText(getActivity());
                createBuilder(R.string.fileviewer_foldername, editTextFolderName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = editTextFolderName.getText().toString();
                        File newFolder = new File(getPathInfo(currentDir, inputText));
                        Log.d(TAG, "NewFolderPath " + getPathInfo(currentDir, inputText) + "/");

                        if (!newFolder.exists()) {
                            newFolder.mkdirs();
                        }
                        initCurrentDirAndChilren();
                    }
                });
                return true;
            case R.id.action_paste:
                Log.d(TAG, "action_paste():");
                if(checkWritePermissions(currentDir) && copyFile != null) {
                    copyFileOrDirectory(copyFile.getPath(), currentDir.getAbsolutePath());
                    initCurrentDirAndChilren();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Override onContextItemSelected.
     * Gets fired when an Item is selected from Context Menu
     * @param item
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected():");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        File editItem = x.getItem(info.position);

        checkReadPermissions(editItem);

        switch(item.getItemId()) {
            case R.id.action_open:
                if(editItem.isDirectory()) {
                    updateDir(editItem);
                }
                else {
                    openFile(editItem);
                }
                return true;
            case R.id.action_delete:
                if(checkWritePermissions(editItem)) {
                    deleteFile(editItem);
                    initCurrentDirAndChilren();
                }
                return true;
            case R.id.action_copy:
                copyFile = editItem;
                return true;
            case R.id.action_paste:
                if(editItem.isDirectory() && checkWritePermissions(editItem) && copyFile != null) {
                    Log.d(TAG, "action_paste():");
                    if(currentDir.isDirectory() && checkWritePermissions(currentDir)) {
                        copyFileOrDirectory(copyFile.getPath(), editItem.getAbsolutePath());
                        initCurrentDirAndChilren();
                    }
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void initCurrentDirAndChilren() {
        Log.d(TAG, "initCurrentDirAndChilren():");
        currentDir = new File(currentDir.getAbsolutePath());
        String[] children = currentDir.list();
        files.clear();

        for(String fileName : children) {
            File child = new File(getPathInfo(currentDir, fileName));
            files.add(child);
        }

        files.sort();

        if(currentDir.getParent() != null) {
            files.add(0, currentDir.getParentFile());
        }

        x.notifyDataSetChanged();
    }

    /**
     * onItemClick
     * Gets fired when an File or Folder is clicked
     */
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "Clicked on an Item");

        File clickedItem = x.getItem(i);

        if(!checkReadPermissions(clickedItem)) {
            return;
        }

        if(clickedItem.isDirectory()){
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

    /**
     * updateDir
     * This function is used to update current Folder
     * @param newDir
     * File (Folder)
     */
    public void updateDir(File newDir){
        Log.v(TAG, "updateDir(" + newDir + "):");
        if(newDir.isDirectory() && newDir.canRead()) {
            String filePath = newDir.getAbsolutePath();
            currentDir = new File(filePath);
            initCurrentDirAndChilren();

            fileList.setSelectionAfterHeaderView();
            fileList.setSelection(0);
        }
    }



    /**
     * openfile
     * Sends Intent to open a File
     * @param file
     * File gets opened
     */
    private void openFile(File file) {
        Log.v(TAG, "openFile()");
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
    public void onClick(View view) {

    }

    /**
     * checkReadPermissions
     * Checks Permissions allow to read File. If not sets Toast to inform
     * @param file
     * @return Boolean
     */
    public Boolean checkReadPermissions(File file) {
        if(file.canRead()) {
            return true;
        }
        else {
            Toast toast = Toast.makeText(getActivity(), R.string.fileviewer_missingpermissions, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }

    /**
     * checkWritePermissions
     * Checks Permissions allow to write File. If not sets Toast to inform
     * @param file
     * @return Boolean
     */
    public Boolean checkWritePermissions(File file) {
        if(file.canRead() && file.canWrite()) {
            return true;
        }
        else {
            Toast toast = Toast.makeText(getActivity(), R.string.fileviewer_missingpermissions, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }


    public void createBuilder(int title, EditText input,DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);


        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", listener);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
