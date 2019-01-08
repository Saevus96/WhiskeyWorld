package com.example.kpchl.whiskeyworld.main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Contact_date extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private EditText updateUaddress;
    private EditText updateUcity;
    private EditText updateUcountry;
    private EditText updateUpostCode;
    private TextView uEmail;
    private EditText uSurname;
    private EditText uName;
    private Button updateUserInfo;
    private Button updateUcardInfo;
    private EditText updateUphoneNumber;
    DatabaseReference myRefx;
    DatabaseReference myRefy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_date);

        //ustawienia kontrolek z informacjami o uzytkowniku
        uEmail = findViewById(R.id.notchangedEmail);
        uSurname = findViewById(R.id.updateLastName);
        uName = findViewById(R.id.updateFirstName);
        updateUserInfo = findViewById(R.id.updateUserInfo);
        //updateUcardInfo = findViewById(R.id.updateUcardInfo);
        updateUaddress = findViewById(R.id.updateAddress);
        updateUpostCode = findViewById(R.id.updatePostCode);
        updateUcity = findViewById(R.id.updateCity);
        updateUcountry = findViewById(R.id.updateCountry);
        updateUphoneNumber = findViewById(R.id.userPhoneNumber);

        // ustawienia nasluchiwacza dla przyciskow
        updateUserInfo.setOnClickListener(this);
        //pobranie unikalnego identyfikatora dla zalogowanego uzytkownika oraz sprawdzenie czy jest on zalogowany przez email/password
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        String provider = currentUser.getProviders().get(0);

        //okreslenie sciezki dla uzytkownika w bazie danych
        myRefx = FirebaseDatabase.getInstance().getReference("Users");
        myRefy = myRefx.child(uid);

        myRefy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String valueEmail = dataSnapshot.child("userEmail").getValue(String.class);
                uEmail.setText(valueEmail);
                String valueSurname = dataSnapshot.child("userLastName").getValue(String.class);
                uSurname.setText(valueSurname);
                String valueFirstName = dataSnapshot.child("userName").getValue(String.class);
                uName.setText(valueFirstName);
                String valueAddress = dataSnapshot.child("userAddress").getValue(String.class);
                updateUaddress.setText(valueAddress);
                String valuePostCode = dataSnapshot.child("userPostCode").getValue(String.class);
                updateUpostCode.setText(valuePostCode);
                String valueCity = dataSnapshot.child("userCity").getValue(String.class);
                updateUcity.setText(valueCity);
                String valueCountry = dataSnapshot.child("userCountry").getValue(String.class);
                updateUcountry.setText(valueCountry);
                String valuePhone = dataSnapshot.child("userPhone").getValue(String.class);
                updateUphoneNumber.setText(valuePhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //sprawdzenie czy uzytkownik uzywa konta email/password jesli tak wyswietla przycisk wyloguj
        /*if(provider.equals("password")){
            RelativeLayout mRelativeLayout = findViewById(R.id.relativLayout);
            Button logoutButton = new Button(Contact_date.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);


            logoutButton.setText("LogOut");

            mRelativeLayout.addView(logoutButton, params);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(Contact_date.this, Login.class));
                }
            });}
            */
    }

    //nasluchiwacz klikniecia przyciskow
    @Override
    public void onClick(View v) {
        if(v== updateUserInfo) {
            userBaseUpdate();
        }
    }



    //funkcja wysylajaca dane o karcie uzytkownika do bazy
    private void userBaseUpdate() {
        myRefx = FirebaseDatabase.getInstance().getReference("Users");
        myRefy = myRefx.child(uid);
        DatabaseReference myRefUname = myRefy.child("userName");
        DatabaseReference myRefUsurname = myRefy.child("userLastName");
        DatabaseReference myRefAddress = myRefy.child("userAddress");
        DatabaseReference myRefPostCode = myRefy.child("userPostCode");
        DatabaseReference myRefCity = myRefy.child("userCity");
        DatabaseReference myRefCountry = myRefy.child("userCountry");
        DatabaseReference myRefPhone = myRefy.child("userPhone");
        myRefUname.setValue(uName.getText().toString());
        myRefUsurname.setValue(uSurname.getText().toString());
        myRefAddress.setValue(updateUaddress.getText().toString());
        myRefPostCode.setValue(updateUpostCode.getText().toString());
        myRefCity.setValue(updateUcity.getText().toString());
        myRefCountry.setValue(updateUcountry.getText().toString());
        myRefPhone.setValue(updateUphoneNumber.getText().toString());

    }
}
