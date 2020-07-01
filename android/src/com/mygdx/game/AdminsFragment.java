package com.mygdx.game;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminsFragment extends Fragment {

    private RecyclerView adminList;
    private DatabaseReference databaseReference;
    private DatabaseReference userDatabase;
    private FirebaseUser currentUser;

    private View view;

    public AdminsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admins, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        adminList = view.findViewById(R.id.adminList_frag);
        adminList.setHasFixedSize(true);
        adminList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, final Users model, int position) {
                final String userId = getRef(position).getKey();
                if (model.getRole().equals("admin")) {

                    viewHolder.setName(model.getName());
                    viewHolder.setStatus(model.getStatus());

                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!currentUser.getUid().equals(userId)) {
                                Intent messageIntent = new Intent(getContext(), MessageActivity.class);
                                messageIntent.putExtra("user_id", userId);
                                messageIntent.putExtra("user_name", model.getName());
                                startActivity(messageIntent);
                            }
                        }
                    });

                    //
                    userDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild("online")) {
                                String userOnline = dataSnapshot.child("online").getValue().toString();
                                viewHolder.setUserOnline(userOnline);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    //
                } else {
                    // set height to null
                    viewHolder.setLayout();
                }

            }
        };

        adminList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View view;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setName(String name) {
            TextView userNameView = view.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setStatus(String status) {
            TextView statusView = view.findViewById(R.id.user_single_status);
            statusView.setText(status);
        }

        public void setLayout() {
            RelativeLayout rl = view.findViewById(R.id.user_single_);
            rl.getLayoutParams().height = 0;
            rl.getLayoutParams().width = 0;

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
