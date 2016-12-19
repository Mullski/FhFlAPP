package de.hsf.bw.hsfl_navapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.util.GeoPoint;

import de.hsf.bw.hsfl_navapp.fragments.FragmentMapView;
import de.hsf.bw.hsfl_navapp.fragments.FragmentOwnPOI;
import de.hsf.bw.hsfl_navapp.fragments.FragmentPOI;
import de.hsf.bw.hsfl_navapp.fragments.FragmentSavePOI;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "fhflMainActivity";

    public static final GeoPoint START_GEOPOINT = new GeoPoint(54.77493, 9.44987);

    private FragmentMapView fragmentMapView;
    private FragmentPOI fragmentPOI;
    private FragmentOwnPOI fragmentOwnPOI;
    private FragmentSavePOI fragmentSavePOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMapView = new FragmentMapView();
        fragmentMapView.setUserAgent(BuildConfig.APPLICATION_ID);

        fragmentPOI = new FragmentPOI();
        fragmentOwnPOI = new FragmentOwnPOI();
        fragmentSavePOI = new FragmentSavePOI();
        fragmentSavePOI.setContext(getApplicationContext());

        if(getFragmentManager().findFragmentByTag("fgrMapView") == null &&
                getFragmentManager().findFragmentByTag("fgrPOI") == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragmentMapView, "fgrMapView")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_app_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected( " + item.getTitle() + " );");

        switch(item.getItemId()) {
            case R.id.action_pois:
                if(getFragmentManager().findFragmentByTag("frgPOI") != null) {
                    Log.v(TAG, "    fragment already active!");
                    break;
                }

                popFromBackstack("frgOwnPOI");

                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragmentPOI, "frgPOI")
                        .hide(fragmentMapView)
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.action_own_pois:
                if(getFragmentManager().findFragmentByTag("frgOwnPOI") != null) {
                    Log.v(TAG, "    fragment already active!");
                    break;
                }

                popFromBackstack("frgPOI");

                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragmentOwnPOI, "frgOwnPOI")
                        .hide(fragmentMapView)
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.action_togglenav:
                if(fragmentMapView.toggleNavLine()) {
                    item.setTitle(R.string.navapp_hide_nav);
                } else {
                    item.setTitle(R.string.navapp_show_nav);
                }
                break;

            case R.id.action_save:
                if(getFragmentManager().findFragmentByTag("frgSavePOI") != null) {
                    Log.v(TAG, "    fragment already active!");
                    break;
                }

                popFromBackstack("frgPOI");
                popFromBackstack("frgOwnPOI");

                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragmentSavePOI, "frgSavePOI")
                        .hide(fragmentMapView)
                        .addToBackStack(null)
                        .commit();
                break;

        }
        return true;
    }

    private void popFromBackstack(String tag) {
        if(getFragmentManager().findFragmentByTag(tag) != null) {
            getFragmentManager().popBackStackImmediate();
        }
    }
}
