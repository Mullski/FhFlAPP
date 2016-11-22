package com.example.antonimuller.fhflapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by antonimuller on 13.11.16.
 */

public class TextEditorFragment extends Fragment
{
    String TAG = "TextEditorFragment";
    private int fragChoice;

    public TextEditorFragment()
    {

    }
    public TextEditorFragment(Intent intenta)
    {

        /*
        try{
            Uri uri = intent.getData();
            String url = uri.toString();
            Log.d("",url);

            WebFileDownloader wf = new WebFileDownloader(){
                @Override
                protected void onPostExecute(String result) {
                    // dismiss the dialog after the file was downloaded
                    //dismissDialog(progress_bar_type);
                    //browser.loadUrl("javascript:test(`finish`);");

                    Log.d("","sfxxx");

                    MultiAutoCompleteTextView multiAutoCompleteTextView = (MultiAutoCompleteTextView)getActivity().findViewById(R.id.multiAutoCompleteTextView);

                    multiAutoCompleteTextView.setText(this.result);
                    //showDialog(this.result);
                    //browser.loadUrl("javascript:openFile(`"+base64(this.result)+"`);");

                }
            };
            wf.execute(url);
        }catch(Exception e){

        }*/


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View choice;


        choice=inflater.inflate(R.layout.texteditor_fragment, container, false);

        setHasOptionsMenu(true);
        return choice;
    }
    private static final int MENU_ADD = Menu.FIRST;
    private static final int MENU_LIST = Menu.FIRST + 1;
    private static final int MENU_REFRESH = Menu.FIRST + 2;
    private static final int MENU_LOGIN = Menu.FIRST + 3;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_open_text_file).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    //Menu menu;
    //int PICKFILE_RESULT_CODE=1;
    int PICKFILE_RESULT_CODE=1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG,"onActivityResult():");
        if (requestCode == PICKFILE_RESULT_CODE) {

            MultiAutoCompleteTextView multiAutoCompleteTextView = (MultiAutoCompleteTextView)getView().findViewById(R.id.multiAutoCompleteTextView);
            String uri = intent.getData().toString();
            
            String content = readFileAsString(uri);
            multiAutoCompleteTextView.setText(content+"  "+intent.getDataString()+" - \n"+uri);
        }
        super.onActivityResult(requestCode,resultCode,intent);
    }

    public static String readFileAsString(String filePath) {
        String result = "";
        File file = new File(filePath);
        if ( file.exists() ) {
            //byte[] buffer = new byte[(int) new File(filePath).length()];
            FileInputStream fis = null;
            try {
                //f = new BufferedInputStream(new FileInputStream(filePath));
                //f.read(buffer);

                fis = new FileInputStream(file);
                char current;
                while (fis.available() > 0) {
                    current = (char) fis.read();
                    result = result + String.valueOf(current);
                }
            } catch (Exception e) {
                Log.d("TourGuide", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
            //result = new String(buffer);
        }
        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected():");
        if(item.getItemId() == R.id.action_open_text_file) {
            sendOpenFileIntent();
        }
        return super.onOptionsItemSelected(item);
    }
    protected void sendOpenFileIntent(){
        Log.d(TAG,"sendOpenFileIntent():");
        int PICKFILE_RESULT_CODE =1;
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        PackageManager packageManager = getActivity().getPackageManager();
        if (intent.resolveActivity(packageManager) == null) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Subject");
        }
        startActivityForResult(Intent.createChooser(intent,  getResources().getString(R.string.action_open_text_file)),PICKFILE_RESULT_CODE);
    }

}
