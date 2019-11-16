package com.ksp.donut.uca.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksp.donut.uca.R;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat extends Fragment {
    FirebaseFirestore mDb;
    public String TAG = Chat.class.getSimpleName();
    private String sender_no = "9845359774";
    private String sender_name = "Yash";
    private String receiver_no;
    private String receiver_name;


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
        MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(sender_no, null);
        MessagesList messagesList = view.findViewById(R.id.messagesList);
        messagesList.setAdapter(adapter);

        mDb = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            Log.d(TAG, "arguments" + getArguments().getString("no"));
            receiver_no = getArguments().getString("no");
            receiver_name = getArguments().getString("name");
        }

        EditText editText = view.findViewById(R.id.chat_text);

        String dpath = sender_no.compareTo(receiver_no) >= 0 ? sender_no + "_" + receiver_no : receiver_no + "_" + sender_no;
        view.findViewById(R.id.chat_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> chat = new HashMap<>();
                chat.put("msg", editText.getText().toString());
                chat.put("timestamp", new Date());
                chat.put("read", false);

                Map<String, Object> array_map = new HashMap<>();
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(sender_no);
                arrayList.add(receiver_no);

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
                                Author author = new Author(sender_no, sender_name, null);
                                adapter.addToStart(new Message("456", editText.getText().toString(), author, new Date()), false);
                                editText.setText("");
                                editText.clearFocus();
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
