package com.ksp.donut.uca.contact;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.ksp.donut.uca.R;
import com.ksp.donut.uca.dm.DMAdapter;
import com.ksp.donut.uca.dm.DMDetails;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectContact extends Fragment {

    FirebaseFirestore mDb;
    private ArrayList<ContactDetails> upcoming;
    ContactAdapter mAdapter;
    private static String TAG = SelectContact.class.getSimpleName();

    public SelectContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_contact, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();

        final RecyclerView upcoming_rv = view.findViewById(R.id.contact_rv);
        upcoming_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        upcoming = new ArrayList<>();
        mAdapter = new ContactAdapter(getActivity(), upcoming);
        upcoming_rv.setAdapter(mAdapter);

        mDb.collection("personnel")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        upcoming.clear();
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                            Log.d(TAG, "Got from online db, with size: " + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                setData(document);
                            }
                            mAdapter.setCards(upcoming);
                            Toast.makeText(getContext(), "Retrieved Contacts!", Toast.LENGTH_LONG).show();
                        } else if(task.getResult().size()==0){
                        }
                        else
                        {
                            Log.d(TAG, "Error getting events.", task.getException());
                            Toast.makeText(getContext(), "Couldn't update events list. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void setData(QueryDocumentSnapshot document) {
        ContactDetails cm = new ContactDetails();
        cm.setName(document.getString("name"));
        cm.setNumber(document.get("mobile_number").toString());
        upcoming.add(cm);
    }
}
