package com.navi;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.BuildConfig;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navi.map.MapHandler;
import com.navi.map.waypoints.Waypoint;
import com.example.antonimuller.fhflapp.R;
import org.osmdroid.views.MapView;

/**
 * Created by Björn on 12.12.2016.
 */
public class FragmentMapView extends Fragment {
    private static final String TAG = "fhflFragmentMapView";

    private static MapHandler mapHandler;
    private FloatingActionButton floatingActionButton_PanToUser;
    private TextView txtV_currentSpeed;

    private FragmentPOI fragmentPOI;
    private FragmentOwnPOI fragmentOwnPOI;
    private FragmentSavePOI fragmentSavePOI;

    public FragmentMapView() {

    }

    public void init(Context context) {
        setUserAgent();

        fragmentPOI = new FragmentPOI();
        fragmentOwnPOI = new FragmentOwnPOI();
        fragmentSavePOI = new FragmentSavePOI();
        fragmentSavePOI.setContext(context);
    }

    private void setUserAgent() {
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        setHasOptionsMenu(true);

        floatingActionButton_PanToUser = (FloatingActionButton)view.findViewById(R.id.fab_nav);
        floatingActionButton_PanToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapHandler.toggleFollow();
            }
        });

        txtV_currentSpeed = (TextView)view.findViewById(R.id.textview_currentspeed);

        mapHandler = new MapHandler(this.getActivity(),
                (MapView)view.findViewById(R.id.fragment_map_view),
                floatingActionButton_PanToUser,
                txtV_currentSpeed);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.nav_app_main_menu, menu);
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
                        .hide(this)
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
                        .hide(this)
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.action_togglenav:
                if(toggleNavLine()) {
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
                        .hide(this)
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

    public static MapHandler getMapHandler() { return mapHandler; }

    public boolean toggleNavLine() {
        return mapHandler.toggleNavLine();
    }

    public Waypoint getUserLocation() {
        return mapHandler.getUserLocation();
    }
}
