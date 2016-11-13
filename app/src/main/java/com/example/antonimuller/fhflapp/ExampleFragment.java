package com.example.antonimuller.fhflapp;

import android.app.Fragment;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

/**
 * Created by antonimuller on 13.11.16.
 */

public class ExampleFragment extends Fragment
{
    private int fragChoice;

    public ExampleFragment()
    {

    }
    public ExampleFragment(int a)
    {
        fragChoice=a;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View choice;

        switch (fragChoice) {
            case 1:
                // Inflate the layout for this fragment
                choice=inflater.inflate(R.layout.fragment_ex1, container, false);
                break;

            case 2:
                // Inflate the layout for this fragment
                choice=inflater.inflate(R.layout.fragment_ex2, container, false);
                break;

            default:
                // Inflate the layout for this fragment
                choice=inflater.inflate(R.layout.fragment_ex1, container, false);
                break;

        }

        return choice;
    }
}
