package com.example.kpchl.whiskeyworld.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.product.ProductActivity;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.example.kpchl.whiskeyworld.using_classes.SingleCreditCardInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ShopDialog extends ArrayAdapter<AddToCart> {
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    Context context;
    List<AddToCart> object;
    private String uid;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public class ViewHolder {
        ImageView photo;
        TextView name;
        TextView howMany;

    }

    public ShopDialog(Context context, List<AddToCart> objects) {

        super(context, R.layout.single_dialog_product, objects);
        this.object = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;

        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_dialog_product, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.photo = row.findViewById(R.id.photo);
            viewHolder.name = row.findViewById(R.id.name);
            viewHolder.howMany = row.findViewById(R.id.howmany);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        final AddToCart item = getItem(position);
        Picasso.get()
                .load(item.getIcon()) // laduje zdjecie
                .resize(500, 500)  // zmienia jego rozmiar
                .into(viewHolder.photo); // dodaje do image View
        viewHolder.name.setText(item.getName());

        viewHolder.howMany.setText(String.valueOf(item.getNumberOfProduct()));

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("Whiskey Name", item.getName());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });
        return row;
    }
}

