package com.mygdx.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private String currentUser;
    private FirebaseAuth mAuth;

    private String userId;
    private DatabaseReference rootRef;

    private ImageButton sendBtn;
    private EditText theMessage;

    private RecyclerView recyclerViewMessageList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    private static final int totalItemsToLoad = 10;
    private int currentPage = 1;


    private int itemPos = 0;
    private String lastKey = "";
    String lastPreviousKey = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        sendBtn = findViewById(R.id.message_send);
        theMessage = findViewById(R.id.message_message);

        userId = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");

        Toolbar toolbar = findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(userName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        messageAdapter = new MessageAdapter(messagesList);

        recyclerViewMessageList = findViewById(R.id.message_list);
        swipeRefreshLayout = findViewById(R.id.message_SwipeLayout);
        linearLayoutManager = new LinearLayoutManager(MessageActivity.this);

        recyclerViewMessageList.setLayoutManager(linearLayoutManager);
        recyclerViewMessageList.setAdapter(messageAdapter);

        loadMessage();

        rootRef.child("chat").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(userId)) {
                    Map map = new HashMap();
                    map.put("time", ServerValue.TIMESTAMP);

                    Map userMap = new HashMap();
                    userMap.put("chat/" + userId + "/" + currentUser, map);
                    userMap.put("chat/" + currentUser + "/" + userId, map);

                    rootRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {


                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = theMessage.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    String currentUserRef = "messages/" + currentUser + "/" + userId;
                    String userIdRef = "messages/" + userId + "/" + currentUser;

                    DatabaseReference userMsgPush = rootRef.child("messages").child(currentUser).child(userId).push();

                    String pushId = userMsgPush.getKey();

                    Map msgMap = new HashMap();
                    msgMap.put("message", message);
                    msgMap.put("time", ServerValue.TIMESTAMP);
                    msgMap.put("from", currentUser);

                    Map userMap = new HashMap();
                    userMap.put(currentUserRef + "/" + pushId, msgMap);
                    userMap.put(userIdRef + "/" + pushId, msgMap);

                    theMessage.setText("");

                    rootRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        }
                    });
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                currentPage++;
                loadExtraMessage();
            }
        });
    }

    private void loadExtraMessage() {
        DatabaseReference msgRef = rootRef.child("messages").child(currentUser).child(userId);
        Query messageQuery = msgRef.orderByKey().endAt(lastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);
               String msgKey = dataSnapshot.getKey();


                if (!lastPreviousKey.equals(msgKey)) {
                    messagesList.add(itemPos++, messages);
                } else {
                    lastPreviousKey = lastKey;
                }
                if (itemPos == 1) {
                    String msg2Key = dataSnapshot.getKey();
                    lastKey = msg2Key;

                }

                messageAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                linearLayoutManager.scrollToPositionWithOffset(10, 0);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMessage() {
        DatabaseReference msgRef = rootRef.child("messages").child(currentUser).child(userId);
        Query messageQuery = msgRef.limitToLast(currentPage * totalItemsToLoad);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);

                itemPos++;
                if (itemPos == 1) {
                    String msgKey =dataSnapshot.getKey();
                    lastKey = msgKey;
                    lastPreviousKey = msgKey;

                }
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();

                recyclerViewMessageList.scrollToPosition(messagesList.size() - 1);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}
