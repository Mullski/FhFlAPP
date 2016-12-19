package de.hsf.bw.hsfl_navapp.map.waypoints;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hsf.bw.hsfl_navapp.R;

/**
 * Created by Björn on 12.12.2016.
 */
public class WaypointAdapter extends ArrayAdapter<Waypoint> {
    private static final String TAG = "fhflWaypointAdapter";

    private Context context;
    private int resource;
    private ArrayList<Waypoint> waypoints;

    public WaypointAdapter(Context context, int resource, ArrayList<Waypoint> waypoints) {
        super(context, resource, waypoints);

        this.context = context;
        this.resource = resource;
        this.waypoints = waypoints;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {

        WaypointHolder holder = null;

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        view = inflater.inflate(resource, container, false);
        view.setTag(holder);

        holder = new WaypointHolder();
        holder.WaypointName = (TextView)view.findViewById(R.id.list_item_name);

        Waypoint wpt = waypoints.get(position);
        holder.WaypointName.setText(wpt.getName());

        return view;
    }

    @Override
    public int getCount() {
        return waypoints.size();
    }
}
