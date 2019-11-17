package com.ksp.donut.uca.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private int count=0;


    private ArrayList<ChatDetails> upcoming;
    ChatAdapter mAdapter;
    boolean mSecure;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_layout, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton ib = view.findViewById(R.id.add_secure);
          mSecure=false;
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSecure)
                {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getContext(),"Secure Mode Off",Toast.LENGTH_LONG).show();
                    mSecure=false;
                }
                else {
                    v.setBackgroundColor(Color.CYAN);
                    Toast.makeText(getContext(),"Secure Mode On",Toast.LENGTH_LONG).show();
                    mSecure=true;
                }
            }
        });

        String my_no = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
        String my_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        TextView read = view.findViewById(R.id.read_receipt);
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
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                            Log.d(TAG, "Got from online db, with size: " + task.getResult().size());
                            adapter1.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp timestamp = (Timestamp) document.getData().get("timestamp");
                                Timestamp readtime = (Timestamp) document.getData().get("read_time");

                                if(timestamp!=null)
                                {
                                    Date then = timestamp.toDate();
                                    Date now=new Date();
                                    long diff = now.getTime()-then.getTime();
                                    Log.d(TAG,String.valueOf(diff));
                                    if(document.getData().get("secure")!=null && (boolean)document.getData().get("secure") && readtime!=null)
                                    {

                                        Date read_time = readtime.toDate();
                                        long read_diff=now.getTime()-read_time.getTime();
                                        if(read_diff>60*1000)
                                        {
                                            continue;
                                        }
                                    }
                                    if(diff>60*1000)
                                    {
                                        continue;
                                    }
                                }
                                if(document.getData().get("receiver").equals(my_no) && getView()!=null)
                                {
                                    markRead(my_no,document.getId());
                                }
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
                                    Timestamp timestamp = (Timestamp) dc.getDocument().getData().get("timestamp");
                                    Timestamp readtime = (Timestamp) dc.getDocument().getData().get("read_time");

                                    if(timestamp!=null)
                                    {
                                        Date then = timestamp.toDate();
                                        Date now=new Date();
                                        long diff = now.getTime()-then.getTime();
                                        Log.d(TAG,String.valueOf(diff));
                                        if(diff>600*1000)
                                        {
                                            continue;
                                        }
                                        if(dc.getDocument().getData().get("secure")!=null && (boolean)dc.getDocument().getData().get("secure") && readtime!=null)
                                        {
                                            Date read_time = readtime.toDate();
                                            long read_diff=now.getTime()-read_time.getTime();
                                            if(read_diff>60*1000)
                                            {
                                                continue;
                                            }
                                        }
                                    }
                                    if(dc.getDocument().getData().get("receiver").equals(my_no) && getView()!=null)
                                    {
                                        markRead(my_no,dc.getDocument().getId());
                                        if(dc.getDocument().getData().get("secure")!= null && (boolean)dc.getDocument().getData().get("secure"))
                                        {
                                            continue;
                                        }
                                    }

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
                chat.put("read_time", null);
                chat.put("sender", my_no);
                chat.put("receiver",peer_no);
                chat.put("secure",mSecure);

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
                                //Change receipt
                                read.setText("Delivered");
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

        final DocumentReference docRef =  mDb.collection("chats").document(dpath).collection("messages").document();
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        mDb.collection("chats").document(dpath).collection("messages")
                .whereEqualTo("sender", my_no)
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
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    if((boolean)(dc.getDocument().getData().get("read"))==true)
                                    {
                                        read.setText("Read");
                                    }
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });
    }

    void markRead(String my_no,String id)
    {
        String dpath = my_no.compareTo(peer_no) >= 0 ? my_no + "_" + peer_no : peer_no + "_" + my_no;
        mDb.collection("chats").document(dpath).collection("messages").document(id)
                .update("read", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        mDb.collection("chats").document(dpath).collection("messages").document(id)
                .update("read_time", new Date())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }




}
