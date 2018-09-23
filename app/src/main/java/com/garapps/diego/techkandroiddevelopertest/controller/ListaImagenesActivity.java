package com.garapps.diego.techkandroiddevelopertest.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.garapps.diego.techkandroiddevelopertest.R;
import com.garapps.diego.techkandroiddevelopertest.models.Images;
import com.garapps.diego.techkandroiddevelopertest.models.Tags;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListaImagenesActivity extends AppCompatActivity{
    Set<Images> images = new HashSet<Images>();
    RecyclerView recyclerView;
    AdapterImages adapterImages;
    private String URL = "https://api.imgur.com/3/album/";
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerViewImages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        Bundle bundle = getIntent().getExtras();
        _id = bundle.getString("id");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getImg();
    }

    public void getImg(){
        images.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + _id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pasooo", response);

                //Tags Response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                    String titleData = jsonObject2.getString("title");
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("images");

                    for (int j = 0; j<jsonArray2.length();j++){
                        JSONObject obImg = (JSONObject) jsonArray2.get(j);
                        Images im = new Images();
                        im.setId(_id);
                        im.setTitle(titleData);
                        im.setDescription(obImg.getString("description"));
                        im.setViews(obImg.getString("views"));
                        im.setLink(obImg.getString("link"));
                        images.add(im);

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
        ArrayList<Images> imagesArray = new ArrayList<>();
        imagesArray.addAll(images);
        adapterImages = new AdapterImages(this, imagesArray);
        recyclerView.setAdapter(adapterImages);
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
                if (adapterImages != null)
                {
                    adapterImages.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

}
