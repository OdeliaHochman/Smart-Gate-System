package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgate.dataObject.User;
import com.example.smartgate.dialogs.AdminDialog;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;


public class MenuHome extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainScreenI, driversDetailsVerificationI, infoForManagerI, logOutI;
    private LinearLayout textsplash, menus;
    private Animation frombottom;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);


        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        menus = (LinearLayout) findViewById(R.id.menus);


        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(400);
        menus.startAnimation(frombottom);

        mainScreenI = (ImageView) findViewById(R.id.mainScreenImage);
        mainScreenI.setOnClickListener(this);

        infoForManagerI = (ImageView) findViewById(R.id.infoForManagerImage);
        infoForManagerI.setOnClickListener(this);

        driversDetailsVerificationI = (ImageView) findViewById(R.id.driversDVImage);
        driversDetailsVerificationI.setOnClickListener(this);

        logOutI = (ImageView) findViewById(R.id.logOutImage);
        logOutI.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (logOutI == v) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }

        if (mainScreenI == v) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (driversDetailsVerificationI == v) {
            Intent intent = new Intent(this, DriversDetailsVerification.class);
            startActivity(intent);
        }

        if (infoForManagerI == v) {
            openDialog();
        }

    }

    public void openDialog() {
        AdminDialog adminDialog = new AdminDialog();
        adminDialog.show(getSupportFragmentManager(), "admin dialog");
    }

//    @Override
//    public void applyTexts(String code,boolean flag) {
//        //adminCode.setText(code);  (admin --> textView)
//        codeFromDialog = code;
//
//    }



}