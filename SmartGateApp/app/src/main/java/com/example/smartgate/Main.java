package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class Main extends AppCompatActivity {

    private ImageView v_image;
    private ImageView x_image;
    private ImageView vx_faceImage;
    private ImageView vx_lpImage;
    private ImageView faceImage;
    private ImageView lpImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faceImage = (ImageView)findViewById(R.id.face_image);
        lpImage = (ImageView)findViewById(R.id.lp_image);
        vx_faceImage = (ImageView)findViewById(R.id.vx_face);
        vx_lpImage = (ImageView)findViewById(R.id.vx_pl);

    }
}