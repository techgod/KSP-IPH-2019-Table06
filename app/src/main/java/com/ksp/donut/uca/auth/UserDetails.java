package com.ksp.donut.uca.auth;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ksp.donut.uca.MainActivity;
import com.ksp.donut.uca.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetails extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText fName,lName;

    public UserDetails() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        fName = view.findViewById(R.id.fname_et);
        lName = view.findViewById(R.id.lname_et);


        view.findViewById(R.id.savedetails).setOnClickListener(this);

        return view;


    }

    @Override
    public void onClick(View v) {

        FirestoreHandler handler = new FirestoreHandler(getActivity());

        handler.addData(mAuth.getUid(),fName.getText().toString(),lName.getText().toString(),"users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        },2000);

    }
}
