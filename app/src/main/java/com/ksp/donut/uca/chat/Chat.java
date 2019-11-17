package com.ksp.donut.uca.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksp.donut.uca.R;
import com.ksp.donut.uca.dm.DMDetails;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends Fragment {
    FirebaseFirestore mDb;
    public String TAG = Chat.class.getSimpleName();
    private String peer_no;
    private String peer_name;


    private ArrayList<ChatDetails> upcoming;
    ChatAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_layout, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String my_no = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
        String my_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        MessagesListAdapter<Message> adapter1 = new MessagesListAdapter<>(my_no, null);
        MessagesList messages = view.findViewById(R.id.messagesList);
        messages.setAdapter(adapter1);

        mDb = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            Log.d(TAG, "arguments" + getArguments().getString("no"));
            peer_no = getArguments().getString("no");
            peer_name = getArguments().getString("name");
        }

        EditText editText = view.findViewById(R.id.chat_text);

        String dpath = my_no.compareTo(peer_no) >= 0 ? my_no + "_" + peer_no : peer_no + "_" + my_no;

        mDb.collection("chats").document(dpath)
                .collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                            Log.d(TAG, "Got from online db, with size: " + task.getResult().size());
                            adapter1.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Author author = new Author(String.valueOf(document.getData().get("sender")), String.valueOf(document.get("sender")), null);
                                adapter1.addToStart(new Message("456", String.valueOf(document.getData().get("msg")), author, new Date()), false);
                            }
                        }
                    }
                });

        mDb.collection("chats").document(dpath).collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    Author author = new Author(String.valueOf(dc.getDocument().getData().get("sender")), String.valueOf(dc.getDocument().get("sender")), null);
                                    adapter1.addToStart(new Message("456", String.valueOf(dc.getDocument().getData().get("msg")), author, new Date()), true);
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                            break;
                        }

                    }
                });

        view.findViewById(R.id.chat_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> chat = new HashMap<>();
                chat.put("msg", editText.getText().toString());
                chat.put("timestamp", new Date());
                chat.put("read", false);
                chat.put("sender", my_no);
                chat.put("receiver",peer_no);

                Map<String, Object> array_map = new HashMap<>();
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(my_no);
                arrayList.add(peer_no);
                array_map.put("numbers", arrayList);


                mDb.collection("chats").document(dpath).set(array_map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                               }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


                mDb.collection("chats").document(dpath).collection("messages").document()
                        .set(chat)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                editText.setText("");
                                editText.clearFocus();
                                /*Author author = new Author(my_no, my_name, null);
                                adapter1.addToStart(new Message("456", editText.getText().toString(), author, new Date()), false);
                                */
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

            }
        });
    }


}
