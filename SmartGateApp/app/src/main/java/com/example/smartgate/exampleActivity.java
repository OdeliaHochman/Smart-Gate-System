package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartgate.dialogs.SuccessDialog;

public class exampleActivity extends AppCompatActivity {

    private Drawable faceImage1,lpImage1;
    private ImageView vx_lpImage,vx_faceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            faceImage1 = ResourcesCompat.getDrawable(getResources(), R.drawable.bush, null);
            lpImage1 =ResourcesCompat.getDrawable(getResources(), R.drawable.lp_bu, null);
            vx_faceImage.setImageResource(R.drawable.checked);
            vx_lpImage.setImageResource(R.drawable.checked);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openSuccessDialog();
    }

    public void openSuccessDialog()
    {
        SuccessDialog successDialogMain = new SuccessDialog();
        successDialogMain.show(getSupportFragmentManager(), "success dialog");

    }
}