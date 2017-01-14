package com.navi.map.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import com.navi.map.waypoints.Waypoint;

/**
 * Created by Björn on 14.12.2016.
 */
public class NavigationOverlay extends Overlay {
    private static final String TAG = "fhflNavigationOverlay";

    private Paint paint;
    private Waypoint userLocation;
    private Waypoint selectedWaypoint;
    private boolean showNavLine;

    public NavigationOverlay()  {

        userLocation = null;
        selectedWaypoint = null;


        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(7.5f);

        userLocation = null;
        selectedWaypoint = null;
        showNavLine = false;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean b) {
        if(showNavLine) {
            Point user = calc(mapView, userLocation);
            Point waypoint = calc(mapView, selectedWaypoint);
            canvas.drawLine(user.x, user.y, waypoint.x, waypoint.y, paint);
        }
    }

    public boolean toggleNavLine() {
        Log.v(TAG, "toggleNavLine()");

        if(userLocation == null || selectedWaypoint == null) return false;
        Log.v(TAG, userLocation.toString() + "/" + selectedWaypoint.toString());
        showNavLine = !showNavLine;
        Log.v(TAG, "    showing line");
        return showNavLine;
    }

    public void setNewUserLocation(GeoPoint newUserLocation) {
        Log.v(TAG, "setNewUserLocation()");
        userLocation = new Waypoint(newUserLocation.getLatitude(), newUserLocation.getLongitude());
        Log.v(TAG, "    @ " + userLocation.getLat() + "/" + userLocation.getLon());
    }

    public void setWaypoint(GeoPoint waypoint) {
        Log.v(TAG, "setWaypoint()");
        selectedWaypoint = new Waypoint(waypoint.getLatitude(), waypoint.getLongitude());
        Log.v(TAG, "    @ " + selectedWaypoint.getLat() + "/" + selectedWaypoint.getLon());
    }

    private Point calc(MapView map, Waypoint w) {
        return map.getProjection().toPixels(new GeoPoint(w.getLat(), w.getLon()), null);
    }
}
