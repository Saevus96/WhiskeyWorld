package com.example.kpchl.whiskeyworld.product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kpchl.whiskeyworld.R;

public class WhiskeyRecipe extends AppCompatActivity {
private String whiskeyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiskey_recipe);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            whiskeyName = extras.getString("Whiskey Name");
        }

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }
}
