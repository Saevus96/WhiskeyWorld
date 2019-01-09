package com.example.kpchl.whiskeyworld.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;

import com.example.kpchl.whiskeyworld.adapters.WhiskeyCartAdapter;
import com.example.kpchl.whiskeyworld.product.ProductActivity;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ShopCart extends Fragment implements View.OnClickListener {
    View view;
    private String name;
    private GridView gridView;
    private ArrayList<AddToCart> whiskeys = new ArrayList<AddToCart>( );
    private ArrayList<String> nameArray = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private ImageView shop_holder;
    String whiskeyString;
    TextView deleteText;
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    DatabaseReference myRefz;
    DatabaseReference myRefProduct;
    String string;
    String string2;
    CardView btnPay;
    WhiskeyCartAdapter movieAdapter;
    private double totalPrice = 0.0;
    private String totalPriceString;
    private  TextView payText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_shop_cart, container, false);
        btnPay = view.findViewById(R.id.btnPay);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        gridView = view.findViewById(R.id.gridViewShop2);
        myRefProduct = FirebaseDatabase.getInstance().getReference("Products");
        shop_holder = view.findViewById(R.id.shop_holder);
        myRefx = FirebaseDatabase.getInstance().getReference("ShopCards");
        myRefy = myRefx.child(uid);
        myRefz = myRefy.getRef();
        gridView.setVisibility(View.INVISIBLE);

        shop_holder.setOnClickListener(this);
        btnPay.setOnClickListener(this);

        payText = view.findViewById(R.id.textPay);
        btnPay.setVisibility(View.INVISIBLE);

        myRefy.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists()) {
                    btnPay.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                    shop_holder.setVisibility(View.INVISIBLE);
                    String string = dataSnapshot.child("icon").getValue(String.class);
                    String string2 = dataSnapshot.child("name").getValue(String.class);
                    Double price = dataSnapshot.child("price").getValue(double.class);
                    int string3 = dataSnapshot.child("numberOfProduct").getValue(int.class);
                    double number = Double.valueOf(string3);
                    totalPrice =(number*price);
                    whiskeys.add(new AddToCart(string3,string2,string,price));
                    movieAdapter = new WhiskeyCartAdapter(getContext(), whiskeys, "shopping");
                    movieAdapter.notifyDataSetChanged();
                    gridView.setAdapter(movieAdapter);
                    totalPriceString = "Pay: "+String.valueOf(totalPrice)+"€";


                }
                else if(!dataSnapshot.exists())
                {
                    shop_holder.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                movieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            movieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                movieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                movieAdapter.notifyDataSetChanged();
            }
        });
        myRefy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object number = map.get("numberOfProduct");
                    Object price = map.get("price");
                    double pValue = Double.parseDouble(String.valueOf(price));
                    int pNumber = Integer.parseInt(String.valueOf(number));
                    sum+=(pNumber*pValue);
                    payText.setText("Pay: "+String.valueOf(sum)+"€");


                }
                if(!dataSnapshot.exists())
                {
                    gridView.setVisibility(View.GONE);
                    btnPay.setVisibility(View.INVISIBLE);
                    shop_holder.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView whiskeyText = view.findViewById(R.id.textview_whiskey_name2);
                 whiskeyString = whiskeyText.getText().toString();
                //Toast.makeText(getActivity(), whiskeyText.getText().toString(), Toast.LENGTH_SHORT).show();
                //deleteText = view.findViewById(R.id.deleteData);
                Intent intent = new Intent(getActivity(),ProductActivity.class);
                intent.putExtra("Whiskey Name", whiskeyString);
                startActivity(intent);





            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == shop_holder){
            Intent intent = new Intent(getActivity(),MainActivity.class);

            startActivity(intent);
            getActivity().finish();
        }
        if(v == btnPay){
            String sendText =payText.getText().toString().substring(5);
            Intent intent = new Intent(getActivity(), ChooseCardActivity.class);
            intent.putExtra("PayValue", sendText);
            startActivity(intent);
        }
    }
}
