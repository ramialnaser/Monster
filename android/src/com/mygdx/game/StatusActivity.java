package com.mygdx.game;


import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Button saveStatus;
    private TextInputLayout newStatus;
    private Toolbar toolbar;

    //Database
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        saveStatus = findViewById(R.id.status_newStatusBtn);
        newStatus = findViewById(R.id.status_newStatus);
        toolbar = findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.keepSynced(true);

        String status_value = getIntent().getStringExtra("status_value");
        newStatus.getEditText().setText(status_value);
        saveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // progress
                progressDialog = new ProgressDialog(StatusActivity.this);
                progressDialog.setTitle("Saving Changes");
                progressDialog.setMessage("Please Wait while we save the changes");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String statusMsg = newStatus.getEditText().getText().toString();
                databaseReference.child("status").setValue(statusMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "There was some error in saving changes", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }


        });
    }


}
