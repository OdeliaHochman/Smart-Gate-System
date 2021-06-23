package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.example.smartgate.firebaseHelper.FirebaseAuthorizedPersonHelper;
import com.example.smartgate.firebaseHelper.FirebasePlacesHelper;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private SearchView mySearchView;
    private RecyclerView mRecycler;
    private FloatingActionButton floatingButton;
    private List<AuthorizedPerson> authPeopleList;
    private User adminUser;
    private FirebaseDatabase pDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference pReference=pDatabase.getReference("Places"); //////*************************
    private  String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mySearchView = (SearchView)findViewById(R.id.searchLine);

        mRecycler = (RecyclerView)findViewById(R.id.recyclerView_products);

        floatingButton = (FloatingActionButton)findViewById(R.id.floating_button_search);
        floatingButton.setOnClickListener(this);

        new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
            @Override
            public void DataIsLoaded(User userHelper, String key) {
                adminUser = (User) userHelper;
                placeName = adminUser.getName();
                ShowAuthPeopleByIDNumbers();
            }
        });


        if(mySearchView != null){
            mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (mySearchView!= null ) {
                        Log.d("debug_search",""+newText);
                        search(newText);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v == floatingButton)
        {
            Intent intent = new Intent(this, AddAuthorizedPersonActivity.class);
            startActivity(intent);
        }
    }

    private void search(String str) {

        ArrayList<AuthorizedPerson> searchList = new ArrayList<>();
        ArrayList<String> searchKeys = new ArrayList<>();
        if (authPeopleList!= null ) {
            for (AuthorizedPerson a : authPeopleList) {
                if (a.getFirstName().toLowerCase().contains(str) || a.getLastName().toLowerCase().contains(str) || a.getLPNumber().contains(str)) {
                    searchList.add(a);
                    //searchKeys.add(a.getIDNumber());///////////////////*********************************************
                }
            }

            new RecyclerView_config().setConfig(mRecycler, this, searchList, searchKeys);

        }
    }

    private void ShowAuthPeopleByIDNumbers()
    {
        new FirebasePlacesHelper().readAuthPeopleOfPlace(placeName,new FirebasePlacesHelper.DataStatus()
        {
            @Override
            public void DataIsLoaded(List<String> barcodesList)
            {
                new FirebaseAuthorizedPersonHelper().readAuthPersonByLPNumber(barcodesList, new FirebaseAuthorizedPersonHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<AuthorizedPerson> ProductBarcode, List<String> keys) {
                        findViewById(R.id.progressBar).setVisibility(View.GONE);

                        authPeopleList=ProductBarcode;
                        new RecyclerView_config().setConfig(mRecycler,SearchActivity.this,authPeopleList,keys);

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
}
