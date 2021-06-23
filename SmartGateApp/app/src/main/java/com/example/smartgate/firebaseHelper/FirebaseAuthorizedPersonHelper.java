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
    private List<AuthorizedPerson> authorizedPersonLPNumber = new ArrayList<>();



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
        mReference=mDatabase.getReference("Places");  ////////////////////////////**************************************
    }

    public void readAuthPersonByLPNumber(final List<String> barcodes, final DataStatus dataStatus)
    {
        authorizedPersonLPNumber.clear();
        for(int i=0; i<barcodes.size(); i++)
        {
            mReference.child(barcodes.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AuthorizedPerson authorizedPerson= dataSnapshot.getValue(AuthorizedPerson.class);
                    authorizedPersonLPNumber.add(authorizedPerson);
                    if (barcodes.size()== authorizedPersonLPNumber.size())
                        dataStatus.DataIsLoaded(authorizedPersonLPNumber,barcodes);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    public void readOneAuthPerson(String barcode, final DataStatus dataStatus)
    {
        mReference.child(barcode).addListenerForSingleValueEvent(new ValueEventListener() {
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


    public void readAuthPeople(final DataStatus dataStatus)
    {
        mReference.addValueEventListener(new ValueEventListener() {

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

    public void addAuthPerson(AuthorizedPerson authorizedPerson, final DataStatus dataStatus)
    {
        String id = authorizedPerson.getIDNumber();
        mReference.child(id).setValue(authorizedPerson).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }
    public void updateAuthPerson(String key, AuthorizedPerson authorizedPerson, final DataStatus dataStatus)
    {
        mReference.child(key).setValue(authorizedPerson).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteAuthPerson(String key , final DataStatus dataStatus)
    {
        mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }


}
