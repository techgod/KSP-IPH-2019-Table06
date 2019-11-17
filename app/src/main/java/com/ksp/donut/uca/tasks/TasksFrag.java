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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.ksp.donut.uca.R;

import java.util.ArrayList;

public class TasksFrag extends Fragment implements View.OnClickListener{
    FirebaseFirestore mDb;
    private String TAG = TasksFrag.class.getSimpleName();

    private ArrayList<TaskDetails> myTasks;
    private TaskAdapter mAdapter;
    private TextView dueTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_layout, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb = FirebaseFirestore.getInstance();

        dueTextView = view.findViewById(R.id.due_text_view);

        final RecyclerView task_rv = view.findViewById(R.id.task_rv);
        task_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        myTasks = new ArrayList<>();
        mAdapter = new TaskAdapter(getActivity(), myTasks);
        task_rv.setAdapter(mAdapter);

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

        view.findViewById(R.id.addTask).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        //fab opens tasks to be assigned

        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.nav_add_tasks);

    }

    private void setData(QueryDocumentSnapshot document) {
        TaskDetails dm = new TaskDetails();
        dm.setTaskName(document.getString("taskName"));
        dm.setAssignedBy(document.getString("assignedTo"));
        dm.setTaskDeadline(document.getString("deadLine"));
        myTasks.add(dm);
    }
}
