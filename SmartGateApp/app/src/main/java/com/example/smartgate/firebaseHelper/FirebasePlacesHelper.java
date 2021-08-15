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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class FirebasePlacesHelper
{
    private FirebaseDatabase cDatabase;
    private DatabaseReference cReference;
    private User user;
    private boolean flag = false;



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
                    //Map<String, Object> map = (Map<String, Object>) keyNode.getValue();
                    //authpeople_IDs.add(keyNode.getValue(String.class));
                    authpeople_IDs.add(keyNode.getKey());
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
        cReference.child(placeName).child("Authorized People").child(id).setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }
    public void deleteAuthPerson(AuthorizedPerson a ,String placeName, final DataStatus dataStatus)
    {
        String id = a.getIDNumber();
        cReference.child(placeName).child("Authorized People").child(id).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }



    public void addTimeAndDateToFB(String LPNumber,String placeName, final DataStatus dataStatus)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd-MMM-yyyy");
        String date = simpleDateFormatDate.format(calendar.getTime());
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("hh-mm-ss");
        String time = simpleDateFormatTime.format(calendar.getTime());

        cReference.child(placeName).child("Realtime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot keyNode:dataSnapshot.getChildren())
                {
                    if(keyNode.getKey().equals(date))
                    {
                        flag = true;
                        continue;
                    }

                }

                if(!flag)
                {
                    cReference.child(placeName).child("Realtime").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dataStatus.DataIsInserted();
                        }
                    });
                }

                cReference.child(placeName).child("Realtime").child(date).setValue(time).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
                cReference.child(placeName).child("Realtime").child(date).child(time).setValue(LPNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
