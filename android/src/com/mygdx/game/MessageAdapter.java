package com.mygdx.game;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_message_layout, parent, false);
        return new MessageViewHolder(view);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMessage;
        public TextView receiverMessage;

        public MessageViewHolder(View view) {
            super(view);
            mAuth = FirebaseAuth.getInstance();
            senderMessage = view.findViewById(R.id.message_senderMessage);
            receiverMessage = view.findViewById(R.id.message_receiverMessage);

        }
    }
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        String currentUser = mAuth.getCurrentUser().getUid();
        Messages msg = mMessageList.get(i);

        String from_user = msg.getFrom();
        if (from_user.equals(currentUser)){
            viewHolder.receiverMessage.setVisibility(View.INVISIBLE);
            viewHolder.senderMessage.setVisibility(View.VISIBLE);
            viewHolder.senderMessage.setText(msg.getMessage());
        }else {
            viewHolder.receiverMessage.setVisibility(View.VISIBLE);
            viewHolder.senderMessage.setVisibility(View.INVISIBLE);
            viewHolder.receiverMessage.setText(msg.getMessage());
        }
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
