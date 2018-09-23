package com.garapps.diego.techkandroiddevelopertest.controller;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.garapps.diego.techkandroiddevelopertest.R;
import com.garapps.diego.techkandroiddevelopertest.models.Data;
import com.garapps.diego.techkandroiddevelopertest.models.Images;
import com.garapps.diego.techkandroiddevelopertest.models.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterImages extends RecyclerView.Adapter<AdapterImages.CustomRecyclerView> implements Filterable{

    public List<Images> itemList;
    public List<Images> imagesFilterList;

    private RequestQueue mRequestQueue;
    private Context context;

    public AdapterImages(Context context, List<Images> itemList) {
        this.itemList = itemList;
        this.mRequestQueue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        this.context = context;
        this.imagesFilterList = itemList;
    }

    @NonNull
    @Override
    public CustomRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_images_list, null);
        CustomRecyclerView rcv = new CustomRecyclerView(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomRecyclerView holder, int position) {
        final Images myData = imagesFilterList.get(position);

        if (myData.getTitle() == null || myData.getTitle().length()==0){
            holder.txtTitle.setText("no existe título");
        }else{
            holder.txtTitle.setText(myData.getTitle());
        }
        //queda guardado un string "null"
        if (myData.getDescription() == "null"){
            holder.txtDescription.setText("no existe descripción");
        }else{
            holder.txtDescription.setText(myData.getDescription());
        }
        Picasso.get().load(myData.getLink()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", myData.getId());
                bundle.putString("description", myData.getDescription());
                bundle.putString("link", myData.getLink());
                bundle.putString("views", myData.getViews());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.imagesFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    imagesFilterList = itemList;
                } else {
                    ArrayList<Images> filteredList = new ArrayList<>();
                    for (Images ta: itemList) {
                        if (ta.getTitle().toLowerCase().contains(charString)) {
                            filteredList.add(ta);
                        }
                    }
                    imagesFilterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = imagesFilterList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                imagesFilterList = (ArrayList<Images>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    //Customizar el RecyclerView del diseño.
    public class CustomRecyclerView extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        ImageView img;

        CustomRecyclerView(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDescription = itemView.findViewById(R.id.txt_description);
            img = itemView.findViewById(R.id.imgView);
        }
    }
}

