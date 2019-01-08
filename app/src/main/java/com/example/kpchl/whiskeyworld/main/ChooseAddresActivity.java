package com.example.kpchl.whiskeyworld.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.AddressCustomList;
import com.example.kpchl.whiskeyworld.adapters.PayAddressAdapter;
import com.example.kpchl.whiskeyworld.user_information.AddAddress;
import com.example.kpchl.whiskeyworld.user_information.MyAddresses;
import com.example.kpchl.whiskeyworld.using_classes.SingleAddressInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChooseAddresActivity extends AppCompatActivity {
    private String payValue;
    private String cardId;
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private ListView addressListView;
    ArrayList<SingleAddressInfo> addressList = new ArrayList<SingleAddressInfo>();
    private PayAddressAdapter addressCustomList;
    private ImageView btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_addres);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Choose Address</font>"));

        Intent intent = getIntent();
        payValue = intent.getStringExtra("payValue");
        cardId = intent.getStringExtra("cardId");

        //Toast.makeText(this, payValue+cardId, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("Addresses");
        myRefy = myRefx.child(uid);
        addressListView = findViewById(R.id.addressList);
        addressCustomList = new PayAddressAdapter(ChooseAddresActivity.this, addressList);
        addressListView.setAdapter(addressCustomList);

        btnAdd = findViewById(R.id.addAddressBtn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseAddresActivity.this, AddAddress.class));
            }
        });
        myRefy.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey().toString();
                String address = dataSnapshot.child("address").getValue(String.class);
                String city = dataSnapshot.child("city").getValue(String.class);
                String country = dataSnapshot.child("country").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                String postCode = dataSnapshot.child("postCode").getValue(String.class);
                String surname = dataSnapshot.child("surname").getValue(String.class);
                addressList.add(new SingleAddressInfo(country,phoneNumber,city,address,postCode,name,surname,email,key,""));
                addressCustomList.notifyDataSetChanged();

                if(addressList.size()==0)
                {
                    btnAdd.setVisibility(View.VISIBLE);
                }else{
                    btnAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey().toString();
                String address = dataSnapshot.child("address").getValue(String.class);
                String city = dataSnapshot.child("city").getValue(String.class);
                String country = dataSnapshot.child("country").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                String postCode = dataSnapshot.child("postCode").getValue(String.class);
                String surname = dataSnapshot.child("surname").getValue(String.class);
                addressList.add(new SingleAddressInfo(country,phoneNumber,city,address,postCode,name,surname,email,key,""));
                addressCustomList.notifyDataSetChanged();
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

addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String sendAddress;
        Intent intent = new Intent(ChooseAddresActivity.this, PayActivity.class);
        intent.putExtra("payValue", payValue);
        intent.putExtra("cardId", cardId);
        switch (position){
            case 0:
                sendAddress = addressList.get(0).getKey();
                intent.putExtra("address", sendAddress );
                startActivity(intent);
                finish();
                break;
            case 1:
                finish();
                sendAddress = addressList.get(1).getKey();
                intent.putExtra("address", sendAddress );
                startActivity(intent);
                break;
            case 2:
                finish();
                sendAddress = addressList.get(2).getKey();
                intent.putExtra("address", sendAddress );
                startActivity(intent);
                break;
        }
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
