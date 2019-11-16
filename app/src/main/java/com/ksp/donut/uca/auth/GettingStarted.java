package com.ksp.donut.uca.auth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ksp.donut.uca.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GettingStarted extends Fragment implements View.OnClickListener {


    public GettingStarted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_getting_started,container,false);

        view.findViewById(R.id.getStarted).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment fragment = new Signup();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }
}
