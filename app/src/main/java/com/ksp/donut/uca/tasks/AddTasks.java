package com.ksp.donut.uca.tasks;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksp.donut.uca.R;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTasks extends Fragment implements View.OnClickListener {

    private EditText taskName;
    private TextView displayDeadline;
    private Calendar myCalendar ;
    private FirebaseFirestore mDb;
    private String peer_no,peer_name;


    public AddTasks() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.set_deadline).setOnClickListener(this);
        view.findViewById(R.id.save_tasks).setOnClickListener(this);
        view.findViewById(R.id.phone_book).setOnClickListener(this);
        taskName = view.findViewById(R.id.task_name);
        displayDeadline = view.findViewById(R.id.display_time);

        myCalendar = Calendar.getInstance();


        if (getArguments() != null) {
            Log.d(TAG, "arguments" + getArguments().getString("no"));
            peer_no = getArguments().getString("no");
            peer_name = getArguments().getString("name");

            TextView tv = view.findViewById(R.id.task_assigned_view);
            tv.setText(peer_name);

        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_tasks, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

         mDb = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View v) {


         switch (v.getId()){

             case R.id.save_tasks : saveTask();break;
             case R.id.set_deadline : setDeadline();break;
             case R.id.phone_book : loadContacts();break;

         }

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        displayDeadline.setText(sdf.format(myCalendar.getTime()));
    }


    public void saveTask(){

        if(taskName.getText().toString() == "" || peer_no == null ){
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }else {

            Map<String, Object> tasks = new HashMap<>();
            tasks.put("taskName", taskName.getText().toString());
            tasks.put("deadLine", displayDeadline.getText().toString());
            tasks.put("assignedToNo", peer_no);
            tasks.put("assignedToName",peer_name);

            // Add a new document with a generated ID
            mDb.collection("myTasks")
                    .add(tasks)
                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        }

    }

    public void setDeadline(){

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void loadContacts(){
        NavController navController = NavHostFragment.findNavController(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean("from_add",true);
        navController.navigate(R.id.nav_contact,bundle);
        //show contacts screen
    }
}
