package de.hsf.bw.hsfl_navapp.gpx;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import de.hsf.bw.hsfl_navapp.map.waypoints.Waypoint;

/**
 * Created by Björn on 18.12.2016.
 */
public class GPXWriter {
    private static final String TAG = "fhflGPXWriter";

    private GPXReader gpxReader;
    private ArrayList<Waypoint> waypoints;

    public GPXWriter(Context context, Waypoint waypoint) {

        gpxReader = new GPXReader(context, false);

        waypoints = new ArrayList<>(gpxReader.getWaypoints());
        waypoints.add(waypoint);
    }

    public boolean writeToFile() {
        Log.v(TAG, "writeToFile()");

        try {
            File file = GPXUtils.getUserPOIFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "gpx");
            xmlSerializer.attribute(null, "version", "1.1");

            for(Waypoint w : waypoints) {
                xmlSerializer.startTag(null, "wpt");
                xmlSerializer.attribute(null, "lat", "" + w.getLat());
                xmlSerializer.attribute(null, "lon", "" + w.getLon());
                xmlSerializer.startTag(null, "name");
                xmlSerializer.text(w.getName());
                xmlSerializer.endTag(null, "name");
                xmlSerializer.endTag(null, "wpt");
            }
            xmlSerializer.endTag(null, "gpx");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();

            Log.v(TAG, "--> Done!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean writeToFile(ArrayList<Waypoint> waypoints) {
        Log.v(TAG, "writeToFile()");

        try {
            File file = GPXUtils.getUserPOIFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "gpx");
            xmlSerializer.attribute(null, "version", "1.1");

            for(Waypoint w : waypoints) {
                xmlSerializer.startTag(null, "wpt");
                xmlSerializer.attribute(null, "lat", "" + w.getLat());
                xmlSerializer.attribute(null, "lon", "" + w.getLon());
                xmlSerializer.startTag(null, "name");
                xmlSerializer.text(w.getName());
                xmlSerializer.endTag(null, "name");
                xmlSerializer.endTag(null, "wpt");
            }
            xmlSerializer.endTag(null, "gpx");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();

            Log.v(TAG, "    Done!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
