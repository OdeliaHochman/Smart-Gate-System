package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.smartgate.dataObject.User;
import com.example.smartgate.dialogs.FailsDialog_Main;
import com.example.smartgate.dialogs.SuccessDialog;
import com.example.smartgate.firebaseHelper.FirebasePlacesHelper;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView v_image;
    private ImageView x_image;
    private ImageView vx_faceImage;
    private ImageView vx_lpImage;
    private ImageView faceImage;
    private ImageView lpImage;
    private String placeName , LPNumber;
    private User adminUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPlaceName();

//        v_image = (ImageView) findViewById(R.id.imageView); //change
//        x_image = (ImageView) findViewById(R.id.imageView);
//        v_image.setBackgroundResource(R.drawable.checked);
//        x_image.setBackgroundResource(R.drawable.close);
//        v_image.setImageDrawable(getResources().getDrawable(R.drawable.checked));
//        x_image.setImageDrawable(getResources().getDrawable(R.drawable.close));

        faceImage = (ImageView)findViewById(R.id.face_image);
        lpImage = (ImageView)findViewById(R.id.lp_image);
        vx_faceImage = (ImageView)findViewById(R.id.vx_face);
        vx_lpImage = (ImageView)findViewById(R.id.vx_pl);


//        if(vx_faceImage.equals(v_image) && vx_lpImage.equals(v_image))
//        {
//            openSuccessDialog();
//        }
//        else
//        {
//            openFailsDialog();
//        }

      //  LPNumber = We need to get the value of the license plate number from Python after image processing.

//        new FirebasePlacesHelper().addTimeAndDateToFB(LPNumber, placeName, new FirebasePlacesHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<String> list) {
//
//            }
//
//            @Override
//            public void DataIsInserted() {
//
//            }
//
//            @Override
//            public void DataIsUpdated() {
//
//            }
//
//            @Override
//            public void DataIsDeleted() {
//
//            }
//        });


    }


    public void openSuccessDialog()
    {
        SuccessDialog successDialogMain = new SuccessDialog();
        successDialogMain.show(getSupportFragmentManager(), "success dialog");

    }

    public void openFailsDialog()
    {
        FailsDialog_Main failsDialogMain = new FailsDialog_Main();
        failsDialogMain.show(getSupportFragmentManager(), "fails dialog");
    }

    private void setPlaceName() {
        new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
            @Override
            public void DataIsLoaded(User userHelper, String key) {
                adminUser = (User) userHelper;
                placeName = adminUser.getName();
            }
        });
    }
}