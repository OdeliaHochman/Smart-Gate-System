package com.example.smartgate.firebaseHelper;

import androidx.annotation.NonNull;
import com.example.smartgate.dataObject.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseUserHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String userKey;
    private User user;

    public interface DataStatusUser
    {
        void DataIsLoaded( User userHelper , String key);

    }

    public FirebaseUserHelper()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();
        mReference=mDatabase.getReference("Users").child(userKey);
    }


    public void readUser (final FirebaseUserHelper.DataStatusUser dataStatus)
    {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               user = dataSnapshot.getValue(User.class);
                dataStatus.DataIsLoaded(user,userKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
