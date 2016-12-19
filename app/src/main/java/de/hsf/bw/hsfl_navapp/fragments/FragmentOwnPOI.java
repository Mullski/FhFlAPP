package de.hsf.bw.hsfl_navapp.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.antonimuller.fhflapp.R;
import de.hsf.bw.hsfl_navapp.gpx.GPXReader;
import de.hsf.bw.hsfl_navapp.gpx.GPXWriter;
import de.hsf.bw.hsfl_navapp.interfaces.OnWaypointSelectedListener;
import de.hsf.bw.hsfl_navapp.map.waypoints.Waypoint;
import de.hsf.bw.hsfl_navapp.map.waypoints.WaypointAdapter;

/**
 * Created by Björn on 12.12.2016.
 */
public class FragmentOwnPOI extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "fhflFragmentOwnPOI";

    private OnWaypointSelectedListener onWaypointSelectedListener;

    private ArrayList<Waypoint> waypoints;
    private GPXReader gpxReader;
    private ListView listView;
    private WaypointAdapter adapter;
    private int waypointIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated()");

        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(Color.WHITE);

        gpxReader = new GPXReader(getActivity().getApplicationContext(), false);

        waypoints = gpxReader.getWaypoints();
        Log.v(TAG, "    read " + waypoints.size() + " WPs");
        adapter = new WaypointAdapter(this.getActivity(),
                R.layout.listview_item_waypoint, waypoints);

        listView = (ListView)getView().findViewById(R.id.listview_poi);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        onWaypointSelectedListener = (OnWaypointSelectedListener)FragmentMapView.getMapHandler();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "onItemClick( i: " + i + " )");
        getFragmentManager().popBackStackImmediate();
        onWaypointSelectedListener.onWaypointSelected(gpxReader.getWaypoint(i));
        listView.setSelectionAfterHeaderView();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "onItemLongClick( i: " + i + " )");
        waypointIndex = i;

        DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.v(TAG, "Deleting WPT: " + waypointIndex);
                        waypoints.remove(waypointIndex);
                        GPXWriter.writeToFile(waypoints);
                        adapter.notifyDataSetChanged();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Ausgewählten Wegpunkt löschen?")
                .setNegativeButton("Nein", onDialogClick)
                .setPositiveButton("Ja", onDialogClick)
                .show();
        return true;
    }
}
