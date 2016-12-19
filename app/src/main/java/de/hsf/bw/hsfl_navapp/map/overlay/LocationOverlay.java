package de.hsf.bw.hsfl_navapp.map.overlay;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import de.hsf.bw.hsfl_navapp.R;

/**
 * Created by Björn on 13.12.2016.
 */
public class LocationOverlay extends MyLocationNewOverlay {
    private static final String TAG = "fhflMyLocationOverlay";

    private FloatingActionButton fab;

    public LocationOverlay(IMyLocationProvider myLocationProvider, MapView mapView) {
        super(myLocationProvider, mapView);
    }

    public void setFAB(FloatingActionButton fab) { this.fab = fab; }

    @Override
    public void enableFollowLocation() {
        super.enableFollowLocation();
        fab.setBackgroundTintList(ColorStateList.valueOf(0xFF3F51B5));
        fab.setImageResource(R.drawable.ic_my_location_white_24dp);
    }

    @Override
    public void disableFollowLocation() {
        super.disableFollowLocation();
        fab.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
        fab.setImageResource(R.drawable.ic_my_location_black_24dp);
    }
}
