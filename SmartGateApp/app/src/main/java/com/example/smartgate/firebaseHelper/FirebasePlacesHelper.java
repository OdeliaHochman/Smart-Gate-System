package com.example.smartgate.firebaseHelper;

import androidx.annotation.NonNull;


import com.example.smartgate.dataObject.AuthorizedPerson;
import com.example.smartgate.dataObject.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebasePlacesHelper
{
    private FirebaseDatabase cDatabase;
    private DatabaseReference cReference;
    private User user;



    public interface DataStatus
    {
        void DataIsLoaded(List<String> IDsList);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebasePlacesHelper()
    {
        cReference=FirebaseDatabase.getInstance().getReference().child("Places");
    }

    public void readAuthPeopleOfPlace(final String placeName, final DataStatus dataStatus)
    {
        cReference.child(placeName).child("Authorized People").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                List<String> authpeople_IDs = new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren())
                {
                    authpeople_IDs.add(keyNode.getValue(String.class));

                }
                dataStatus.DataIsLoaded(authpeople_IDs);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    public void addAuthPersonOfPlace(AuthorizedPerson authorizedPerson,String placeName, final DataStatus dataStatus)
    {
        String id = authorizedPerson.getIDNumber();
        cReference.child(placeName).child(id).setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }
    public void deleteAuthPerson(AuthorizedPerson a ,String placeName, final DataStatus dataStatus)
    {
        String id = a.getIDNumber();
        cReference.child(placeName).child(id).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }


}
