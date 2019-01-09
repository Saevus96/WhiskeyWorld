package com.example.kpchl.whiskeyworld.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.CustomList;
import com.example.kpchl.whiskeyworld.authorization.Start;
import com.example.kpchl.whiskeyworld.user_information.AboutUs;
import com.example.kpchl.whiskeyworld.user_information.AppConfiguration;
import com.example.kpchl.whiskeyworld.user_information.MyAddresses;
import com.example.kpchl.whiskeyworld.user_information.MyCreditCards;
import com.example.kpchl.whiskeyworld.user_information.MyOrders;
import com.example.kpchl.whiskeyworld.user_information.Support;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Settings extends Fragment {

    private ListView userListView;
    private ArrayList<String> options = new ArrayList<>();
    private ArrayAdapter adapter;
    ListView listOrders;
    ListView listUser;
    ListView listSupport;
    String[] orders = {"My Orders"};
    Integer[] ordersImg = {R.drawable.orders};
    String[] user = {"My Addresses", "My Cards"};
    String[] support = {"Where can you find us?"};
    Integer[] userImg = {R.drawable.location,R.drawable.credit_card};
    Integer[] supportImg = {R.drawable.about_us};
    private String provider;
    private TextView logOut;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(true);

        
        //pobranie unikalnego identyfikatora dla zalogowanego uzytkownika oraz sprawdzenie czy jest on zalogowany przez email/password
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        imageView = view.findViewById(R.id.userImage);
        logOut = view.findViewById(R.id.tx_logout);
        if(!currentUser.isAnonymous()) {
            provider = currentUser.getProviders().get(0);
            if (provider.equals("facebook.com")) {
                imageView.getLayoutParams().width = 280;
                imageView.getLayoutParams().height = 280;
                imageView.requestLayout();
                imageView.invalidate();
                String photoUrl = currentUser.getPhotoUrl().toString();
                Picasso.get().load(photoUrl).into(imageView);
            }
        }
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser.isAnonymous()){
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                    startActivity(new Intent(getApplicationContext(), Start.class));
                }else {
                    if (provider.equals("password")) {

                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getApplicationContext(), Start.class));
                    } else if (provider.equals("facebook.com")) {

                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getApplicationContext(), Start.class));
                    }
                }
            }
        });

        CustomList adapter = new
                CustomList(getActivity(), orders, ordersImg);
        listOrders = view.findViewById(R.id.listOrders);
        listOrders.setAdapter(adapter);

        CustomList adapterUser = new
                CustomList(getActivity(), user, userImg);

        CustomList adapterSupport = new
                CustomList(getActivity(), support, supportImg);

        listUser = view.findViewById(R.id.listUser);
        listUser.setAdapter(adapterUser);

        listSupport = view.findViewById(R.id.listSupport);
        listSupport.setAdapter(adapterSupport);

        listOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:

                        startActivity(new Intent(getActivity(), MyOrders.class));
                        break;
                }

            }
        });
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String[] user = {"My Addresses", "My Cards"};

                switch (position){
                    case 0:

                        startActivity(new Intent(getActivity(), MyAddresses.class));
                        break;
                    case 1:

                        startActivity(new Intent(getActivity(), MyCreditCards.class));
                        break;
                }
            }
        });
        listSupport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        startActivity(new Intent(getActivity(), Support.class));
                        break;
                    case 0:
                        startActivity(new Intent(getActivity(),AboutUs.class));
                        break;
                }
            }
        });

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear(); //Empty the old menu
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), AppConfiguration.class));
        }
        return super.onOptionsItemSelected(item);
    }
}