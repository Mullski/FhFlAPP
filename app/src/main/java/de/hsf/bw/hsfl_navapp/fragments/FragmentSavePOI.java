package de.hsf.bw.hsfl_navapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonimuller.fhflapp.R;
import de.hsf.bw.hsfl_navapp.gpx.GPXWriter;
import de.hsf.bw.hsfl_navapp.map.waypoints.Waypoint;

/**
 * Created by Björn on 15.12.2016.
 */
public class FragmentSavePOI extends Fragment {

    private Context context;
    private EditText waypoint_name;
    private Button btn_save;

    public FragmentSavePOI() { }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savepoi, container, false);

        btn_save = (Button)view.findViewById(R.id.save_poi);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMapView map = (FragmentMapView)getFragmentManager().findFragmentByTag("fgrMapView");
                Waypoint userLocation = map.getUserLocation();
                String wpname = waypoint_name.getText().toString();

                if(userLocation == null) {
                    Toast.makeText(context, "Aktueller Ort konnte nicht bestimmt werden!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wpname == "" || wpname == null) {
                    Toast.makeText(context, "Bitte Wegpunktnamen eingeben!", Toast.LENGTH_SHORT).show();
                    return;
                }

                userLocation.setName(wpname);
                GPXWriter writer = new GPXWriter(context, userLocation);
                if(writer.writeToFile()) {
                    waypoint_name.setText("");
                    Toast.makeText(context, "Wegpunkt \"" + wpname + "\" gespeichert!", Toast.LENGTH_SHORT).show();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(waypoint_name.getWindowToken(), 0);

                    getFragmentManager().popBackStackImmediate();

                    return;
                }

                Toast.makeText(context, "FEHLER!", Toast.LENGTH_SHORT).show();
            }
        });

        waypoint_name = (EditText)view.findViewById(R.id.waypointname);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(Color.WHITE);
    }

}
