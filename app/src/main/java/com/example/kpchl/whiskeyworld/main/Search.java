package com.example.kpchl.whiskeyworld.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.SearchAdapter;
import com.example.kpchl.whiskeyworld.adapters.WhiskeyCartAdapter;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private View view;
    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    SearchAdapter searchAdapter;
    ArrayList<String>nameList;
    ArrayList<String>photo;
    ArrayList<String>priceList;
    ArrayList<String>photoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        search_edit_text = view.findViewById(R.id.search_edit_text);
        recyclerView = view.findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        nameList = new ArrayList<>();
        priceList = new ArrayList<>();
        photoList = new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }else{
                    nameList.clear();
                    priceList.clear();
                    photoList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });



        return view;
    }

    private void setAdapter(final String searchString) {


        databaseReference.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameList.clear();
                priceList.clear();
                photoList.clear();
                recyclerView.removeAllViews();
                int counter =0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    String name = snapshot.child("name").getValue(String.class);
                    Double price = snapshot.child("price").getValue(Double.class);
                    String photo = snapshot.child("icon").getValue(String.class);
                    String stringPrice = String.valueOf(price);
                   // Toast.makeText(getActivity(), price+"euro", Toast.LENGTH_SHORT).show();

                    if(name.toLowerCase().contains(searchString))
                    {
                        nameList.add(name);
                        priceList.add(stringPrice);
                        photoList.add(photo);
                        counter++;
                    }else if(stringPrice.toLowerCase().contains(searchString))
                    {
                        nameList.add(name);
                        priceList.add(stringPrice);
                        photoList.add(photo);
                        counter++;
                    }
                    if (counter==15){
                        break;
                    }
                }
                searchAdapter = new SearchAdapter(getContext(), nameList, priceList, photoList);
                recyclerView.setAdapter(searchAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}