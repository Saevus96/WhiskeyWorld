package com.example.kpchl.whiskeyworld.user_information;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.TextView;
import android.widget.Toast;
import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.main.ChooseCardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

public class AddCreditCard extends AppCompatActivity {
    CardInputWidget mCardInputWidget;
    private CardView addCardBtn;
    private DatabaseReference myRefx;
    private DatabaseReference myRefy;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private ProgressDialog progressDialog;
    private String active;
    private String payValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_credit_card);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mCardInputWidget = findViewById(R.id.card_input_widget);
        addCardBtn = findViewById(R.id.addCard);
        Intent intent = getIntent();
        active= intent.getStringExtra("activity");
        payValue = intent.getStringExtra("payValue");
        Toast.makeText(this, active, Toast.LENGTH_LONG).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>New Credit Card</font>"));
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        progressDialog = new ProgressDialog(this);

        myRefx = FirebaseDatabase.getInstance().getReference("stripe_customers");
        myRefy = myRefx.child(uid).child("sources");
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding Card...");
                progressDialog.show();

                    final Card card = new Card(mCardInputWidget.getCard().getNumber(),
                            mCardInputWidget.getCard().getExpMonth(), mCardInputWidget.getCard()
                            .getExpYear(), mCardInputWidget.getCard().getCVC());



                    Stripe stripe = new Stripe(getApplicationContext(),
                            "pk_test_ecRmBeq0XQpa1jpUeIkHLB0c");
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                        finish();
                                        myRefy.push().child("token").setValue(token.getId());
                                        ToastWindow("You successfully add new Card");


                                }

                                public void onError(Exception error) {
                                    // Show localized error message
                                    ToastWindow("You can't add Credit Card");
                                    finish();
                                }
                            }
                    );
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
}