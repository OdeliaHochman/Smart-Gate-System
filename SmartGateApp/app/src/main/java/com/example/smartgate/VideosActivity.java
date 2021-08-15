package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VideosActivity extends AppCompatActivity {

    private FloatingActionButton addVideosBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        setTitle("Videos");

        addVideosBtn = findViewById(R.id.addVideosBtn);

        addVideosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideosActivity.this,AddVideoActivity.class));
            }
        });



    }


}
