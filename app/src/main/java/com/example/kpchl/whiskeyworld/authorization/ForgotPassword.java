package com.example.kpchl.whiskeyworld.authorization;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private CardView remind;
    private TextInputLayout editTextEmail;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setTitle("");

        progressDialog = new ProgressDialog(this);
        editTextEmail = findViewById(R.id.et_email);
        remind = findViewById(R.id.remind);

        firebaseAuth = FirebaseAuth.getInstance();

        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Sending remind..");
                progressDialog.show();
                if(!editTextEmail.getEditText().getText().toString().isEmpty()) {
                    firebaseAuth.sendPasswordResetEmail(editTextEmail.getEditText().getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        toastWindow("Your password has succesfully reminded." +
                                                "Check Your E-mail",R.drawable.accepted);
                                        finish();
                                    } else if (!task.isSuccessful()) {
                                        progressDialog.hide();
                                        toastWindow("User don't exist", R.drawable.close);
                                    }
                                }
                            });
                }else{
                    progressDialog.hide();
                    editTextEmail.setError("Provide E-mail");
                }
            }
        });
    }

    public void toastWindow(String message, int drawable){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(drawable);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
