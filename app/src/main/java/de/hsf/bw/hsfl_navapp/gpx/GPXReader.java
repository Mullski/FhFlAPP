package de.hsf.bw.hsfl_navapp.gpx;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.hsf.bw.hsfl_navapp.map.waypoints.Waypoint;

/**
 * Created by Björn on 18.12.2016.
 */
public class GPXReader {
    private static final String TAG = "fhflGPXReader";

    private ArrayList<Waypoint> waypoints;

    public GPXReader(Context context, boolean readDefault) {
        waypoints = new ArrayList<>();

        try {
            InputStream inputStream;

            if(readDefault) {
                Log.v(TAG, "    Reading default file");
                inputStream = context.getAssets().open(GPXUtils.DEFAULT_POI);
            } else {
                Log.v(TAG, "    Reading user file");
                inputStream = new FileInputStream(GPXUtils.getUserPOIFile());
            }

            readFile(inputStream);
            if(inputStream != null) inputStream.close();

        }catch(Exception e) {
            Log.e(TAG, "An error occured!");
            e.printStackTrace();
        }
    }

    public Waypoint getWaypoint(int i) {
        return waypoints.get(i);
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    private void readFile(InputStream input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(input);

        Element element = doc.getDocumentElement();
        element.normalize();

        NodeList nodeList = doc.getElementsByTagName("wpt");

        if(nodeList.getLength() >= 1) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Waypoint wpt;
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    wpt = new Waypoint(
                            Float.parseFloat(node.getAttributes().getNamedItem("lat").getNodeValue()),
                            Float.parseFloat(node.getAttributes().getNamedItem("lon").getNodeValue()));
                    wpt.setName(getAttributeValueByTag(node, "name"));
                    Log.v(TAG, "    Read WPT: " + wpt.getName());
                    waypoints.add(wpt);
                }
            }
            Log.v(TAG, "    read all waypoints from file");
        } else {
            Log.v(TAG, "    there seems to be no waypoints in this file...");
        }
    }

    private String getAttributeValueByTag(Node node, String tag) {
        Element element = (Element)node;
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue();
    }
}
