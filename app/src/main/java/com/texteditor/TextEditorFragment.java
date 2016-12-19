package com.texteditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.antonimuller.fhflapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.R.attr.path;
import static android.R.attr.tag;

/**
 * Created by antonimuller on 13.11.16.
 */

public class TextEditorFragment extends Fragment
{
    String TAG = "TextEditorFragment";
    private int fragChoice;
    Intent intent;
    String openFilePath;

    public void setIntent(Intent intent){
        this.intent = intent;
    }

    public TextEditorFragment()
    {
        //intent = intenta;


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


        choice = inflater.inflate(R.layout.texteditor_fragment, container, false);

        setHasOptionsMenu(true);




        super.onCreateView(inflater, container, savedInstanceState);
        View view = choice;
        view.invalidate();

        openFileFromUrlIntent(view,intent);

       // return v;

        return view;
    }
    private static final int MENU_ADD = Menu.FIRST;
    private static final int MENU_LIST = Menu.FIRST + 1;
    private static final int MENU_REFRESH = Menu.FIRST + 2;
    private static final int MENU_LOGIN = Menu.FIRST + 3;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.texteditor_open_text_file).setVisible(true);
        menu.findItem(R.id.texteditor_save_text_file).setVisible(true);
        menu.findItem(R.id.texteditor_save_text_file_as).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    //Menu menu;
    //int PICKFILE_RESULT_CODE=1;
    int PICKFILE_RESULT_CODE=1;

    public void openFileFromUrlIntent(View view,Intent intent){
        if(intent == null)return;
        MultiAutoCompleteTextView multiAutoCompleteTextView11 = (MultiAutoCompleteTextView) view.findViewById(R.id.multiAutoCompleteTextView);

        Uri uri = intent.getData();
        if(uri == null)return;
        String url = uri.toString();
        url = uri.getPath();
        File file = new File(url);
        if(file.exists() && file.canRead()) {

            if (multiAutoCompleteTextView11 != null) {
                try{
                    Activity context = getActivity();
                    int k = Context.MODE_PRIVATE;
                    FileInputStream fis = new FileInputStream(url);
                    //FileInputStream fis = context.openFileInput(url, Context.MODE_PRIVATE);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    multiAutoCompleteTextView11.setText((sb.toString()));
                    openFilePath = url;
                }
                catch(Exception e){
                    multiAutoCompleteTextView11.setText(e.getMessage());
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG,"onActivityResult():");
        openFileFromUrlIntent(getView(),intent);
        /*



        if (requestCode == PICKFILE_RESULT_CODE) {

            MultiAutoCompleteTextView multiAutoCompleteTextView = (MultiAutoCompleteTextView)getView().findViewById(R.id.multiAutoCompleteTextView);
            String uri = intent.getData().toString();

            String content = readFileAsString(uri);
            multiAutoCompleteTextView.setText(content+"  "+intent.getDataString()+" - \n"+uri);
        }*/
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

    public void saveFileToUrlWithoutAsking(String path) {
        final File file = new File(path);
        // Make sure the path directory exists.

        try
        {
            if(!file.exists())
            {
                int p=path.lastIndexOf("/");
                String e=path.substring(p+1);
                File folder = new File(path.substring(0,p));
                if(!folder.exists()){
                    folder.mkdirs();
                }
                //file.mkdir();
                file.createNewFile();
            }
            MultiAutoCompleteTextView multiAutoCompleteTextView11 = (MultiAutoCompleteTextView) getView().findViewById(R.id.multiAutoCompleteTextView);
            String data = multiAutoCompleteTextView11.getText().toString();
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            openFilePath = path;
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void saveFileToUrl(final String path){






        if(path == null){
            saveOpenFileAsIntent();
            return;
        }

        final File file = new File(path);
        // Make sure the path directory exists.
        if(file.exists())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Überschreiben?");

// Set up the input
            final TextView text = new TextView(getActivity());
            text.setMaxLines(5);
            text.setSingleLine(false);
           // text.setLines(5);
            //text.setOverScrollMode(Ove);
            builder.setView(text);
            text.setText(path);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(10, ViewGroup.LayoutParams.MATCH_PARENT);
            text.setLayoutParams(lp);
            text.setEllipsize(TextUtils.TruncateAt.END);
// Set up the buttons
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveFileToUrlWithoutAsking(path);


                }
            });
            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();




        }else{
            saveFileToUrlWithoutAsking(path);
        }
        // Save your stream, don't forget to flush() it before closing it.

    }

    public void saveOpenFileAsIntent(){
        Log.d(TAG,"saveOpenFileAsIntent():");

        Log.d(TAG,Environment.getExternalStorageDirectory().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Dateipfad");

// Set up the input
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);
        String path = openFilePath == null?Environment.getExternalStorageDirectory().toString()+"/":openFilePath;
        input.setText(path);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(10, ViewGroup.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
// Set up the buttons
        builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = input.getText().toString();
                saveFileToUrl(path);

            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        /*
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        PackageManager packageManager = getActivity().getPackageManager();
        if (intent.resolveActivity(packageManager) == null) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Subject");
        }*/
        //startActivityForResult(Intent.createChooser(intent,  getResources().getString(R.string.texteditor_open_text_file)),PICKFILE_RESULT_CODE);
    }
    public void saveOpenFileIntent(){
        //openFile
        MultiAutoCompleteTextView multiAutoCompleteTextView11 = (MultiAutoCompleteTextView) getView().findViewById(R.id.multiAutoCompleteTextView);
        String data = multiAutoCompleteTextView11.getText().toString();

        saveFileToUrl(openFilePath);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected():");
        if(item.getItemId() == R.id.texteditor_open_text_file) {
            sendOpenFileIntent();
        }
        if(item.getItemId() == R.id.texteditor_save_text_file) {
            saveOpenFileIntent();
        }
        if(item.getItemId() == R.id.texteditor_save_text_file_as) {
            saveOpenFileAsIntent();
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
        startActivityForResult(Intent.createChooser(intent,  getResources().getString(R.string.texteditor_open_text_file)),PICKFILE_RESULT_CODE);
    }

}
