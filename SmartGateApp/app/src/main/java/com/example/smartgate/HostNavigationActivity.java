package com.example.smartgate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.smartgate.dataObject.User;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HostNavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersReference;
    private User user;

    private DrawerLayout drawer;
    private NavController navController;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_navigation);

        usersReference= FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        drawer = findViewById(R.id.nav_main);
        navigationView = findViewById(R.id.nav_view);

        setUserNameHeader();



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.MenuHome_menu,R.id.Homepage_menu, R.id.Driver_menu)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    finish();
                }
                boolean handled = NavigationUI.onNavDestinationSelected(menuItem, navController);
                if (handled) {
                    ViewParent parent = navigationView.getParent();
                    if (parent instanceof DrawerLayout) {
                        ((DrawerLayout) parent).closeDrawer(navigationView);
                    }
                }

                return handled;            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, drawer);

    }

    private void setUserNameHeader () {

        new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
            @Override
            public void DataIsLoaded(User userHelper, String key) {
                user = userHelper;
                String name = user.getName();
                String txt = getString(R.string.Hello);

                navigationView = findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView navUsername = (TextView) headerView.findViewById(R.id.text_view_header);
                navUsername.setText(txt+" " +name);
            }
        });


    }

}
