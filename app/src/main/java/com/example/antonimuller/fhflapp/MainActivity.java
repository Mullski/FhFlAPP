package com.example.antonimuller.fhflapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import de.rp_byte.neroazure.chat.ChatFragment;

import static com.example.antonimuller.fhflapp.R.id.toolbar;

import com.fileviewer.FileViewerFragment;
import com.navi.FragmentMapView;
import com.streich.todo.TodoFragment;
import com.texteditor.TextEditorFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String TAG ="MainActivity";

    private ExampleFragment exFragment1;
    private ExampleFragment exFragment2;
    private TodoFragment todos;
    private FragmentMapView fragmentMapView;
    private FileViewerFragment fileView;
    private ChatFragment chat;
    private TextEditorFragment textEditorFragment;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Beispiel Fragmente instanziieren
        exFragment1=new ExampleFragment(1);
        exFragment2=new ExampleFragment(2);
        todos = new TodoFragment();
        fragmentMapView = new FragmentMapView();
        fragmentMapView.init(getApplicationContext());
        fileView = new FileViewerFragment();
        chat = new ChatFragment();
        textEditorFragment = new TextEditorFragment();
        textEditorFragment.setIntent(getIntent());

        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, exFragment1).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final Activity activity = this;

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            private float last = 0;

            @Override
            public void onDrawerSlide(View arg0, float arg1) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }

            @Override public void onDrawerStateChanged(int arg0) {}
            @Override public void onDrawerOpened(View arg0) {}
            @Override public void onDrawerClosed(View arg0) {}

        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*

    InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_editor:

                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, textEditorFragment);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                break;
            case R.id.nav_tdList:

                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, todos);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                break;
            case R.id.nav_navi:
                //nav Fragment
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, fragmentMapView);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                break;
            case R.id.nav_radio:
                //radio Fragment
                break;
            case R.id.nav_Fviewer:
                //File Viewer Fragment
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, fileView);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                break;
            case R.id.chat:
                //File Viewer Fragment
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, chat);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                break;
            default:
                fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, exFragment1);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                break;


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
