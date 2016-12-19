package de.hsf.bw.hsfl_navapp.map.waypoints;

/**
 * Created by Björn on 12.12.2016.
 */
public class Waypoint {
    private static final String TAG = "fhflWaypoint";

    private float lat;
    private float lon;
    private String name;

    public Waypoint() {
        lat = 0f;
        lon = 0f;
        name = "WAYPOINT NOT INITIALIZED";
    }

    public Waypoint(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
        this.name = "NULL";
    }

    public Waypoint(double lat, double lon) {
        this((float)lat, (float)lon);
    }

    public Waypoint(float lat, float lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public float getLat() { return lat; }
    public void setLat(float value) { lat = value; }

    public float getLon() { return lon; }
    public void setLon(float value) { lon = value; }

    public String getName() { return name; }
    public void setName(String value) { name = value; }
}
