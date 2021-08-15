package com.example.smartgate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartgate.dataObject.AuthorizedPerson;
import com.example.smartgate.dataObject.ModelVideo;
import com.example.smartgate.dataObject.User;
import com.example.smartgate.firebaseHelper.FirebaseAuthorizedPersonHelper;
import com.example.smartgate.firebaseHelper.FirebasePlacesHelper;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AuthorizedPersonDetailsActivity extends AppCompatActivity {

    private TextView firstName, lastName, IDNumber, LPNumber, employeeNumber;
    private ImageView authPersonImage;
    private Button btnUpdate,btnDelete;
    private String IDNumberS;
    private FirebaseDatabase firebaseDatabase;
    private AuthorizedPerson authorizedPerson;
    private String placeName;
    private User adminUser;
    private ModelVideo modelVideo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_person_details);
        firebaseDatabase = FirebaseDatabase.getInstance();
        IDNumberS = getIntent().getStringExtra("ID Number"); // get id from listview
        setPlaceName();

        lastName = (TextView) findViewById(R.id.last_name_Adetails);
        firstName = (TextView) findViewById(R.id.first_name_Adetails);
        employeeNumber = (TextView) findViewById(R.id.employee_number_Adetails);
        IDNumber = (TextView) findViewById(R.id.id_Adetails);
        authPersonImage = (ImageView)findViewById(R.id.authperson_image_Adetails);
        LPNumber = (TextView) findViewById(R.id.lp_number_details);


        new FirebaseAuthorizedPersonHelper().readOneAuthPerson(IDNumberS,placeName,new FirebaseAuthorizedPersonHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<AuthorizedPerson> authorizedPeopleList, List<String> keys) {

            }

            @Override
            public void AuthPersonDataLoaded(AuthorizedPerson a)
            {
                authorizedPerson =a;
                lastName.setText(authorizedPerson.getLastName());
                firstName.setText(authorizedPerson.getFirstName());
                employeeNumber.setText(authorizedPerson.getEmployeeNumber());
                IDNumber.setText(authorizedPerson.getIDNumber());
                LPNumber.setText(authorizedPerson.getLPNumber());
                Picasso.get().load(authorizedPerson.getUrlImage()).into(authPersonImage);


            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


        btnUpdate = (Button)findViewById(R.id.btnUpdateProDet);
        btnDelete = (Button)findViewById(R.id.btnDeleteProDet);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorizedPersonDetailsActivity.this, UpdateAuthorizedPersonActivity.class);
                intent.putExtra("ID Number",IDNumberS);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(AuthorizedPersonDetailsActivity.this);
                builder1.setMessage("Are you sure you want to delete this person's details from the authorized people list? ");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick( final DialogInterface dialog, int id) {

                                deleteVideo(modelVideo);

                                // delete from authpeople tree
                                new FirebaseAuthorizedPersonHelper().deleteAuthPerson(IDNumberS,placeName, new FirebaseAuthorizedPersonHelper.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<AuthorizedPerson> authorizedPeopleList, List<String> keys) {

                                    }

                                    @Override
                                    public void AuthPersonDataLoaded(AuthorizedPerson authorizedPerson) {

                                    }

                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {

                                    }

                                    @Override
                                    public void DataIsDeleted() {

                                    }
                                });

                                new FirebasePlacesHelper().deleteAuthPerson(authorizedPerson,placeName, new FirebasePlacesHelper.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<String> idList) {

                                    }

                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {

                                    }

                                    @Override
                                    public void DataIsDeleted() {

                                    }
                                });

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }


        });

    }


    private void deleteVideo(ModelVideo modelVideo)  // todo: change path
    {
        String videoId = modelVideo.getId();
        String videoUrl = modelVideo.getVideoUrl();

        //delete from FB storage
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
                        databaseReference.child(videoId)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AuthorizedPersonDetailsActivity.this,"Video deleted successfuly...",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AuthorizedPersonDetailsActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthorizedPersonDetailsActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
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
