package com.example.kpchl.whiskeyworld.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.PayCartAdapter;
import com.example.kpchl.whiskeyworld.user_information.AddCreditCard;
import com.example.kpchl.whiskeyworld.using_classes.SingleCreditCardInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.view.CardInputWidget;

import java.util.ArrayList;
import java.util.Currency;

public class ChooseCardActivity extends AppCompatActivity {
    CardInputWidget mCardInputWidget;
    CardView paybtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private DatabaseReference myRefx;
    private DatabaseReference myRefy;
    private ListView creditCardListView;
    ArrayList<SingleCreditCardInfo> creditCardList = new ArrayList<SingleCreditCardInfo>();
    private PayCartAdapter creditCardCustomList;

    String customer_id;
    String token_id;

    String payValue;
    Button pay;
    private ImageView btnAdd;
    Currency currency;
    String cardId = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Choose Card</font>"));
        Intent intent = getIntent();

        btnAdd = findViewById(R.id.addCardBtn);
        payValue = intent.getStringExtra("PayValue");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("stripe_customers");
        myRefy = myRefx.child(uid).child("sources");

        creditCardListView = findViewById(R.id.creditCardList);
        creditCardCustomList = new PayCartAdapter(ChooseCardActivity.this, creditCardList);
        creditCardListView.setAdapter(creditCardCustomList);


        //Toast.makeText(getApplicationContext(), payValue, Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        // myRefx = FirebaseDatabase.getInstance().getReference("stripe_customers");
        myRefx = FirebaseDatabase.getInstance().getReference("stripe_customers");
        myRefy = myRefx.child(uid).child("sources");


        cardList();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent1 = new Intent(ChooseCardActivity.this, AddCreditCard.class);
               intent1.putExtra("activity","ChooseCard");
               intent1.putExtra("payValue", payValue);
               startActivity(intent1);

            }
        });
        creditCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sendCardId;
                Intent intent = new Intent(ChooseCardActivity.this, ChooseAddresActivity.class);
                intent.putExtra("payValue", payValue);
                switch (position){
                    case 0:
                        sendCardId = creditCardList.get(0).getCardId();
                        intent.putExtra("cardId", sendCardId );
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        sendCardId = creditCardList.get(1).getCardId();
                        intent.putExtra("cardId", sendCardId );
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        sendCardId = creditCardList.get(2).getCardId();
                        intent.putExtra("cardId", sendCardId );
                        startActivity(intent);
                        finish();
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

   private void cardList(){
       myRefy.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               if(dataSnapshot.child("exp_month").exists() && dataSnapshot.child("exp_year").exists()) {
                   String key = dataSnapshot.getKey().toString();
                   String last4 = dataSnapshot.child("last4").getValue(String.class);
                   String brand = dataSnapshot.child("brand").getValue(String.class);
                   int exp_month = dataSnapshot.child("exp_month").getValue(int.class);
                   int exp_year = dataSnapshot.child("exp_year").getValue(int.class);
                   String cardId = dataSnapshot.child("id").getValue(String.class);
                   creditCardList.add(new SingleCreditCardInfo(last4, brand, exp_month, exp_year, key, cardId));
                   creditCardCustomList.notifyDataSetChanged();
                   if(creditCardList.size()==0)
                   {
                       btnAdd.setVisibility(View.VISIBLE);
                   }else{
                       btnAdd.setVisibility(View.GONE);
                   }
               }


           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               if(dataSnapshot.child("exp_month").exists() && dataSnapshot.child("exp_year").exists()) {
                   String key = dataSnapshot.getKey().toString();
                   String last4 = dataSnapshot.child("last4").getValue(String.class);
                   String brand = dataSnapshot.child("brand").getValue(String.class);
                   int exp_month = dataSnapshot.child("exp_month").getValue(int.class);
                   int exp_year = dataSnapshot.child("exp_year").getValue(int.class);
                   String cardId = dataSnapshot.child("id").getValue(String.class);
                   creditCardList.add(new SingleCreditCardInfo(last4, brand, exp_month, exp_year, key, cardId));
                   creditCardCustomList.notifyDataSetChanged();
                   if(creditCardList.size()==0)
                   {
                       btnAdd.setVisibility(View.VISIBLE);
                   }else{
                       btnAdd.setVisibility(View.GONE);
                   }
               }
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


}
