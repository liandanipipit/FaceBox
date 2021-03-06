package com.pipitliandani.android.facebox;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pipitliandani.android.facebox.fragments.Administrator;
import com.pipitliandani.android.facebox.fragments.BirthdayFragment;
import com.pipitliandani.android.facebox.fragments.CloseFriends;
import com.pipitliandani.android.facebox.fragments.Division;
import com.pipitliandani.android.facebox.fragments.ListOfEmployee;
import com.pipitliandani.android.facebox.fragments.ListOfEmployeeOtherUnits;
import com.pipitliandani.android.facebox.fragments.ManagementFragment;
import com.pipitliandani.android.facebox.fragments.OtherFragment;
import com.pipitliandani.android.facebox.fragments.OtherUnitFragment;
import com.pipitliandani.android.facebox.fragments.Subsidiaries;
import com.pipitliandani.android.facebox.fragments.UBFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NavigationDrawer extends AppCompatActivity
        implements BirthdayFragment.OnFragmentInteractionListener,
        CloseFriends.OnFragmentInteractionListener,Division.OnFragmentInteractionListener,
        ListOfEmployee.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, ManagementFragment.OnFragmentInteractionListener,
        UBFragment.OnFragmentInteractionListener, OtherFragment.OnFragmentInteractionListener,
        Subsidiaries.OnFragmentInteractionListener, OtherUnitFragment.OnFragmentInteractionListener,
        ListOfEmployeeOtherUnits.OnFragmentInteractionListener , Administrator.OnFragmentInteractionListener{

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationDrawer.this, com.pipitliandani.android.facebox.Search.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Menu nav_menu = navigationView.getMenu();
//                if (firebaseAuth != null){
//                    nav_menu.findItem(R.id.Login).setVisible(false);
//                } else {
//                    nav_menu.findItem(R.id.Logout).setVisible(false);
//                    nav_menu.findItem(R.id.admin).setVisible(false);
//                    nav_menu.findItem(R.id.Login).setVisible(true);
//                }
//            }
//        });
        Menu nav_menu = navigationView.getMenu();
        if(auth.getCurrentUser() != null) {
            nav_menu.findItem(R.id.Login).setVisible(false);
        } else {
            nav_menu.findItem(R.id.Logout).setVisible(false);
            nav_menu.findItem(R.id.admin).setVisible(false);
            nav_menu.findItem(R.id.Login).setVisible(true);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new BirthdayFragment());
        fragmentTransaction.commit();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 05);
        calendar.set(Calendar.MINUTE, 00);

        NotificationScheduler.setReminder(getApplicationContext(), MyReceiver.class, 05, 00);



    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        Log.i("NavDrawer", "Back Pressed");
        Log.i("NavDrawer", fm.getBackStackEntryCount()+"");
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("NavDrawer", "Poping back stack");
            fm.popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Close")
                    .setMessage("Do you want to close this application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_navigation_drawer_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        menuHandler(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void menuHandler(int id) {
        Fragment fragment = null;

        if (id == R.id.ListOfEmployee) {
            // Handle the camera action
            fragment = new ListOfEmployee();
        } else if (id == R.id.birthdayFragment) {
            fragment = new BirthdayFragment();
        } else if (id == R.id.search) {
            Intent intent = new Intent(this, com.pipitliandani.android.facebox.Search.class);
            startActivity(intent);
        } else if (id == R.id.division) {
            fragment = new Division();
        } else if (id == R.id.management){
            fragment = new ManagementFragment();
        } else if (id == R.id.unitBisnis){
            fragment = new UBFragment();
        }else if (id == R.id.subsidiaries){
            fragment = new Subsidiaries();
        }else if (id == R.id.other){
            fragment = new OtherFragment();
        }else if (id == R.id.otherUnit){
            fragment = new OtherUnitFragment();
        }else if (id == R.id.Login){
            finish();
            Intent in = new Intent(this, LoginActivity.class);
            in.setFlags(in.getFlags()|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(in);
        }else if (id == R.id.Logout){
            auth.signOut();
            finish();
            Intent i = getIntent();
            i.setFlags(i.getFlags()| Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }else if (id == R.id.admin){
            fragment = new Administrator();

        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction (Uri uri){

    }

}
