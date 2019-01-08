package com.example.kpchl.whiskeyworld.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.example.kpchl.whiskeyworld.adapters.OrderAdapter;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.example.kpchl.whiskeyworld.using_classes.OrderClass;
import com.example.kpchl.whiskeyworld.using_classes.SingleAddressInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class PayActivity extends AppCompatActivity {
    private String cardId;
    private String payValue;
    private String address;
    private TextView pVal;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private DatabaseReference myRefx;
    private DatabaseReference chargeRef;
    private DatabaseReference shopCartRef;
    private DatabaseReference orderRef;
    private DatabaseReference orderToDoRef;
    private DatabaseReference addressRef;
    private DatabaseReference myRefy;
    private ListView productListView;
    ArrayList<AddToCart> orderList = new ArrayList<AddToCart>();
    private OrderAdapter orderCustomList;
    private CardView pay;
    private SingleAddressInfo singleAddressInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Finalise your purchase</font>"));
        productListView = findViewById(R.id.productListView);
        orderCustomList = new OrderAdapter(PayActivity.this,orderList);
        productListView.setAdapter(orderCustomList);

        Intent intent = getIntent();
        pVal = findViewById(R.id.payValue);
        cardId = intent.getStringExtra("cardId");
        payValue = intent.getStringExtra("payValue");

        address = intent.getStringExtra("address");
        pVal.setText(payValue);
       // Toast.makeText(this, cardId+payValue+address, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("ShopCards");
        addressRef = FirebaseDatabase.getInstance().getReference("Addresses").child(uid).child(address);

        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                singleAddressInfo = dataSnapshot.getValue(SingleAddressInfo.class);
               // Toast.makeText(PayActivity.this, singleAddressInfo.getCountry(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        chargeRef = FirebaseDatabase.getInstance().getReference("stripe_customers").child(uid);
        //Date currentTime = Calendar.getInstance().getTime();
        //String currTime = currentTime.toString().substring(0,19);

        //Toast.makeText(this, GetToday(), Toast.LENGTH_SHORT).show();
        orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(uid).child(GetToday());
        orderToDoRef = FirebaseDatabase.getInstance().getReference("OrdersToDo").child(GetTodayDay());
        myRefy = myRefx.child(uid);
        final String timeDate = GetToday().toString();


        pay = findViewById(R.id.pay);
        myRefy.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String icon = dataSnapshot.child("icon").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                int numberOfProduct = dataSnapshot.child("numberOfProduct").getValue(int.class);
                Double price = dataSnapshot.child("price").getValue(Double.class);
                orderList.add(new AddToCart(numberOfProduct,name,icon,price));
                orderCustomList.notifyDataSetChanged();


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
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = payValue.indexOf("€");

                final String payVal = payValue.substring(0, index).trim();

                ChargeClass charge = new ChargeClass(Double.valueOf(payVal) * 100, cardId, "eur", "products");
                chargeRef.child("charges").push().setValue(charge).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task != null){

                            finish();
                            String order = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
                            String orderId = order.substring(0,16);
                            OrderClass orderClass = new OrderClass(timeDate,"PENDING", payValue, orderId);
                            orderRef.setValue(orderClass);
                            orderRef.child("Products").setValue(orderList);
                            orderRef.child("OrderAddress").setValue(singleAddressInfo);


                            /*orderToDoRef.child("Products").setValue(orderList);
                            orderToDoRef.child("OrderAddress").setValue(singleAddressInfo);*/

                            orderToDoRef.child(orderId).setValue(orderClass);
                            orderToDoRef.child(orderId).child("Products").setValue(orderList);
                            orderToDoRef.child(orderId).child("OrderAddress").setValue(singleAddressInfo);
                            myRefx.child(uid).setValue(null);
                            ToastWindow("Payment Successfull, Your order ID is: " + orderClass.getOrderId());


                        }
                        else{
                            ToastWindow("Payment UnSuccessfull");
                            finish();
                        }
                    }
                });
            }
        });
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)

    public static String GetToday(){
        Date presentTime_Date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZonedDateTime.now().getZone().toString()));
        return dateFormat.format(presentTime_Date);
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)

    public static String GetTodayDay(){
        Date presentTime_Date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZonedDateTime.now().getZone().toString()));
        return dateFormat.format(presentTime_Date);
    }
}
