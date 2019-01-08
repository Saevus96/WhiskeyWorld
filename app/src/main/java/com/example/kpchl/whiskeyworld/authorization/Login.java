package com.example.kpchl.whiskeyworld.authorization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class Login extends AppCompatActivity  {
    private ImageView logo;
    private CardView dataCard;
    private CardView login;
    private ImageButton facebookButton;
    private ProgressDialog progressDialog;

    private TextInputLayout editTextEmail;
    private TextInputLayout editTextPassword;
    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle("");
        logo = findViewById(R.id.logo);
        dataCard = findViewById(R.id.dataCard);
        login = findViewById(R.id.loginCard);
        facebookButton = findViewById(R.id.customFacebook);
        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_Password);
        Animation rotateimage = AnimationUtils.loadAnimation(Login.this,android.R.anim.slide_in_left);
        facebookButton.startAnimation(rotateimage);
        logo.startAnimation(rotateimage);
        dataCard.startAnimation(rotateimage);
        login.startAnimation(rotateimage);
        if(!isOnline()){
            finish();
            toastWindow("You don't have internet connection!");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                // ...
            }

            @Override
            public void onError(FacebookException error) {

                // ...
            }
        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));
                progressDialog.setMessage("Logging...");
                progressDialog.show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast,
                                    (ViewGroup) findViewById(R.id.toast_layout_root));

                            ImageView image = layout.findViewById(R.id.image);
                            image.setImageResource(R.drawable.close);
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("User exist");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            updateUI();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //aktualizuje ui uzytkownika po pomyslnym zalogowaniu
    private void updateUI() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
    private void userLogin() {
        String email = editTextEmail.getEditText().getText().toString();
        String password = editTextPassword.getEditText().getText().toString();
        boolean hasErrors = false;

        if (TextUtils.isEmpty(email)) {
            hasErrors = true;
            editTextEmail.setError("Provide E-mail");

        }
        if(!email.contains("@")){
            editTextEmail.setError("Bad E-mail Address");
            hasErrors=true;
        }
        if (TextUtils.isEmpty(password)) {
            hasErrors = true;
            editTextPassword.setError("Provide Password");
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password to short");
        }
        if (email.length() <= 6) {
            editTextEmail.setError("E-mail to short");

        }
        if (!hasErrors) {
            progressDialog.setMessage("Logging...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener < AuthResult > () {
                        @Override
                        public void onComplete(@NonNull Task < AuthResult > task) {
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                progressDialog.hide();
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));

                                ImageView image = layout.findViewById(R.id.image);
                                image.setImageResource(R.drawable.close);
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("Incorrect Password or login");

                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }
                        }
                    });
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void toastWindow(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.close);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
