package de.hsf.bw.hsfl_navapp.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.hsf.bw.hsfl_navapp.R;
import de.hsf.bw.hsfl_navapp.gpx.GPXReader;
import de.hsf.bw.hsfl_navapp.interfaces.OnWaypointSelectedListener;
import de.hsf.bw.hsfl_navapp.map.waypoints.WaypointAdapter;

/**
 * Created by Björn on 12.12.2016.
 */
public class FragmentPOI extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "fhflFragmentPOI";

    private OnWaypointSelectedListener onWaypointSelectedListener;

    private GPXReader gpxReader;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(Color.WHITE);

        gpxReader = new GPXReader(getActivity().getApplicationContext(), true);

        WaypointAdapter adapter = new WaypointAdapter(this.getActivity(),
                R.layout.listview_item_waypoint,
                gpxReader.getWaypoints());

        listView = (ListView)getView().findViewById(R.id.listview_poi);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        onWaypointSelectedListener = (OnWaypointSelectedListener)FragmentMapView.getMapHandler();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "onClick( i: " + i + " )");
        getFragmentManager().popBackStackImmediate();
        onWaypointSelectedListener.onWaypointSelected(gpxReader.getWaypoint(i));
        listView.setSelectionAfterHeaderView();
    }
}
