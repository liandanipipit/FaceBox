package com.pipitliandani.android.facebox;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pipitliandani.android.facebox.fragments.BirthdayFragment;
import com.pipitliandani.android.facebox.fragments.CloseFriends;
import com.pipitliandani.android.facebox.fragments.Division;
import com.pipitliandani.android.facebox.fragments.ListOfEmployee;
import com.pipitliandani.android.facebox.fragments.ManagementFragment;
import com.pipitliandani.android.facebox.fragments.OtherFragment;
import com.pipitliandani.android.facebox.fragments.Search;
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
        Search.OnFragmentInteractionListener, ListOfEmployee.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, ManagementFragment.OnFragmentInteractionListener,
        UBFragment.OnFragmentInteractionListener, OtherFragment.OnFragmentInteractionListener, Subsidiaries.OnFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuHandler(R.id.ListOfEmployee);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 39);

        Intent notifyIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 3, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 2, pendingIntent);
//        alarmManager.

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_navigation_drawer_drawer, menu);
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
        } else if (id == R.id.close_friends) {
            fragment = new CloseFriends();
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
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction (Uri uri){

    }

}
