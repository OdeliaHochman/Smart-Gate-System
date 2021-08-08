package com.example.smartgate;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.smartgate.firebaseHelper.FirebasePlacesHelper;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddAuthorizedPersonActivity extends AppCompatActivity {

    private Button btnSave;

    private EditText firstName;
    private EditText lastName;
    private EditText LPNumber;
    private EditText IDNumber;
    private EditText employeeNumber;
    private EditText urlImage;
    private FirebaseDatabase firebaseDatabase;
    private ProgressBar mProgressBar;
    private boolean isAdd=true; // if this add new or edit
    private String placeName;
    private User adminUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_authorized_person);
        firebaseDatabase = FirebaseDatabase.getInstance();
        final String id_number = getIntent().getStringExtra("ID Number");

        LPNumber = findViewById(R.id.LPNumber_editTxt);
        employeeNumber = findViewById(R.id.employeeNumber_editTxt);
        IDNumber = findViewById(R.id.IDNumber_editTxt);
        firstName = findViewById(R.id.firstName_editTxt);
        urlImage = findViewById(R.id.image_editTxt);
        lastName = findViewById(R.id.lastName_editTxt);
        btnSave = findViewById(R.id.save_button);
        mProgressBar= findViewById(R.id.progressBar_add_authpersonID);
        setPlaceName();
        //placeName = "Place1";

        if (id_number != null) { // if you get from update page
            isAdd=false;
            btnSave.setText("Update"); // change the txt button
            setDetails(id_number);
            IDNumber.setFocusable(false); // unable change the ID Number

        }



            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty()) // if one of the text view is empty
                        return;


                    final AuthorizedPerson authPerson = new AuthorizedPerson();
                    authPerson.setLPNumber(LPNumber.getText().toString());
                    authPerson.setEmployeeNumber(employeeNumber.getText().toString());
                    authPerson.setFirstName(firstName.getText().toString());
                    authPerson.setLastName(lastName.getText().toString());
                    authPerson.setUrlImage(urlImage.getText().toString());

                    if (isAdd) {

                        new FirebasePlacesHelper().addAuthPersonOfPlace(authPerson,placeName, new FirebasePlacesHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<String> keys) {

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
                        new FirebaseAuthorizedPersonHelper().addAuthPerson(authPerson,placeName, new FirebaseAuthorizedPersonHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<AuthorizedPerson> authorizedPeopleList, List<String> keys) {
                            }

                            @Override
                            public void AuthPersonDataLoaded(AuthorizedPerson authorizedPerson) {

                            }

                            @Override
                            public void DataIsInserted() {

                                Toast.makeText(AddAuthorizedPersonActivity.this, "Added successfully", Toast.LENGTH_LONG).show();
                                finish();
                                return;

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });
                    } else { // update
                        String id_new = IDNumber.getText().toString();
                        new FirebaseAuthorizedPersonHelper().updateAuthPerson(id_new,placeName, authPerson, new FirebaseAuthorizedPersonHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<AuthorizedPerson> authorizedPersonList, List<String> keys) {

                            }

                            @Override
                            public void AuthPersonDataLoaded(AuthorizedPerson authorizedPerson) {

                            }

                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {
                                Toast.makeText(AddAuthorizedPersonActivity.this, "Updated successfully", Toast.LENGTH_LONG).show();
                                finish();
                                return;
                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });
                    }
                }
            });
        }



    private void setDetails (String id_number) {

        inProgress(true);

        DatabaseReference reference = firebaseDatabase.getReference("Places").child(placeName).child("Authorized People").child(id_number);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AuthorizedPerson authorizedPerson = dataSnapshot.getValue(AuthorizedPerson.class);

                LPNumber.setText(authorizedPerson.getLPNumber());
                employeeNumber.setText(authorizedPerson.getEmployeeNumber());
                IDNumber.setText(authorizedPerson.getIDNumber());
                firstName.setText(authorizedPerson.getFirstName());
                urlImage.setText(authorizedPerson.getUrlImage());
                lastName.setText(authorizedPerson.getLastName());
                inProgress(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void  inProgress ( boolean flag) {

        if(flag){

            mProgressBar.setVisibility(View.VISIBLE);
            LPNumber.setEnabled(false);
            employeeNumber.setEnabled(false);
            IDNumber.setEnabled(false);
            firstName.setEnabled(false);
            urlImage.setEnabled(false);
            lastName.setEnabled(false);
        }

        else {

            mProgressBar.setVisibility(View.GONE);
            LPNumber.setEnabled(true);
            employeeNumber.setEnabled(true);
            IDNumber.setEnabled(true);
            firstName.setEnabled(true);
            urlImage.setEnabled(true);
            lastName.setEnabled(true);        }
    }

    private  boolean isEmpty () {
        if (TextUtils.isEmpty(LPNumber.getText().toString())) {
            Toast.makeText(this, "LP was not typed", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(employeeNumber.getText().toString())) {
            Toast.makeText(this, "Employee number was not typed", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(IDNumber.getText().toString())) {
            Toast.makeText(this, "ID number was not typed", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            Toast.makeText(this, "First name was not typed", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(urlImage.getText().toString())) {
            Toast.makeText(this, "Url image was not typed", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(lastName.getText().toString())) {
            Toast.makeText(this, "Last Name was not typed", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
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
