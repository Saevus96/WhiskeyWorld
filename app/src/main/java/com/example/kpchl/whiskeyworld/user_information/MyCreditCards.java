package com.example.kpchl.whiskeyworld.user_information;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.AddressCustomList;
import com.example.kpchl.whiskeyworld.adapters.CreditCardCustomList;
import com.example.kpchl.whiskeyworld.using_classes.SingleAddressInfo;
import com.example.kpchl.whiskeyworld.using_classes.SingleCreditCardInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyCreditCards extends AppCompatActivity {
    private ImageView addCardBtn;
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private ListView creditCardListView;

    ArrayList<SingleCreditCardInfo> creditCardList = new ArrayList<SingleCreditCardInfo>();
    private CreditCardCustomList creditCardCustomList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_my_credit_cards);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>My Credit Cards</font>"));

        addCardBtn = findViewById(R.id.addCardBtn);

        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCreditCards.this, AddCreditCard.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("stripe_customers");
        myRefy = myRefx.child(uid).child("sources");
        creditCardListView = findViewById(R.id.creditCardList);
        creditCardCustomList = new CreditCardCustomList(MyCreditCards.this, creditCardList);
        creditCardListView.setAdapter(creditCardCustomList);
        if(currentUser.isAnonymous())
        {
            addCardBtn.setVisibility(View.GONE);
            ToastWindow("You can't add new card if you are a guest");
        }
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
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(creditCardList.size()>=3){
                    ToastWindow("You can't add more than 3 cards");
                }
                else {

                    Intent intent = new Intent(MyCreditCards.this, AddCreditCard.class);
                    startActivity(intent);

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
    void ToastWindow(String message) {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.card);
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
