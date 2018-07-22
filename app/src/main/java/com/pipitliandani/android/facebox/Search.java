package com.pipitliandani.android.facebox;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Search extends AppCompatActivity implements SearchAdapter.SearchAdapterlistener {
    private static final String URL = "https://faceboxprod.firebaseio.com/employee.json";
    private static final String URL1 = "https://facebox-89904.firebaseio.com/data.json";
    private List<FaceBoxModel> modelList;
    private SearchAdapter searchAdapter;
    private SearchView searchView;
    RecyclerView rViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rViewSearch = (RecyclerView)findViewById(R.id.rViewSearch);
        modelList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, modelList, this);
        whiteNotificationBar(rViewSearch);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rViewSearch.setLayoutManager(mLayoutManager);
        rViewSearch.setItemAnimator(new DefaultItemAnimator());
        rViewSearch.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, 36));
        rViewSearch.setAdapter(searchAdapter);
        fetchList();
    }
    private void fetchList(){
        JsonObjectRequest request = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Iterator iterator = response.keys();
                        while (iterator.hasNext()) {
                            String key = (String)iterator.next();
                            try {
                                JSONObject object = response.getJSONObject(key);
                                FaceBoxModel data = new Gson().fromJson(object.toString(), FaceBoxModel.class);
                                data.setKey(key);
                                Log.d("search", key);
                                modelList.add(data);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        searchAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Search.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySearchApplication.getmInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);

        // Associate searchable configuration with the SearchView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setQueryHint("Search by name, division, place of birth");
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                searchAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
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
        Log.d("Selected", model.getName() + ", " + model.getUnit() + ", " + model.getFunctionTitle());

    }
}
