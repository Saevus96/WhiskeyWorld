package com.example.kpchl.whiskeyworld.user_information;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import com.example.kpchl.whiskeyworld.adapters.AddressCustomList;
import com.example.kpchl.whiskeyworld.using_classes.SingleAddressInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class EditAddress extends AppCompatActivity {
    private TextInputLayout textInputCity;
    private TextInputLayout textInputAddress;
    private TextInputLayout textInputPostCode;
    private TextInputLayout textInputName;
    private TextInputLayout textInputSurname;
    private TextInputLayout textInputPhone;
    private TextInputLayout textInputEmail;
    private CountryCodePicker countryNumber;
    private CardView addAddressBtn;
    private String key;
    private TextView btnText;
    private CountryCodePicker country;

    private DatabaseReference myRefx;
    private DatabaseReference myRefy;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;

    private SingleAddressInfo addressInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_add_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Edit Address</font>"));
        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        textInputCity = findViewById(R.id.text_input_city);
        textInputAddress = findViewById(R.id.text_input_address);
        textInputPostCode = findViewById(R.id.text_input_postcode);
        textInputName = findViewById(R.id.text_input_name);
        textInputSurname = findViewById(R.id.text_input_surname);
        textInputPhone = findViewById(R.id.text_input_phone);
        textInputEmail = findViewById(R.id.text_input_email);
        addAddressBtn = findViewById(R.id.addAddress);
        country = findViewById(R.id.ccp);
        countryNumber = findViewById(R.id.ccpphone);
        btnText = findViewById(R.id.btnText);

        btnText.setText("Save");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        myRefx = FirebaseDatabase.getInstance().getReference("Addresses");
        myRefy = myRefx.child(uid);

        myRefy.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 String city = dataSnapshot.child("city").getValue(String.class);
                 String address = dataSnapshot.child("address").getValue(String.class);
                 String email = dataSnapshot.child("email").getValue(String.class);
                 String name = dataSnapshot.child("name").getValue(String.class);
                 String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                 String postCode = dataSnapshot.child("postCode").getValue(String.class);
                 String surname = dataSnapshot.child("surname").getValue(String.class);
                 String countryCode= dataSnapshot.child("countryCode").getValue(String.class);

                 textInputCity.getEditText().setText(city);
                 textInputAddress.getEditText().setText(address);
                 textInputEmail.getEditText().setText(email);
                 textInputName.getEditText().setText(name);
                 textInputPostCode.getEditText().setText(postCode);
                 textInputSurname.getEditText().setText(surname);
                 String phone = phoneNumber.substring(phoneNumber.indexOf(" "));
                 textInputPhone.getEditText().setText(phone);
                 country.setCountryForNameCode(countryCode);
                 String phoneCode = phoneNumber.substring(0,phoneNumber.indexOf(" "));
                 countryNumber.setCountryForPhoneCode(Integer.parseInt(phoneCode));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateEmail() | !validateName() | !validateCity() | !validateAddress() |
                        !validateSurname() | !validatePostCode() | !validatePhone()){
                    return;
                }
                final String countrypicked = country.getSelectedCountryName();
                final String countryPhonepicked = countryNumber.getSelectedCountryCodeWithPlus();
                final String inputCity = textInputCity.getEditText().getText().toString().trim();
                final String inputAddress = textInputAddress.getEditText().getText().toString().trim();
                final String inputPostCode = textInputPostCode.getEditText().getText().toString().trim();
                final String inputName = textInputName.getEditText().getText().toString().trim();
                final String inputSurname = textInputSurname.getEditText().getText().toString().trim();
                final String inputPhone = textInputPhone.getEditText().getText().toString().trim();
                final String inputEmail = textInputEmail.getEditText().getText().toString().trim();
                final String countryCode = country.getSelectedCountryNameCode();

                myRefy.child(key).setValue(new SingleAddressInfo(countrypicked,
                        countryPhonepicked+" "+inputPhone, inputCity,
                        inputAddress,inputPostCode,inputName,inputSurname,inputEmail,key ,countryCode));

                Intent intent1 = new Intent(EditAddress.this, MyAddresses.class);
                startActivity(intent1);
                ToastWindow("Address has been changed");
                finish();
            }
        });


    }
    private  boolean validateEmail(){
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            textInputEmail.setError("Field can't be empty");
            return false;
        }else if(!emailInput.contains("@")){
            textInputEmail.setError("Bad E-mail Address");
            return false;
        }
        else{
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
            return true;
        }
    }
    private  boolean validatePhone(){
        String phoneInput = textInputEmail.getEditText().getText().toString().trim();

        if(phoneInput.isEmpty()){
            textInputPhone.setError("Field can't be empty");
            return false;
        }else{
            textInputPhone.setError(null);
            textInputPhone.setErrorEnabled(false);
            return true;
        }
    }
    private  boolean validatePostCode(){
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            textInputPostCode.setError("Field can't be empty");
            return false;
        }else{
            textInputPostCode.setError(null);
            textInputPostCode.setErrorEnabled(false);
            return true;
        }
    }
    private  boolean validateAddress(){
        String addressInput = textInputAddress.getEditText().getText().toString().trim();

        if(addressInput.isEmpty()){
            textInputAddress.setError("Field can't be empty");
            return false;
        }else{
            textInputAddress.setError(null);
            textInputAddress.setErrorEnabled(false);
            return true;
        }
    }
    private  boolean validateCity(){
        String cityInput = textInputCity.getEditText().getText().toString().trim();

        if(cityInput.isEmpty()){
            textInputCity.setError("Field can't be empty");
            return false;
        }else{
            textInputCity.setError(null);
            textInputCity.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        for(int i =0; i<=9; i++){
            if(nameInput.contains(String.valueOf(i))){

                textInputName.setError("Name can't contains number");
                return false;
            }
        }
        if (nameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else if (nameInput.length() > 15) {
            textInputName.setError("Name too long");
            return false;
        } else {
            textInputName.setError(null);
            textInputName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateSurname() {
        String surnameInput = textInputSurname.getEditText().getText().toString().trim();
        for(int i =0; i<=9; i++){
            if(surnameInput.contains(String.valueOf(i))){

                textInputSurname.setError("Surname can't contains number");
                return false;
            }
        }
        if (surnameInput.isEmpty()) {
            textInputSurname.setError("Field can't be empty");
            return false;
        } else if (surnameInput.length() > 15) {
            textInputSurname.setError("Name too long");
            return false;
        } else {
            textInputSurname.setError(null);
            textInputSurname.setErrorEnabled(false);
            return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent1 = new Intent(EditAddress.this, MyAddresses.class);
            startActivity(intent1);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void ToastWindow(String message) {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.location_loc);
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
