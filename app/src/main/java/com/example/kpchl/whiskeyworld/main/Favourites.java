package com.example.kpchl.whiskeyworld.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kpchl.whiskeyworld.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Favourites extends Fragment {
    View view;
    private String name;
    private GridView gridView;
    private ArrayList<AddToCart> whiskeys = new ArrayList<AddToCart>();
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
    WhiskeyCartAdapter movieAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourites, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        gridView = view.findViewById(R.id.gridViewFav);
        myRefProduct = FirebaseDatabase.getInstance().getReference("Products");
        //shop_holder = view.findViewById(R.id.shop_holder);
        myRefx = FirebaseDatabase.getInstance().getReference("Favourites");
        myRefy = myRefx.child(uid);
        myRefz = myRefy.getRef();
        gridView.setVisibility(View.INVISIBLE);
        /*shop_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);

                startActivity(intent);
                getActivity().finish();
            }
        });*/
        myRefy.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    gridView.setVisibility(View.VISIBLE);
                    //shop_holder.setVisibility(View.INVISIBLE);
                    String string = dataSnapshot.child("icon").getValue(String.class);
                    String string2 = dataSnapshot.child("name").getValue(String.class);
                    whiskeys.add(new AddToCart(0,string2,string,0));
                    movieAdapter = new WhiskeyCartAdapter(getContext(), whiskeys,"favourites");
                    movieAdapter.notifyDataSetChanged();
                    gridView.setAdapter(movieAdapter);
                } else if (!dataSnapshot.exists()) {
                   // shop_holder.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                gridView.setAdapter(movieAdapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                movieAdapter.notifyDataSetChanged();
                gridView.setAdapter(movieAdapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                gridView.setAdapter(movieAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                gridView.setAdapter(movieAdapter);

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView whiskeyText = view.findViewById(R.id.textview_whiskey_name2);
                whiskeyString = whiskeyText.getText().toString();
                //Toast.makeText(getActivity(), whiskeyText.getText().toString(), Toast.LENGTH_SHORT).show();
                //deleteText = view.findViewById(R.id.deleteData);
                Intent intent = new Intent(getActivity(), ProductActivity.class);

                intent.putExtra("Whiskey Name", whiskeyString);
                startActivity(intent);


            }
        });
        return view;
    }
}