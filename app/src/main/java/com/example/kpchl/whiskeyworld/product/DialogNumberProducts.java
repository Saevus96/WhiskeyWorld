package com.example.kpchl.whiskeyworld.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.using_classes.AddToCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DialogNumberProducts extends DialogFragment {
    //Tag dla komunikacji pomiedzy fragmentami
    private static final String TAG = "MyCustomDialog";

    //dane wyciagniete z bazy danych
    private String imageUrl;
    private String whiskeyName;
    private double price;

    //obsluga dla danych uzytkownika
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;

    // obsluga bazy danych firebase
    private DatabaseReference myRefx;
    private DatabaseReference myRefy;
    private DatabaseReference myRefz;
    private DatabaseReference myRefImage;
    private int existingValue = 0;

    //obsluga kontrolek
    private EditText mInput;
    private TextView mActionOk, mActionCancel;

    // wysylanie wyniku do fragmentu
    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_number_products, container, false);

        //wyciagniecie wartosci z poprzedniego fragmentu
        whiskeyName = getArguments().getString("name");

        //znalezienie kontrolek w layoucie
        mActionOk = view.findViewById(R.id.action_ok);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mInput = view.findViewById(R.id.input);

        //pobranie uid dla uzytkownika
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        //Referencje do wyszukania wartosci w bazie danych
        myRefx = FirebaseDatabase.getInstance().getReference("ShopCards");
        myRefy = myRefx.child(uid);
        myRefz = myRefy.child(whiskeyName);
        myRefImage = FirebaseDatabase.getInstance().getReference("Products").child(whiskeyName);

        //wyszukanie zdjęcia produktu
        myRefImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              imageUrl = dataSnapshot.child("icon").getValue(String.class);
              price = dataSnapshot.child("price").getValue(double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //sprawdzenie czy uzytkownik dodal juz do koszyka produkty jesli tak to pobiera istniejaca wartosc
        myRefz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    existingValue = dataSnapshot.child("numberOfProduct").getValue(int.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //obsluga przycisku cancel
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });
        //obsluga przycisku ok
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sprawdzenie poprawnosci  wartosci oraz stworzenie zmodyfikowanego Toasta
                String userString = mInput.getText().toString().trim();
                if(userString.isEmpty()){
                    LayoutInflater inflater = getLayoutInflater();

                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) view.findViewById(R.id.toast_layout_root));

                    ImageView image = (ImageView) layout.findViewById(R.id.image);
                    Picasso.get().load(imageUrl).into(image);
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("You can't add 0 bottles to your shopping cart");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    int userValue = Integer.parseInt(userString);
                    if (userValue > 10) {
                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        ImageView image = layout.findViewById(R.id.image);
                        Picasso.get().load(imageUrl).into(image);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("You can put into cart only 10 bottles");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }else if(userValue == 0) {
                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        ImageView image = (ImageView) layout.findViewById(R.id.image);
                        Picasso.get().load(imageUrl).into(image);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("You can't add 0 bottles to your shopping cart");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }else if (existingValue == 10) {
                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        ImageView image = (ImageView) layout.findViewById(R.id.image);
                        Picasso.get().load(imageUrl).into(image);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("You already have 10 bottles in Cart");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else {
                        // wyslanie prawidlowych wartosci do bazy
                        AddToCart addToCart = new AddToCart(userValue + existingValue, whiskeyName, imageUrl, price);
                        myRefz.setValue(addToCart);

                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        ImageView image = (ImageView) layout.findViewById(R.id.image);
                        Picasso.get().load(imageUrl).into(image);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(userValue + existingValue + " bottles in your cart");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                    }
                }
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){

        }
    }

}
