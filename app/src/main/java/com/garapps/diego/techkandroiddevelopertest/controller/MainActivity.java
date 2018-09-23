package com.garapps.diego.techkandroiddevelopertest.controller;

import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garapps.diego.techkandroiddevelopertest.R;
import com.garapps.diego.techkandroiddevelopertest.models.Data;
import com.garapps.diego.techkandroiddevelopertest.models.Tags;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Set<Tags> tags = new HashSet<Tags>();
    RecyclerView recyclerView;
    AdapterTags adapterTags;
    private String URL = "https://api.imgur.com/3/gallery/hot/viral/0.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTags();

    }

    public void getTags(){
        tags.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray2 = jsonObject.getJSONArray("data");
                    for (int i = 0; i< jsonArray2.length(); i++){
                        JSONObject tagOb = (JSONObject) jsonArray2.get(i);
                        String id = tagOb.getString("id");
                        JSONArray arrayTags = tagOb.getJSONArray("tags");
                        for (int j = 0; j<arrayTags.length(); j++){
                            Tags ta = new Tags();
                            JSONObject tagObj = (JSONObject) arrayTags.get(j);
                            ta.setId(id);
                            ta.setName(tagObj.getString("name"));
                            tags.add(ta);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("error json", "json parsing error: " + e.getMessage());
                }

            }
        }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Client-ID 5be9caa5826a8d3");
                return headers;
            }
        };
        queue.add(stringRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                try {
                    if (request.getCacheEntry() != null) {
                        String cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                adapterGet();
            }
        });
    }

    public void adapterGet(){
        ArrayList<Tags> tagsArray = new ArrayList<>();
        tagsArray.addAll(tags);
        adapterTags = new AdapterTags(this, tagsArray);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterTags.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapterTags);
    }
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchObj(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void searchObj (SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapterTags != null)
                {
                    adapterTags.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

}
