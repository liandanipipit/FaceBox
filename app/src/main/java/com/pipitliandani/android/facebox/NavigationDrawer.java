package com.pipitliandani.android.facebox;

import android.app.SearchManager;
import android.content.Context;
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
import com.pipitliandani.android.facebox.fragments.Search;
import com.pipitliandani.android.facebox.fragments.UBFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NavigationDrawer extends AppCompatActivity
        implements BirthdayFragment.OnFragmentInteractionListener,
        CloseFriends.OnFragmentInteractionListener,Division.OnFragmentInteractionListener,
        Search.OnFragmentInteractionListener, ListOfEmployee.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, ManagementFragment.OnFragmentInteractionListener,
        UBFragment.OnFragmentInteractionListener, SearchAdapter.SearchAdapterlistener{
    private static final String URL = "https://facebox-89904.firebaseio.com/employee.json";
    private List<FaceBoxModel> modelList;
    private SearchAdapter searchAdapter;
    private SearchView searchView;
    RecyclerView rViewSearch;



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

//        menuHandler(R.id.ListOfEmployee);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rViewSearch = (RecyclerView)findViewById(R.id.rVSearch);
        modelList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, modelList, this);
        whiteNotificationBar(rViewSearch);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rViewSearch.setLayoutManager(mLayoutManager);
        rViewSearch.setItemAnimator(new DefaultItemAnimator());
        rViewSearch.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, 36));
        rViewSearch.setAdapter(searchAdapter);

        fetchList();
    }

    private void fetchList(){
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(NavigationDrawer.this, "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<FaceBoxModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<FaceBoxModel>>() {
                        }.getType());
                        modelList.clear();
                        modelList.addAll(items);
                        searchAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(NavigationDrawer.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySearchApplication.getmInstance().addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_navigation_drawer_drawer, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });
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
        } else if (id == R.id.search){
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
            fragment = new Search();
        } else if (id == R.id.division) {
            fragment = new Division();
        } else if (id == R.id.unitBisnis){
            fragment = new UBFragment();
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
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onSearchSelected(FaceBoxModel model) {
        Toast.makeText(getApplicationContext(), "Selected: " + model.getName() + ", " + model.getUnit(), Toast.LENGTH_LONG).show();
    }
}
