package com.mygdx.game;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatFragment extends Fragment {

    private RecyclerView convList;

    private DatabaseReference convDatabase;
    private DatabaseReference msgDatabase;
    private DatabaseReference userDatabase;

    private FirebaseAuth mAuth;

    private String currentUser;

    private View view;

    public ChatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        convList = view.findViewById(R.id.chatfrag_List);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser().getUid();

        convDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(currentUser);

        convDatabase.keepSynced(true);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        msgDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUser);
        userDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        convList.setHasFixedSize(true);
        convList.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query convQuery = convDatabase.orderByChild("time");

        FirebaseRecyclerAdapter<Conversation, ConvViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<Conversation, ConvViewHolder>(
                Conversation.class,
                R.layout.users_single_layout,
                ConvViewHolder.class,
                convQuery
        ) {
            @Override
            protected void populateViewHolder(final ConvViewHolder convViewHolder, final Conversation conv, int i) {
                final String list_user_id = getRef(i).getKey();

                Query lastMessageQuery = msgDatabase.child(list_user_id).limitToLast(1);

                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        try {


                             String message = dataSnapshot.child("message").getValue().toString();
                            convViewHolder.setMessage(message);
                        } catch (Exception ex) {
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                userDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();

                        if (dataSnapshot.hasChild("online")) {
                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            convViewHolder.setUserOnline(userOnline);

                        }
                        convViewHolder.setName(userName);


                        convViewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent chatIntent = new Intent(getContext(), MessageActivity.class);
                                chatIntent.putExtra("user_id", list_user_id);
                                chatIntent.putExtra("user_name", userName);
                                startActivity(chatIntent);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
        convList.setAdapter(firebaseConvAdapter);
    }

    public static class ConvViewHolder extends RecyclerView.ViewHolder {

        View view;

        public ConvViewHolder(View itemView) {
            super(itemView);

            view = itemView;

        }

        public void setMessage(String message) {

            TextView userStatusView = view.findViewById(R.id.user_single_status);
            userStatusView.setText(message);

            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);


        }

        public void setName(String name) {

            TextView userNameView = view.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }


        public void setUserOnline(String onlineStatus) {
            ImageView onlineImage = view.findViewById(R.id.user_single_onoff);

            if (onlineStatus.equals("true")) {
                onlineImage.setVisibility(View.VISIBLE);
            } else {
                onlineImage.setVisibility(View.INVISIBLE);

            }

        }


    }

}
