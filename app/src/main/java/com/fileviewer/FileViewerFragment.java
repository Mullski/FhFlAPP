package com.fileviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.antonimuller.fhflapp.R;

/**
 * Created by Donny on 17.11.2016.
 */

public class FileViewerFragment extends Fragment {

    View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.filev_fragment, container, false);

        return fragmentView;
    }
}
