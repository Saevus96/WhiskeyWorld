package com.example.kpchl.whiskeyworld.authorization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
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

public class Register extends AppCompatActivity  implements View.OnClickListener {
    private CardView register;
    private TextInputLayout editTextRepeatPassword;
    private TextInputLayout editTextEmail;
    private TextInputLayout editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    private ImageView logo;
    private CardView dataCard;
    private ImageButton facebookButton;
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        if(!isOnline()){
            finish();
            toastWindow("You don't have internet connection!");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        register = findViewById(R.id.registerCard);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        register.setOnClickListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        logo = findViewById(R.id.logo);
        dataCard = findViewById(R.id.dataCard);
       // login = findViewById(R.id.loginCard);
        facebookButton = findViewById(R.id.customFacebook);
        aSwitch = findViewById(R.id.switch1);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mCallbackManager = CallbackManager.Factory.create();
        Animation rotateimage = AnimationUtils.loadAnimation(Register.this,android.R.anim.slide_in_left);
        facebookButton.startAnimation(rotateimage);
        logo.startAnimation(rotateimage);
        dataCard.startAnimation(rotateimage);
        register.startAnimation(rotateimage);
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
                LoginManager.getInstance().logInWithReadPermissions(Register.this, Arrays.asList("email", "public_profile"));
                progressDialog.setMessage("Logging...");
                progressDialog.show();
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
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
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

   public void registerUser(){
        String email = editTextEmail.getEditText().getText().toString();
        String password = editTextPassword.getEditText().getText().toString();
        String repeatPassword = editTextRepeatPassword.getEditText().getText().toString();

       if(!isOnline()){
           finish();
           toastWindow("You don't have internet connection!");
           return;
       }
        boolean hasErrors =false;
        if(TextUtils.isEmpty(email)){
            hasErrors = true;
            editTextEmail.setError("No E-mail");
        }
        if(!repeatPassword.equals(password)){
            editTextRepeatPassword.setError("Bad repeat");
            hasErrors = true;
        }
        if(!aSwitch.isChecked()){
            hasErrors = true;
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.toast_layout_root));

            ImageView image = layout.findViewById(R.id.image);
            image.setImageResource(R.drawable.close);
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Accept rules");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
        if(TextUtils.isEmpty(password)){
            hasErrors = true;
            editTextPassword.setError("No password");
        }
        if(password.length()<6){
            hasErrors = true;
            editTextPassword.setError("Password to short. Password should contain 6 characters");
        }
        if(!email.contains("@"))
        {
            hasErrors = true;
            editTextEmail.setError("Incorectly E-mail");
        }
        if(email.length()<=6){
            hasErrors = true;
            editTextEmail.setError("Incorrect E-mail");

        }
        if(!hasErrors) {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();

            //Stworzenie uzytkownika za pomoca formularza rejestracyjnego.
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        updateUI();
                    }
                    else{

                        progressDialog.hide();
                        LayoutInflater inflater = getLayoutInflater();
                        editTextEmail.setErrorEnabled(false);
                        editTextPassword.setErrorEnabled(false);
                        editTextRepeatPassword.setErrorEnabled(false);
                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.toast_layout_root));

                        ImageView image = layout.findViewById(R.id.image);
                        image.setImageResource(R.drawable.close);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("You Can not register. User exist");

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
    @Override
    public void onClick(View v) {
        if(v == register){
            registerUser();
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
