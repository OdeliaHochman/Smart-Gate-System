package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.smartgate.dialogs.FailsDialog_Main;
import com.example.smartgate.dialogs.SuccessDialog;

public class MainActivity extends AppCompatActivity {

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


        v_image.setImageDrawable(getResources().getDrawable(R.drawable.checked));
        x_image.setImageDrawable(getResources().getDrawable(R.drawable.close));

        faceImage = (ImageView)findViewById(R.id.face_image);
        lpImage = (ImageView)findViewById(R.id.lp_image);
        vx_faceImage = (ImageView)findViewById(R.id.vx_face);
        vx_lpImage = (ImageView)findViewById(R.id.vx_pl);


        if(vx_faceImage.equals(v_image) && vx_lpImage.equals(v_image))
        {
            openSuccessDialog();
        }
        else
        {
            openFailsDialog();
        }

    }


    public void openSuccessDialog()
    {
        SuccessDialog successDialogDDV = new SuccessDialog();
        successDialogDDV.show(getSupportFragmentManager(), "success dialog");
    }

    public void openFailsDialog()
    {
        FailsDialog_Main failsDialogMain = new FailsDialog_Main();
        failsDialogMain.show(getSupportFragmentManager(), "fails dialog");
    }
}