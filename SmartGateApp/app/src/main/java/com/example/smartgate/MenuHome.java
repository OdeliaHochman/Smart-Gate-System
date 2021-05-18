package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;


public class MenuHome extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainScreenI, driversDetailsVerificationI, infoForManagerI, logOutI;
    private ImageView bgapp, clover;
    private LinearLayout textsplash, menus;
    private Animation frombottom;
    private User user;

    private TextView nameTextView, companyTextView; //companyProT , statT ,contactT ,logOutT;


    final String activity = " RepresentativeHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", activity);
        setContentView(R.layout.activity_menu_home);


        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        menus = (LinearLayout) findViewById(R.id.menus);
        nameTextView = findViewById(R.id.helloNameT);
        companyTextView = findViewById(R.id.helloSecondT);

        setCompanyName();

        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(400);
        clover.animate().alpha(0).setDuration(800).setStartDelay(700);
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
            Intent intent = new Intent(this, MainActivity.class); ///to change!!!
            startActivity(intent);
        }

    }


    private void setCompanyName() {

        new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
            @Override
            public void DataIsLoaded(User companyUser, String key) {
                String name = companyUser.getName();
                String helloTxt = getString(R.string.Hello);
                String companyTxt = getString(R.string.Company);

                nameTextView.setText(helloTxt);
                companyTextView.setText(companyTxt + " " + name);
            }
        });
    }
}