package com.example.kpchl.whiskeyworld.user_information;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.OrderAdapter;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.example.kpchl.whiskeyworld.using_classes.OrderClass;
import com.example.kpchl.whiskeyworld.using_classes.SingleAddressInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrderInformation extends AppCompatActivity {
    String orderDate;
    DatabaseReference productsRef;
    DatabaseReference addressRef;
    DatabaseReference detailsRef;

    private TextView nameSurname;
    private TextView address;
    private TextView city;
    private TextView country;
    private TextView phoneNumber;
    private TextView postCode;
    private TextView email;

    private TextView orderDat;
    private TextView orderID;
    private TextView orderPrice;
    private TextView orderStatus;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;


    private ListView orderProducts;
    private OrderAdapter orderAdapter;
    private ArrayList<AddToCart> orderList = new ArrayList<AddToCart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        orderDate = intent.getStringExtra("orderDate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Order Information</font>"));
        setContentView(R.layout.activity_my_order_information);

        nameSurname = findViewById(R.id.name_surname);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        phoneNumber = findViewById(R.id.phone);
        postCode = findViewById(R.id.post);
        email = findViewById(R.id.email);

        orderDat = findViewById(R.id.orderDate);
        orderID = findViewById(R.id.orderID);
        orderPrice = findViewById(R.id.orderPrice);
        orderStatus = findViewById(R.id.orderStatus);



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        productsRef = FirebaseDatabase.getInstance().getReference("Orders").child(uid).child(orderDate).child("Products");
        detailsRef = FirebaseDatabase.getInstance().getReference("Orders").child(uid).child(orderDate);
        addressRef = FirebaseDatabase.getInstance().getReference("Orders").child(uid).child(orderDate).child("OrderAddress");

        orderProducts = findViewById(R.id.orderListView);
        orderAdapter = new OrderAdapter(MyOrderInformation.this, orderList);
        orderProducts.setAdapter(orderAdapter);

        detailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderClass orderClass = dataSnapshot.getValue(OrderClass.class);
                orderDat.setText(orderClass.getOrderDate());
                orderStatus.setText(orderClass.getStatus());
                orderPrice.setText(orderClass.getPayValue());
                orderID.setText(orderClass.getOrderId());

                if(orderClass.getStatus().equals("PENDING")){
                    orderStatus.setTextColor(Color.RED);
                }else{
                    orderStatus.setTextColor(Color.parseColor("#33cc66"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SingleAddressInfo singleAddressInfo = dataSnapshot.getValue(SingleAddressInfo.class);
                 nameSurname.setText(singleAddressInfo.getName()+" "+singleAddressInfo.getSurname());
                address.setText(singleAddressInfo.getAddress());
                city.setText(singleAddressInfo.getCity());
                country.setText(singleAddressInfo.getCountry());
                phoneNumber.setText(singleAddressInfo.getPhoneNumber());
                postCode.setText(singleAddressInfo.getPostCode());
                email.setText(singleAddressInfo.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        productsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AddToCart addToCart = dataSnapshot.getValue(AddToCart.class);
                orderList.add(addToCart);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
