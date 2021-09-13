package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.smartgate.dataObject.User;
import com.example.smartgate.dialogs.FailsDialog_Main;
import com.example.smartgate.dialogs.SuccessDialog;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {

    private ImageView vx_faceImage;
    private ImageView vx_lpImage;
    private ImageView faceImage;
    private ImageView lpImage;
    private String placeName , LPNumber;
    private User adminUser;
    private Drawable v_image,x_image;
    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;
    private String clientid = "";
    private Timer myTimer;
    private TextView tvMessage;
    private String msg_new="";
    private boolean connect,subscribe,publish;
    private String urlBroker,username,password;
    private ImageView imgViewer;
    private boolean lp_flag,face_flag;
    private String lp_flag_str,face_flag_str;
    private String topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPlaceName();
        tvMessage  = (TextView) findViewById(R.id.subscribedMsg);
        msg_new=""; username=""; password="";
        urlBroker = "tcp://192.168.43.171:1883".trim();

        v_image = ResourcesCompat.getDrawable(getResources(), R.drawable.checked, null);
        x_image =ResourcesCompat.getDrawable(getResources(), R.drawable.close, null);

        faceImage = (ImageView)findViewById(R.id.face_image_main);
        lpImage = (ImageView)findViewById(R.id.lp_image_main);
        vx_faceImage = (ImageView)findViewById(R.id.vx_face_main);
        vx_lpImage = (ImageView)findViewById(R.id.vx_lp_main);

        imgViewer = (ImageView) findViewById(R.id.lp_image_main);

        TextView tvMessage = (TextView) findViewById(R.id.subscribedMsg);
        tvMessage.setMovementMethod(new ScrollingMovementMethod());           // Scroller for feedback TextView object

        //Generate unique client id for MQTT broker connection
        Random r = new Random();
        int i1 = r.nextInt(5000 - 1) + 1;
        clientid = "mqtt" + i1;


        pahoMqttClient = new PahoMqttClient();
        client = pahoMqttClient.getMqttClient(  getApplicationContext(),        // Connect to MQTT Broker
                urlBroker,
                clientid,
                username,
                password
        );

        //Create listener for MQTT messages.
        mqttCallback();

        //Create Timer to report MQTT connection status every 1 second
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ScheduleTasks();
            }

        }, 0, 1000);


        connect = connect();

        if(connect)
        {
            subscribe = subscribe();
            if(subscribe)
            {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publish = publish();

                if(publish)
                {
                    try {
                        LoadImage();
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Publish failed", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "Subscribe failed", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "Connected failed", Toast.LENGTH_SHORT).show();
        }


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // todo: set vx_face and vx_lp images according to the results from the python:




        if(face_flag_str == "True")
        {
            vx_faceImage.setImageResource(R.drawable.checked);
        }
        else
        {
            vx_faceImage.setImageResource(R.drawable.close);
        }
        vx_faceImage.setVisibility(View.VISIBLE);

        if(lp_flag)
        {
            vx_lpImage.setImageResource(R.drawable.checked);
        }
        else
        {
            vx_lpImage.setImageResource(R.drawable.close);
        }
        vx_lpImage.setVisibility(View.VISIBLE);


        if(vx_faceImage.equals(v_image) && vx_lpImage.equals(v_image))
        {
            try {
                Thread.sleep(10);
                openSuccessDialog();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                Thread.sleep(10);
                openFailsDialog();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

      //  LPNumber = We need to get the value of the license plate number from Python after image processing.

//        new FirebasePlacesHelper().addTimeAndDateToFB(LPNumber, placeName, new FirebasePlacesHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<String> list) {
//
//            }
//
//            @Override
//            public void DataIsInserted() {
//                Toast.makeText(MainActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
//                finish();
//                return;
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

    private void ScheduleTasks()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(RunScheduledTasks);
    }


    private Runnable RunScheduledTasks = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.

            //Check MQTT Connection Status
            TextView tvMessage  = (TextView) findViewById(R.id.cnxStatus);
            String msg_new="";

            if(pahoMqttClient.mqttAndroidClient.isConnected() ) {
                msg_new = "Connected\r\n";
                tvMessage.setTextColor(0xFF00FF00); //Green if connected
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            else {
                msg_new = "Disconnected\r\n";
                tvMessage.setTextColor(0xFFFF0000); //Red if not connected
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            tvMessage.setText(msg_new);
        }
    };


    // Called when a subscribed message is received
    protected void mqttCallback() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //msg("Connection lost...");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TextView tvMessage = (TextView) findViewById(R.id.subscribedMsg);

                if(topic.equals("Visitor")) {
                    topic = "Visitor";
                    changeLoadedImage();
                }
                else if(topic.equals("FaceFlag")) {
                    topic = "FaceFlag";
                    face_flag_str = message.toString();
                    //face_flag_str = message.getPayload().toString();
                }
                else if(topic.equals("LPFlag")) {
                    topic = "LPFlag";
                    LPNumber = message.toString();
                    if(LPNumber.isEmpty())
                    {
                        lp_flag = false;
                    }
                    lp_flag = true;
                }
//                else if(topic.equals("FaceFlag_F")) {
//                    topic = "FaceFlag_F";
//                    face_flag = false;
//                }
//                else if(topic.equals("LPFlag_F")) {
//                    topic = "LPFlag_F";
//                    lp_flag = false;
//                }
                else if(topic.equals("VisitorImage")) {
                    topic = "VisitorImage";
                    LoadVisitorImage(message.getPayload());
                }
                else {
                    String msg = "topic: " + topic + "\r\nMessage: " + message.toString() + "\r\n";
                    tvMessage.append( msg);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private void LoadImage()
    {
        Toast.makeText(this, "get image", Toast.LENGTH_SHORT).show();

        byte buffer[] = new byte[4096];
        File file;
        //file = new File( "@res/drawable/flowers.jpg");

        Resources res = getApplicationContext().getResources();
        Drawable myImage = ResourcesCompat.getDrawable(res, R.mipmap.logo_foreground, null);

        Bitmap bmp= BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.logo_foreground);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imgViewer.setMinimumHeight(dm.heightPixels);
        imgViewer.setMinimumWidth(dm.widthPixels);
        imgViewer.setImageBitmap(bm);
    }

    private void LoadVisitorImage(byte[] byteArray ) {
        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imgViewer.setMinimumHeight(dm.heightPixels);
        imgViewer.setMinimumWidth(dm.widthPixels);
        imgViewer.setImageBitmap(bm);
    }

    private void changeLoadedImage() {
        Resources res = getApplicationContext().getResources();
        Drawable myImage = ResourcesCompat.getDrawable(res, R.mipmap.logo_foreground, null);

        Bitmap bmp= BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.logo_foreground);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imgViewer.setMinimumHeight(dm.heightPixels);
        imgViewer.setMinimumWidth(dm.widthPixels);
        imgViewer.setImageBitmap(bm);
    }

    private boolean connect()
    {
        Random r = new Random();        //Unique Client ID for connection
        int i1 = r.nextInt(5000 - 1) + 1;
        clientid = "mqtt" + i1;

        if(pahoMqttClient.mqttAndroidClient.isConnected() ) {
            //Disconnect and Reconnect to  Broker
            try {
                //Disconnect from Broker
                pahoMqttClient.disconnect(client);
                //Connect to Broker
                client = pahoMqttClient.getMqttClient(getApplicationContext(), urlBroker, clientid, username, password);
                //Set Mqtt Message Callback
                mqttCallback();
            }
            catch (MqttException e) {
            }
        }
        else {
            //Connect to Broker
            client = pahoMqttClient.getMqttClient(getApplicationContext(), urlBroker, clientid, username, password);
            //Set Mqtt Message Callback
            mqttCallback();
        }
        return true;
    }

    private boolean subscribe()
    {
        if(!pahoMqttClient.mqttAndroidClient.isConnected() ) {
            msg_new = "Currently not connected to MQTT broker: Must be connected to subscribe to a topic\r\n";
            tvMessage.append(msg_new);
            return true;
        }

        if (!topic.isEmpty()) {
            try {
                pahoMqttClient.subscribe(client, topic, 1);
                msg_new = "Added subscription topic: " + topic + "\r\n";
                tvMessage.append(msg_new);

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private boolean publish()
    {
        if(!pahoMqttClient.mqttAndroidClient.isConnected() ) {
            msg_new = "Currently not connected to MQTT broker: Must be connected to subscribe to a topic\r\n";
            tvMessage.append(msg_new);
            return true;
        }
        if (!topic.isEmpty()) {
            try {
                pahoMqttClient.subscribe(client, topic, 1);
                msg_new = "Added subscription topic: " + topic + "\r\n";
                tvMessage.append(msg_new);

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return true;
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