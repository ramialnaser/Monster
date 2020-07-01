package com.mygdx.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersFragment extends Fragment {

    private RecyclerView usersList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private DatabaseReference roleRef;
    private DatabaseReference userDatabase;

    private View view;

    public UsersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        roleRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("role");

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        usersList = view.findViewById(R.id.usersList_frag);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(getContext()));

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
                if (model.getRole().equals("user")) {

                    viewHolder.setName(model.getName());
                    viewHolder.setStatus(model.getStatus());

                    roleRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().equals("admin")) {
                                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CharSequence options[] = new CharSequence[]{"Change Score", "Open Profile", "Send A Message"};
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Choose an option:");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {
                                                    Intent changeScoreIntent = new Intent(getContext(), ChangeScoreActivity.class);
                                                    changeScoreIntent.putExtra("user_id", userId);
                                                    changeScoreIntent.putExtra("user_name", model.getName());
                                                    startActivity(changeScoreIntent);


                                                }
                                                if (which == 1) {
                                                    Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", userId);
                                                    startActivity(profileIntent);
                                                }
                                                if (which == 2) {
                                                    Intent messageIntent = new Intent(getContext(), MessageActivity.class);
                                                    messageIntent.putExtra("user_id", userId);
                                                    messageIntent.putExtra("user_name", model.getName());
                                                    startActivity(messageIntent);
                                                }

                                            }

                                        });
                                        if (!currentUser.getUid().equals(userId)) {
                                            builder.show();
                                        }

                                    }
                                });

                            }
                            if (dataSnapshot.getValue().equals("user")) {
                                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send A Message"};
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Choose an option:");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {

                                                    Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", userId);
                                                    startActivity(profileIntent);

                                                }
                                                if (which == 1) {
                                                    Intent messageIntent = new Intent(getContext(), MessageActivity.class);
                                                    messageIntent.putExtra("user_id", userId);
                                                    messageIntent.putExtra("user_name", model.getName());
                                                    startActivity(messageIntent);
                                                }

                                            }

                                        });
                                        if (!currentUser.getUid().equals(userId)) {
                                            builder.show();
                                        }

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

        usersList.setAdapter(firebaseRecyclerAdapter);

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
