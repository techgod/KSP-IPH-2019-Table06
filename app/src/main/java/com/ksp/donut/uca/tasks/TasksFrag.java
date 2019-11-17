package com.ksp.donut.uca.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.ksp.donut.uca.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TasksFrag extends Fragment implements View.OnClickListener{
    FirebaseFirestore mDb;
    private String TAG = TasksFrag.class.getSimpleName();
    private boolean isUserSp = false;

    private ArrayList<TaskDetails> myTasks;
    private TaskAdapter mAdapter;
    private TextView dueTextView;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_layout, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb = FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();

        view.findViewById(R.id.addTask).setVisibility(View.INVISIBLE);

        String phone = mAuth.getCurrentUser().getPhoneNumber();
        phone = phone.substring(3);
        mDb.collection("personnel")
                .whereEqualTo("mobile_number",phone)
                .get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().size()>0){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            if(document.get("position").equals("SP")){
                                isUserSp = true;
                                //Toast.makeText(getContext(), "SP is logged in", Toast.LENGTH_SHORT).show();
                                Log.i("asf","SP is logged in");


                            }
                        }
                        loadAdapter(view);
                    }
                });



        dueTextView = view.findViewById(R.id.due_text_view);


    }

    private void loadAdapter(View view) {


        if(isUserSp){
            view.findViewById(R.id.addTask).setVisibility(View.VISIBLE);
            view.findViewById(R.id.addTask).setOnClickListener(this);

        }else{
            view.findViewById(R.id.addTask).setVisibility(View.INVISIBLE);

        }


        final RecyclerView task_rv = view.findViewById(R.id.task_rv);
        task_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        myTasks = new ArrayList<>();
        mAdapter = new TaskAdapter(getActivity(), myTasks, isUserSp);
        task_rv.setAdapter(mAdapter);



        if(isUserSp){
            mDb.collection("myTasks")
                    .get(Source.SERVER)
                    .addOnCompleteListener(task -> {
                        myTasks.clear();
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                            dueTextView.setVisibility(View.GONE);
                            Log.d(TAG, "Got from online db, with size: " + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                setData(document);
                            }
                            mAdapter.setCards(myTasks);
                        } else {
                            dueTextView.setVisibility(View.VISIBLE);
                            Log.d(TAG, "Error getting events.", task.getException());
                            Toast.makeText(getContext(), "Couldn't update events list. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
        }else{

            mDb.collection("myTasks")
                    .whereEqualTo("assignedToNo",mAuth.getCurrentUser().getPhoneNumber().substring(3))
                    .get(Source.SERVER)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0){
                            dueTextView.setVisibility(View.GONE);
                            for(QueryDocumentSnapshot document : task.getResult()){

                                setData(document);
                                Toast.makeText(getContext(), "Read data", Toast.LENGTH_SHORT).show();
                            }
                            mAdapter.setCards(myTasks);

                        }else{
                            dueTextView.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {

        //fab opens tasks to be assigned


        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.nav_add_tasks);

    }

    private void setData(QueryDocumentSnapshot document) {

        TaskDetails task = new TaskDetails();
        task.setTaskName(document.getString("taskName"));
        task.setTaskDeadline(document.getString("deadLine"));
        if(isUserSp) {

            task.setAssignedToNo(document.getString("assignedTo"));
            task.setAssignedToName(document.getString("assignedToName"));


        }
        myTasks.add(task);

    }
}
