package com.example.kpchl.whiskeyworld.main;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private FirebaseUser currentUser;

    private String uid;

    private String value;
    private FirebaseDatabase database;
    private DatabaseReference mref;
    private DatabaseReference mref2;
    private EditText emailEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Bundle extras = getIntent().getExtras();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);

        TextView title = findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("Home");


    }




    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        return true;
    }

    Fragment selectedFragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            TextView title= findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));

            switch (menuItem.getItemId()) {
               case R.id.nav_home:
                    selectedFragment = new Home();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                   getSupportActionBar().setElevation(0);
                    title.setText("Home");

                    break;
                case R.id.nav_search:

                    selectedFragment = new Search();
                    title.setText("Search");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setElevation(0);
                    break;
                case R.id.nav_favourites:
                    selectedFragment = new Favourites();
                    title.setText("My Favourites");

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setElevation(0);
                    break;
                case R.id.nav_settings:
                    selectedFragment = new Settings();

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setElevation(0);
                    title.setText("");
                    break;
                case R.id.shop_cart:
                    selectedFragment = new ShopCart();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setElevation(0);
                    title.setText("My Shopping Cart");
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
