package com.example.kpchl.whiskeyworld.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.example.kpchl.whiskeyworld.using_classes.Favourite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductFragment extends Fragment implements DialogNumberProducts.OnInputSelected{



    public static final String TAG = "ProductFragment";
    private int i = 0;
    private CardView btnAddtoCart;
    private DatabaseReference myRefx;
    private DatabaseReference myRefy;
    private DatabaseReference myRefz;
    private DatabaseReference myRefUid;
    private DatabaseReference myRefWhiskeyFav;
    private String image;
    private String description;
    private double price;
    private ImageView imageView;
    private TextView productName;
    public TextView txDescription;
    public TextView txPrice;
    private String whiskeyName;
    //obsluga dla danych uzytkownika
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;

    @Override
    public void sendInput(String input) {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_product, container, false);
        whiskeyName = getArguments().getString("name");
        productName = view.findViewById(R.id.productName);
        productName.setText(whiskeyName);

        imageView = view.findViewById(R.id.productImage);
        productName = view.findViewById(R.id.productName);
        txDescription = view.findViewById(R.id.description);
        txPrice = view.findViewById(R.id.priceText);
        btnAddtoCart = view.findViewById(R.id.btnAddtoCart);
        final ImageView imageLike = view.findViewById(R.id.viewLike);
        final boolean[] liked = {false};
        //pobranie uid dla uzytkownika
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefz = FirebaseDatabase.getInstance().getReference("Favourites");
        myRefUid = myRefz.child(uid);
        myRefWhiskeyFav = myRefUid.child(whiskeyName);

        myRefWhiskeyFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Boolean snap = dataSnapshot.child("favourite").getValue(Boolean.class);
                    String whiskeyTag = dataSnapshot.child("name").getValue(String.class);

                    if (whiskeyTag.equals(whiskeyName)) {
                        //Toast.makeText(getActivity(), String.valueOf(snap), Toast.LENGTH_SHORT).show();
                        if (snap == true) {
                            imageLike.setImageResource(R.drawable.fav_filled);
                            liked[0] = true;
                        }
                    } else {
                        liked[0] = false;
                        imageLike.setImageResource(R.drawable.fav_not_filled);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);


                imageLike.startAnimation(myAnim);
                if(liked[0] ==false){
                    imageLike.setImageResource(R.drawable.fav_filled);
                    liked[0] = true;
                    Favourite addToFavourite = new Favourite(whiskeyName, image, liked[0]);
                    myRefWhiskeyFav.setValue(addToFavourite);

                }else{
                    imageLike.setImageResource(R.drawable.fav_not_filled);
                    liked[0] = false;
                   myRefWhiskeyFav.setValue(null);
                }

            }
        });

        myRefx = FirebaseDatabase.getInstance().getReference("Products");
        myRefy = myRefx.child(whiskeyName);
        if(currentUser.isAnonymous()){btnAddtoCart.setVisibility(View.GONE);
        imageLike.setVisibility(View.GONE);}
        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putString("name", whiskeyName);

                DialogNumberProducts dialog = new DialogNumberProducts();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(ProductFragment.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");

            }
        });
        myRefy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                image = dataSnapshot.child("icon").getValue(String.class);
                description = dataSnapshot.child("description").getValue(String.class);
                price = dataSnapshot.child("price").getValue(double.class);
                Picasso.get().load(image).into(imageView);
                txDescription.setText(description);
                txPrice.setText(price+"€");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
