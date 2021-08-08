package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgate.dataObject.User;
import com.example.smartgate.dialogs.AdminDialog;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;


public class MenuHome extends AppCompatActivity implements View.OnClickListener, AdminDialog.ExampleDialogListener {

    private ImageView mainScreenI, driversDetailsVerificationI, infoForManagerI, logOutI;
    private LinearLayout textsplash, menus;
    private Animation frombottom;
    private User user;
    private String adminCode,codeFromDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);


        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        menus = (LinearLayout) findViewById(R.id.menus);

       //setPlaceName();

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
            Intent loginActivity = new Intent(getApplicationContext(), Login.class);
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
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
//        if (infoForManagerI == v) {
//            openDialog();
//
//            new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
//                @Override
//                public void DataIsLoaded(User userHelper, String key) {
//                    User adminUser = (User) userHelper;
//                    adminCode = adminUser.getAdminCode();
//                }
//            });
//
//            if(adminCode == codeFromDialog)
//            {
//                Intent intent = new Intent(this, SearchActivity.class);
//                startActivity(intent);
//            }
//            else
//            {
//                Toast.makeText(this, " Invalid code", Toast.LENGTH_LONG).show();
//                finish();
//                return;
//            }
//
//        }

    }

    public void openDialog() {
        AdminDialog adminDialog = new AdminDialog();
        adminDialog.show(getSupportFragmentManager(), "admin dialog");
    }

    @Override
    public void applyTexts(String code) {
        //adminCode.setText(code);  (admin --> textView)
        codeFromDialog = code;
    }

/*
    private void setPlaceName() {

        new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
            @Override
            public void DataIsLoaded(User placeUser, String key) {
                String name = placeUser.getName();
                String helloTxt = getString(R.string.Hello);
                String placeTxt = getString(R.string.Company);
            }
        });
    }

 */
}