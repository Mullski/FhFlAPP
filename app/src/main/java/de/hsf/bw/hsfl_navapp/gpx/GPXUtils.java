package de.hsf.bw.hsfl_navapp.gpx;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Björn on 18.12.2016.
 */
public class GPXUtils {
    private static final String TAG = "fhflGPXUtils";

    public static final String DEFAULT_POI = "default_pois.gpx";
    public static final String USER_POI = "own_pois.gpx";

    public static File getUserPOIFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS) + "/HSFL-NavApp/" + USER_POI);

        if(!file.exists()) {
            Log.v(TAG, "UserFile doesn't exists. I try to create it");
            File folder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS) + "/HSFL-NavApp/");
            folder.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
