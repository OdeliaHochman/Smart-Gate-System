package com.example.smartgate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.smartgate.dataObject.ModelVideo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;



public class UpdateVideoActivity extends AppCompatActivity {


    private EditText titleEt;
    private VideoView videoView;
    private Button uploadVideoBtn;
    private FloatingActionButton pickVideoFab;
    private static final int CAMERA_REQUEST_CODE =102;
    private String[] cameraPermissions;
    private Uri videoUri = null;
    private ProgressDialog progressDialog;
    private ActivityResultLauncher<Intent> resultLauncher;
    private ModelVideo modelVideo;
    private String id_number,placeName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_video);
        id_number = getIntent().getStringExtra("ID Number");
        placeName = getIntent().getStringExtra("Place Name");


        titleEt = findViewById(R.id.titleEt_update);
        videoView = findViewById(R.id.videoView_update);
        uploadVideoBtn = findViewById(R.id.uploadVideoBtn_update);
        pickVideoFab = findViewById(R.id.pickVideoFab_update);

        titleEt.setText(id_number);
        titleEt.setFocusable(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading Video");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            videoUri = data.getData();
                            setVideoToVideoView();
                        }
                    }
                });

        loadVideoFromFB();

        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(videoUri == null)
                {
                    Toast.makeText(UpdateVideoActivity.this,"Pick a video before you can upload..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadVideoFirebase();
                }

            }
        });

        // pick video from camera/gallery
        pickVideoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPickDialog();
            }
        });


    }

    private void uploadVideoFirebase()
    {
        progressDialog.show();

        //timestamp
        String timestamp = ""+System.currentTimeMillis();

        String filePathAndName = "Videos/" + "video_" + id_number;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // video uploaded , get url of uploaded video
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if(uriTask.isSuccessful())
                        {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("id",""+ id_number);
                            hashMap.put("timestamp",""+ timestamp);
                            hashMap.put("videoUrl",""+ downloadUri);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");
                            reference.child("video_"+id_number)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // video details added to db
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateVideoActivity.this,"Video uploaded...",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateVideoActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            DatabaseReference referenceAutoPerson = FirebaseDatabase.getInstance().getReference("Places").child(placeName)
                                    .child("Authorized People").child(id_number);
                            referenceAutoPerson.child("video_"+id_number)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // video details added to db
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateVideoActivity.this,"Video uploaded...",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateVideoActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed uploading to storage
                        progressDialog.dismiss();
                        Toast.makeText(UpdateVideoActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void videoPickDialog()
    {
        String[] options = {"Camera" , "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0)
                        {
                            //camera
                            if(!checkCameraPermission())
                            {
                                requestCameraPermission();
                            }
                            else
                            {
                                videoPickCamera();
                            }

                        }
                        else if (i == 1)
                        {
                            //gallery
                            videoPickGallery();
                        }
                    }
                })
                .show();
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }


    private void videoPickGallery()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(intent.createChooser(intent,"Select Videos"));
    }


    private void videoPickCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        resultLauncher.launch(intent);
    }


    private void setVideoToVideoView()
    {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.pause();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0)
                {
                    //check permission allowed or not
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted)
                    {
                        videoPickCamera();
                    }
                    else
                    {
                        Toast.makeText(this,"Camera & Storage permission are required",Toast.LENGTH_SHORT).show();
                    }
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void loadVideoFromFB()
    {
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");
        DatabaseReference referenceAutoPerson = FirebaseDatabase.getInstance().getReference("Places").child(placeName)
                .child("Authorized People").child(id_number).child("video_"+id_number);
        referenceAutoPerson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelVideo = datasnapshot.getValue(ModelVideo.class);
                if(modelVideo != null)
                {
                    String videoUrl = modelVideo.getVideoUrl();
                    Uri modelVideoUri = Uri.parse(videoUrl);
                    videoView.setVideoURI(modelVideoUri);
                    videoUri = modelVideoUri;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();  // go to previous activity on clicking back button on actionbar
        return super.onSupportNavigateUp();
    }
}