package de.hsf.bw.hsfl_navapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.w3c.dom.Text;

import de.hsf.bw.hsfl_navapp.BuildConfig;
import de.hsf.bw.hsfl_navapp.R;
import de.hsf.bw.hsfl_navapp.map.MapHandler;
import de.hsf.bw.hsfl_navapp.map.waypoints.Waypoint;

/**
 * Created by Björn on 12.12.2016.
 */
public class FragmentMapView extends Fragment {
    private static final String TAG = "fhflFragmentMapView";

    private static MapHandler mapHandler;
    private FloatingActionButton floatingActionButton_PanToUser;
    private TextView txtV_currentSpeed;



    public FragmentMapView() {
        setUserAgent();
    }

    private void setUserAgent() {
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

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

    public static MapHandler getMapHandler() { return mapHandler; }

    public boolean toggleNavLine() {
        return mapHandler.toggleNavLine();
    }

    public Waypoint getUserLocation() {
        return mapHandler.getUserLocation();
    }
}
