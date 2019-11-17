package com.ksp.donut.uca.dm;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.ksp.donut.uca.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DirectMessage extends Fragment {
    FirebaseFirestore mDb;
    public String TAG = DirectMessage.class.getSimpleName();
    private String myNumber;

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
        myNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);


        FloatingActionButton new_chat = view.findViewById(R.id.dm_fab);
        new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);

                navController.navigate(R.id.nav_contact);
            }
        });
        final RecyclerView upcoming_rv = view.findViewById(R.id.dm_rv);
        upcoming_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        upcoming = new ArrayList<>();
        mAdapter = new DMAdapter(getActivity(), upcoming);
        upcoming_rv.setAdapter(mAdapter);
        TextView no_convo = view.findViewById(R.id.dm_no_convo);

        mDb.collection("chats")
                .whereArrayContains("numbers",myNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        upcoming.clear();
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                            no_convo.setVisibility(View.GONE);
                            Log.d(TAG, "Got from online db, with size: " + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                setData(document);
                            }
                            mAdapter.setCards(upcoming);
                        } else if(task.getResult().size()==0){
                            no_convo.setVisibility(View.VISIBLE);

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
        DMDetails dm = new DMDetails();
        ArrayList<String> arrayList = (ArrayList<String>) document.get("numbers");
        /*if(arrayList!=null)
        {
            Log.d(TAG,"arraylist not null"+ arrayList.get(0));
        }*/
        String toset = arrayList.get(0).equals(myNumber)?arrayList.get(1):arrayList.get(0);

        dm.setNumber(toset);
        upcoming.add(dm);
    }

}
