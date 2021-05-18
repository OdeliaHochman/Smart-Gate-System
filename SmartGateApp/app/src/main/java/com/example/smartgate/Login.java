package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private Button ButtonLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBar;
    final String activity = " Login";
    private FirebaseUser autoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        autoUser = firebaseAuth.getCurrentUser();

        Log.d("debug",activity);
        setContentView(R.layout.activity_login);

        EditTextEmail =  (EditText) findViewById(R.id.username_editTxt);
        EditTextPassword =  (EditText) findViewById(R.id.password_editTxt);
        ButtonLogin =  (Button) findViewById(R.id.login_button);
       // mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);


        // if the user already login go to search activity
        if(autoUser!=null ) {
            setUser();
        }


        ButtonLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v ==  ButtonLogin)
            login();
    }

    // login
    private void login (){
        if(isEmpty())
            return;
        inProgress(true);
        String email = EditTextEmail.getText().toString();
        String password = EditTextPassword.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this,"Login successfully", Toast.LENGTH_LONG).show();
                setUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                inProgress(false);
                Toast.makeText(Login.this,"Login failed"+e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    // deal with button and progressBar inProgress
    private void  inProgress ( boolean flag) {

        if(flag){

            //mProgressBar.setVisibility(View.VISIBLE);
            ButtonLogin.setEnabled(false);
        }

        else {

           // mProgressBar.setVisibility(View.GONE);
            ButtonLogin.setEnabled(true);
        }
    }

    // return if one of the text view is empty
    private  boolean isEmpty () {
        if ((TextUtils.isEmpty(EditTextEmail.getText().toString())) || (TextUtils.isEmpty(EditTextPassword.getText().toString()))) {
            Toast.makeText(this, "Incorrect / missing details", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void setUser()
    {
        inProgress(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startActivity(new Intent(Login.this, MenuHome.class));
                finish(); return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase", databaseError.getMessage());
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);

    }
}
