package com.example.kpchl.whiskeyworld.main;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.adapters.WhiskeyCartAdapter;
import com.example.kpchl.whiskeyworld.product.ProductActivity;
import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Home extends Fragment {

    private GridView gridView;
    private ArrayList<AddToCart> whiskeys = new ArrayList<AddToCart>( );

    private DatabaseReference ProductsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //pobranie gridView
        gridView = view.findViewById(R.id.gridview);

        //referencja do bazy danych
        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference("Products");

        //Wyciagniecie informacji na temat produktu(zdjecie, nazwa) i dodanie informacji do gridView

       ProductsRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               String string = dataSnapshot.child("icon").getValue(String.class);
               String string2 = dataSnapshot.child("name").getValue(String.class);
               whiskeys.add(new AddToCart(0,string2,string,0));
               final WhiskeyCartAdapter adapter = new WhiskeyCartAdapter(getContext(), whiskeys, "home");

               adapter.notifyDataSetChanged();
               gridView.setAdapter(adapter);
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
          ;
       });

    //metoda nasluchujaca klikniecia na produkt
   gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView whiskeyText = view.findViewById(R.id.textview_whiskey_name2);
            String whiskeyString = whiskeyText.getText().toString();
            //Toast.makeText(getActivity(), whiskeyText.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(),ProductActivity.class);

            intent.putExtra("Whiskey Name", whiskeyString);
            startActivity(intent);
        }
    });
        return view;
    }


}
