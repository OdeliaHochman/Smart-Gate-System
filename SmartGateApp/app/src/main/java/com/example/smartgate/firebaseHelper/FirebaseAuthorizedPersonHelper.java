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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthorizedPersonHelper implements Serializable {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<AuthorizedPerson> authorizedPeopleList = new ArrayList<>();
    private List<AuthorizedPerson> authorizedPersonIDNumber = new ArrayList<>();



    public interface DataStatus
    {
        void DataIsLoaded(List<AuthorizedPerson> authorizedPeopleList,List<String> keys);
        void AuthPersonDataLoaded(AuthorizedPerson authorizedPerson);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();


    }

    public FirebaseAuthorizedPersonHelper()
    {

        mDatabase = FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference("Places");
    }

    public void readAuthPersonByIDNumber(final List<String> IDs,String placeName, final DataStatus dataStatus)
    {
        authorizedPersonIDNumber.clear();
        for(int i=0; i<IDs.size(); i++)
        {
            mReference.child(placeName).child("Authorized People").child(IDs.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AuthorizedPerson authorizedPerson= dataSnapshot.getValue(AuthorizedPerson.class);
                    authorizedPersonIDNumber.add(authorizedPerson);
                    if (IDs.size()== authorizedPersonIDNumber.size())
                        dataStatus.DataIsLoaded(authorizedPersonIDNumber,IDs);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    public void readOneAuthPerson(String ID,String placeName, final DataStatus dataStatus)
    {
        mReference.child(placeName).child("Authorized People").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                AuthorizedPerson authorizedPerson = dataSnapshot.getValue(AuthorizedPerson.class);
                dataStatus.AuthPersonDataLoaded(authorizedPerson);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void readAuthPeople(String placeName,final DataStatus dataStatus)
    {
        mReference.child(placeName).child("Authorized People").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                authorizedPeopleList.clear();
                User userPre = dataSnapshot.getValue(User.class);
                System.out.println(userPre);

                List<String> keys = new ArrayList<>();

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {

                    keys.add(keyNode.getKey());
                    AuthorizedPerson authorizedPerson = keyNode.getValue(AuthorizedPerson.class);
                    authorizedPeopleList.add(authorizedPerson);
                }
                dataStatus.DataIsLoaded(authorizedPeopleList,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addAuthPerson(AuthorizedPerson authorizedPerson,String placeName, final DataStatus dataStatus)
    {
        String id = authorizedPerson.getIDNumber();
        mReference.child(placeName).child("Authorized People").child(id).setValue(authorizedPerson).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }
    public void updateAuthPerson(String key,String placeName, AuthorizedPerson authorizedPerson, final DataStatus dataStatus)
    {
        mReference.child(placeName).child("Authorized People").child(key).setValue(authorizedPerson).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteAuthPerson(String key ,String placeName, final DataStatus dataStatus)
    {
        mReference.child(placeName).child("Authorized People").child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }


}
