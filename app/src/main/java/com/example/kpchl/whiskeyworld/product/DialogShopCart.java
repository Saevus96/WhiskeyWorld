package com.example.kpchl.whiskeyworld.product;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.OrderAdapter;
import com.example.kpchl.whiskeyworld.adapters.ShopDialog;
import com.example.kpchl.whiskeyworld.authorization.Start;
import com.example.kpchl.whiskeyworld.product.ProductActivity;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DialogShopCart extends DialogFragment {

    private static final String TAG = "ShoppingCartDialog";
    private ListView shoppingListView;
    private ArrayList<AddToCart> productList = new ArrayList<AddToCart>();
    private ShopDialog orderAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private DatabaseReference productsRef;


    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_shop_cart, container,false);

        shoppingListView = view.findViewById(R.id.shoppingListView);
        orderAdapter = new ShopDialog(getActivity() ,productList);
        shoppingListView.setAdapter(orderAdapter);
        getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.TOP);
        Animation rotateimage = AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in);
        getDialog().getWindow().setWindowAnimations(android.R.anim.fade_in);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();

        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.x = 200;

        getDialog().getWindow().setAttributes(p);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        productsRef = FirebaseDatabase.getInstance().getReference("ShopCards").child(uid);

        productsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AddToCart addToCart = dataSnapshot.getValue(AddToCart.class);
                productList.add(addToCart);
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AddToCart addToCart = dataSnapshot.getValue(AddToCart.class);
                productList.add(addToCart);
                orderAdapter.notifyDataSetChanged();
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


        return view;

    }
}
