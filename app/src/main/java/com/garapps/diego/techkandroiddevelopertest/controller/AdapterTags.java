package com.garapps.diego.techkandroiddevelopertest.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.garapps.diego.techkandroiddevelopertest.R;
import com.garapps.diego.techkandroiddevelopertest.models.Data;
import com.garapps.diego.techkandroiddevelopertest.models.Tags;

import java.util.ArrayList;
import java.util.List;

public class AdapterTags extends RecyclerView.Adapter<AdapterTags.CustomRecyclerView> implements Filterable{

    public List<Tags> itemList;
    public List<Tags> tagsFilterList;

    private RequestQueue mRequestQueue;
    private Context context;

    public AdapterTags(Context context, List<Tags> itemList) {
        this.itemList = itemList;
        this.mRequestQueue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        this.context = context;
        this.tagsFilterList = itemList;
    }

    @NonNull
    @Override
    public CustomRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_principal, null);
        CustomRecyclerView rcv = new CustomRecyclerView(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomRecyclerView holder, int position) {
        final Tags myData = tagsFilterList.get(position);

        holder.txtName.setText(myData.getName());
        holder.txtId.setText(" ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListaImagenesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", myData.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tagsFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    tagsFilterList = itemList;
                } else {

                    ArrayList<Tags> filteredList = new ArrayList<>();

                    for (Tags ta: itemList) {

                        if (ta.getName().toLowerCase().contains(charString)) {

                            filteredList.add(ta);
                        }
                    }

                    tagsFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = tagsFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tagsFilterList = (ArrayList<Tags>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    //Customizar el RecyclerView del diseño.
    public class CustomRecyclerView extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtId;

        CustomRecyclerView(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtId = itemView.findViewById(R.id.txt_images_id);
        }
    }
}
