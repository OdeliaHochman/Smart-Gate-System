package com.example.smartgate.firebaseHelper;

import androidx.annotation.NonNull;


import com.example.smartgate.AuthorizedPerson;
import com.example.smartgate.User;
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
        void DataIsLoaded(List<String> barcodesList);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebasePlacesHelper()
    {
        //cDatabase =FirebaseDatabase.getInstance();
        //cReference=cDatabase.getReference("Companies");
        cReference=FirebaseDatabase.getInstance().getReference().child("Places"); /////////////////****************************
    }

    public void readAuthPeopleOfPlace(final String placeName, final DataStatus dataStatus)
    {
        cReference.child(placeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                List<String> barcodes = new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren())
                {
                    barcodes.add(keyNode.getValue(String.class));

                }
                dataStatus.DataIsLoaded(barcodes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    public void addAuthPersonOfPlace(AuthorizedPerson authorizedPerson, final DataStatus dataStatus)
    {
        String id = authorizedPerson.getIDNumber();
        cReference.child(authorizedPerson.getPlaceName()).child(id).setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }
    public void deleteAuthPerson(AuthorizedPerson a , final DataStatus dataStatus)
    {
        String placeName = a.getPlaceName();
        String id = a.getIDNumber();
        cReference.child(placeName).child(id).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }


}
