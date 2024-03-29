package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgate.dataObject.AuthorizedPerson;
import com.example.smartgate.dataObject.User;
import com.example.smartgate.firebaseHelper.FirebaseAuthorizedPersonHelper;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UpdateAuthorizedPersonActivity extends AppCompatActivity {

    private Button btnUpdate,updateVideosBtn;

    private EditText IDNumber;
    private EditText firstName;
    private EditText lastName;
    private EditText employeeNumber;
    private EditText urlImage;
    private EditText LPNumber;

    private FirebaseDatabase firebaseDatabase;
    private ProgressBar mProgressBar;
    private boolean isAdd=true; // if this add new or edit
    private AuthorizedPerson authorizedPerson;
    private boolean update=false;
    private String placeName,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_authorized_person);
        firebaseDatabase = FirebaseDatabase.getInstance();
        id = getIntent().getStringExtra("ID Number");
        placeName = getIntent().getStringExtra("Place Name");

        IDNumber = findViewById(R.id.updateIDNumber_editTxt);
        firstName = findViewById(R.id.updateFirstName_editTxt);
        lastName = findViewById(R.id.updateLastName_editTxt);
        employeeNumber = findViewById(R.id.updateEmployeeNumber_editTxt);
        urlImage = findViewById(R.id.Updateimage_editTxt);
        LPNumber = findViewById(R.id.updateLPNumber_editTxt);
        btnUpdate = findViewById(R.id.saveUpdate_button);
        mProgressBar= findViewById(R.id.progressBar_update_authpersonID);
        updateVideosBtn = findViewById(R.id.updateVideoBtn);
        placeName = "Place1";

        updateVideosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateAuthorizedPersonActivity.this, UpdateVideoActivity.class);
                intent.putExtra("ID Number",id);
                intent.putExtra("Place Name",placeName);
                startActivity(intent);
                finish();
            }
        });



        if (id != null) { // if you get from update page
            isAdd=false;
            setDetails(id);
            IDNumber.setFocusable(false); // unable change the ID Number

        }



            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    authorizedPerson = new AuthorizedPerson();
                    authorizedPerson.setFirstName(firstName.getText().toString());
                    authorizedPerson.setLastName(lastName.getText().toString());
                    authorizedPerson.setEmployeeNumber(employeeNumber.getText().toString());
                    authorizedPerson.setUrlImage(urlImage.getText().toString());
                    authorizedPerson.setLPNumber(LPNumber.getText().toString());
                    authorizedPerson.setIDNumber(IDNumber.getText().toString());


                    String id_new = IDNumber.getText().toString();
                    new FirebaseAuthorizedPersonHelper().updateAuthPerson(id_new,placeName, authorizedPerson, new FirebaseAuthorizedPersonHelper.DataStatus() {
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
                            Toast.makeText(UpdateAuthorizedPersonActivity.this, " Updated successfully", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        @Override
                        public void DataIsDeleted() {



                        }
                    });
                }
            });
        }





    private void setDetails (String id) {

        DatabaseReference reference = firebaseDatabase.getReference("Places").child(placeName).child("Authorized People").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AuthorizedPerson a = dataSnapshot.getValue(AuthorizedPerson.class);

                IDNumber.setText(a.getIDNumber());
                firstName.setText(a.getFirstName());
                lastName.setText(a.getLastName());
                employeeNumber.setText(a.getEmployeeNumber());
                urlImage.setText(a.getUrlImage());
                LPNumber.setText(a.getLPNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}