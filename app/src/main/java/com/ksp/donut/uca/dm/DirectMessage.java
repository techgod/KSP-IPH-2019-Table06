package com.ksp.donut.uca.dm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class DirectMessage extends Fragment {
    FirebaseFirestore mDb;
    public String TAG = DirectMessage.class.getSimpleName();

    private ArrayList<DMDetails> upcoming;
    DMAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.direct_message_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb = FirebaseFirestore.getInstance();

        final RecyclerView upcoming_rv = view.findViewById(R.id.dm_rv);
        upcoming_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        upcoming = new ArrayList<>();
        mAdapter = new DMAdapter(getActivity(), upcoming);
        upcoming_rv.setAdapter(mAdapter);

        mDb.collection("chats")
                .whereEqualTo("receiver", "9845359774")
                .get(Source.SERVER)
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
                            Toast.makeText(getContext(), "Got chats", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Error getting events.", task.getException());
                            Toast.makeText(getContext(), "Couldn't update events list. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void setData(QueryDocumentSnapshot document) {
        DMDetails dm = new DMDetails();
        dm.setSender(document.getString("sender"));
        upcoming.add(dm);
    }

}
