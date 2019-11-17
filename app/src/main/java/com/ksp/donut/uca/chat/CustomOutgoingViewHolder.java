package com.ksp.donut.uca.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class CustomOutgoingViewHolder extends MessagesListAdapter.OutcomingMessageViewHolder<Message> {

    public CustomOutgoingViewHolder(View itemView, Object payload) {
        super(itemView);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);


        time.setText(time.getText()+"hello");
    }
}