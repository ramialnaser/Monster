package com.mygdx.game;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button signInBtn;
    private TextInputLayout signInEmail;
    private TextInputLayout signInPassword;

    private ProgressDialog progressDialog;

    private Button reset;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        reset = findViewById(R.id.reset);
        signInEmail = findViewById(R.id.signIn_email);
        signInPassword = findViewById(R.id.signIn_password);
        signInBtn = findViewById(R.id.signIn_signIn);
        toolbar = findViewById(R.id.signIn_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SignInActivity.this, ForgetPassword.class);
                startActivity(in);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signInEmail.getEditText().getText().toString();
                String password = signInPassword.getEditText().getText().toString();


                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please Wait while we check your credentials!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    loginUser(email, password);
                }
            }
        });

    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    currentUser = mAuth.getCurrentUser().getUid();

                    userRef = rootRef.child("Users").child(currentUser);

                    userRef.child("role").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().equals("admin")) {
                                Intent mainIntent = new Intent(SignInActivity.this, AdminActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                            if (dataSnapshot.getValue().equals("user")) {
                                Intent mainIntent = new Intent(SignInActivity.this, AndroidLauncher.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    progressDialog.hide();

                    Toast.makeText(SignInActivity.this, "Cannot Sign in. Please Check the form and try again!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
