package com.garapps.diego.techkandroiddevelopertest.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.garapps.diego.techkandroiddevelopertest.models.ImageComent;
import com.garapps.diego.techkandroiddevelopertest.models.Tags;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageScreen extends AppCompatActivity{

    TextView txtViews, txtDescription;
    ImageView imgView;
    String link = "";
    String _id = "";
    String views = "";
    String description = "";
    ArrayList<ImageComent> imageComent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_screen);

        txtViews = (TextView) findViewById(R.id.txt_views);
        txtDescription = (TextView) findViewById(R.id.txt_description_view);
        imgView = (ImageView) findViewById(R.id.image_view_screen);

        Bundle bundle = getIntent().getExtras();
        _id = bundle.getString("id");
        views = bundle.getString("views");
        if (bundle.getString("description") == null){
            description = "no existe descripción";
        }else{
            description = bundle.getString("description");
        }
        link = bundle.getString("link");
    }


    @Override
    protected void onStart() {
        super.onStart();
        setImageView();
        getImageComment();
    }

    public void setImageView(){
        Picasso.get().load(link).into(imgView);
        txtViews.setText("views: " + views.toString());
        txtDescription.setText("descripción: " + description.toString());

    }

    public void getImageComment(){
        imageComent.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.imgur.com/3/gallery/" + _id + "/comments/best", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray2 = jsonObject.getJSONArray("data");
                    for (int i = 0; i< jsonArray2.length(); i++){
                        JSONObject comOb = (JSONObject) jsonArray2.get(i);
                        ImageComent ic = new ImageComent();
                        ic.setComment(comOb.getString("comment"));
                        imageComent.add(ic);
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
                getAdapter();
            }
        });
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

    public void getAdapter(){
        CommentAdapter adapter = new CommentAdapter(this, imageComent);
        ListView list = (ListView) findViewById(R.id.list_comment);
        list.setAdapter(adapter);
    }

    public class CommentAdapter extends ArrayAdapter<ImageComent> {
        public CommentAdapter(Context context, ArrayList<ImageComent> comment) {
            super(context, 0, comment);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageComent comment = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_comment, parent, false);
            }
            TextView tvComment = (TextView) convertView.findViewById(R.id.txt_comment);
            tvComment.setText(comment.getComment());
            return convertView;
        }
    }
}
