package com.example.kpchl.whiskeyworld.adapters;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.product.MyBounceInterpolator;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WhiskeyCartAdapter extends BaseAdapter {
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    DatabaseReference myRefFav1;
    DatabaseReference myRefFav2;
    private String uid;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private final Context mContext;
    private final ArrayList<AddToCart> whiskeys;
    private String fragment;

    public WhiskeyCartAdapter(Context context, ArrayList<AddToCart> whiskeys, String fragment) {
        this.mContext = context;
        this.whiskeys = whiskeys;
        this.fragment = fragment;

    }
    @Override
    public int getCount() {
        return whiskeys.size();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AddToCart whiskey = whiskeys.get(position);

        if (convertView == null) {
            if(fragment.equals("shopping")) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.whiskey_cart_fav_layout, null);

            }else if(fragment.equals("favourites")){
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.whiskey_cart_layout, null);
            }else if(fragment.equals("home"))
            {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.whiskey_layout, null);
            }

        }

        final ImageView imageView =
                (ImageView)convertView.findViewById(R.id.imageview_cover_art2);
        final TextView nameTextView =
                (TextView)convertView.findViewById(R.id.textview_whiskey_name2);
        final TextView deleteTextView = convertView.findViewById(R.id.deleteData);
        final TextView tx_price = convertView.findViewById(R.id.textPay);
        Picasso.get()
                .load(whiskey.getIcon()) // laduje zdjecie
                .resize(500, 500)  // zmienia jego rozmiar
                .into(imageView); // dodaje do image View

        //ustawia tytul z tablicy.
        nameTextView.setText(whiskey.getName());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("ShopCards");
        myRefy = myRefx.child(uid);
        myRefFav1 = FirebaseDatabase.getInstance().getReference("Favourites");
        myRefFav2 = myRefFav1.child(uid);

        if(fragment.equals("shopping")){
       deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String delete = nameTextView.getText().toString();
                notifyDataSetChanged();
                myRefy.child(delete).setValue(null);
                whiskeys.remove(position);
                WhiskeyCartAdapter.this.notifyDataSetChanged();

            }
        });
        }else if(fragment.equals("favourites")){

            deleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String delete = nameTextView.getText().toString();
                    notifyDataSetChanged();
                    myRefFav2.child(delete).setValue(null);

                    whiskeys.remove(position);
                    WhiskeyCartAdapter.this.notifyDataSetChanged();

                }
            });
        }
        if(fragment.equals("shopping")){
            final TextView plus = convertView.findViewById(R.id.plus);
            final TextView minus = convertView.findViewById(R.id.minus);
            final TextView numberOf = convertView.findViewById(R.id.numberOf);
            final TextView price = convertView.findViewById(R.id.textPrice);

            price.setText(whiskey.getPrice()+"€");
            numberOf.setText(String.valueOf(whiskey.getNumberOfProduct()));
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(mContext, "PLUS "+whiskey.getNumberOf(), Toast.LENGTH_SHORT).show();
                    if(whiskey.getNumberOfProduct()<10) {
                        numberOf.setText(String.valueOf(whiskey.getNumberOfProduct()));
                        myRefy.child(whiskey.getName()).child("numberOfProduct").setValue(whiskey.getNumberOfProduct() + 1);
                        whiskey.setNumberOfProduct(whiskey.getNumberOfProduct() + 1);
                        numberOf.setText(String.valueOf(whiskey.getNumberOfProduct()));
                    }
                    else{

                    }


                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myRefy.child(whiskey.getName()).child("numberOfProduct").setValue(whiskey.getNumberOfProduct()-1);
                    whiskey.setNumberOfProduct(whiskey.getNumberOfProduct()-1);
                    numberOf.setText(String.valueOf(whiskey.getNumberOfProduct()));
                    if(whiskey.getNumberOfProduct()==0){
                        String delete = nameTextView.getText().toString();
                        notifyDataSetChanged();
                        myRefy.child(delete).setValue(null);

                        whiskeys.remove(position);
                        WhiskeyCartAdapter.this.notifyDataSetChanged();
                    }
                }
            });

        }
        return convertView;
    }
}
