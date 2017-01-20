package com.navi.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.ArrayList;

import com.navi.interfaces.OnWaypointSelectedListener;
import com.navi.map.overlay.LocationOverlay;
import com.navi.map.overlay.NavigationOverlay;
import com.navi.map.waypoints.Waypoint;

/**
 * Created by Björn on 12.12.2016.
 */
public class MapHandler implements OnWaypointSelectedListener {
    private static final String TAG = "fhflMapHandler";

    public static final GeoPoint START_GEOPOINT = new GeoPoint(54.77493, 9.44987);

    private MapView mapView;
    private IMapController mapController;

    private GeoPoint userLocation;
    private GeoPoint selectedWaypoint;

    private NavigationOverlay navigationOverlay;
    private LocationOverlay locationOverlay;
    private ItemizedIconOverlay<OverlayItem> itemizedIconOverlay;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private FloatingActionButton fab_panToUser;
    private TextView txtV_currentSpeed;


    public MapHandler(Activity parent, final MapView mapView, FloatingActionButton fab, TextView tv) {
        Log.v(TAG, "MapHandler()------------------------->");
        this.fab_panToUser = fab;
        this.txtV_currentSpeed = tv;

        this.mapView = mapView;
        this.mapView.setMultiTouchControls(true);
        this.mapView.setBuiltInZoomControls(true);

        mapController = this.mapView.getController();
        mapController.setCenter(START_GEOPOINT);
        mapController.setZoom(19);

        Context context = parent.getApplicationContext();

        navigationOverlay = new NavigationOverlay();
        mapView.getOverlays().add(navigationOverlay);

        locationOverlay = new LocationOverlay(new GpsMyLocationProvider(context),mapView);
        locationOverlay.setFAB(fab_panToUser);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);

        itemizedIconOverlay = new ItemizedIconOverlay<>(
                new ArrayList<OverlayItem>(),
                context.getResources().getDrawable(org.osmdroid.library.R.drawable.marker_default),
                null, context);
        mapView.getOverlays().add(itemizedIconOverlay);

        userLocation = null;
        selectedWaypoint = null;

        locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location l) {
                Log.v(TAG, "onLocationChanged()");

                if(userLocation == null) {
                    userLocation = new GeoPoint(l.getLatitude(), l.getLongitude());
                    toggleFollow();
                } else {
                    userLocation = new GeoPoint(l.getLatitude(), l.getLongitude());
                }

                if(l.hasSpeed()) {
                    float speed = l.getSpeed();
                    txtV_currentSpeed.setText("Geschwindigkeit: " + speed + " m/s");
                }

                navigationOverlay.setNewUserLocation(userLocation);
                mapView.invalidate();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.v(TAG, "onStatusChanged()");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.v(TAG, "onProviderEnabled()");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.v(TAG, "onProviderDisabled()");
            }
        };
        turnOnLocationUpdates(parent);
    }

    private void turnOnLocationUpdates(Activity parent) {
        Log.v(TAG, "turnOnLocationUpdates()");
        if (ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    @Override
    public void onWaypointSelected(Waypoint w) {
        Log.v(TAG, "onWaypointSelected( " + w.getName() + " )");
        selectedWaypoint = new GeoPoint(w.getLat(), w.getLon());
        navigationOverlay.setWaypoint(selectedWaypoint);
        itemizedIconOverlay.removeAllItems();
        itemizedIconOverlay.addItem(
                new OverlayItem(w.getName(), "",
                        new GeoPoint(w.getLat(), w.getLon()))
        );
        mapController.animateTo(new GeoPoint(w.getLat(), w.getLon()));
        mapController.zoomTo(19);
        locationOverlay.disableFollowLocation();
        mapView.invalidate();
    }

    public void toggleFollow() {
        Log.v(TAG, "toggleFollow() ");
        if(userLocation == null) return;
        if(locationOverlay.isFollowLocationEnabled()) {
            locationOverlay.disableFollowLocation();
        } else {
            locationOverlay.enableFollowLocation();
        }
    }

    public boolean toggleNavLine() {
        boolean res = navigationOverlay.toggleNavLine();
        mapView.invalidate();
        return res;
    }

    public Waypoint getUserLocation() {
        if(userLocation == null) return null;

        return new Waypoint(userLocation.getLatitude(), userLocation.getLongitude());
    }
}
