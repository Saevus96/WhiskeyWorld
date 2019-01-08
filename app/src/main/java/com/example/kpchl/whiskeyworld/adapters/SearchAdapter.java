package com.example.kpchl.whiskeyworld.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.main.Search;
import com.example.kpchl.whiskeyworld.product.ProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<String> nameList;
    ArrayList<String> priceList;
    ArrayList<String> photoList;

    static class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        static ImageView photopr;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name  = itemView.findViewById(R.id.name);
            price  = itemView.findViewById(R.id.price);
            photopr = itemView.findViewById(R.id.photo);

        }
    }
    public SearchAdapter(Context context, ArrayList<String> nameList, ArrayList<String> priceList, ArrayList<String> photoList) {
        this.context = context;
        this.nameList = nameList;
        this.priceList = priceList;
        this.photoList = photoList;
    }


    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, viewGroup, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder searchViewHolder, final int i) {
            searchViewHolder.name.setText(nameList.get(i));
            searchViewHolder.price.setText(priceList.get(i)+"€");
        Picasso.get().load(photoList.get(i)).into(SearchViewHolder.photopr);
        searchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProductActivity.class);

                intent.putExtra("Whiskey Name", nameList.get(i));
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return nameList.size();
    }
}
