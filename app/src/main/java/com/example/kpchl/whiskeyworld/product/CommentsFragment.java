package com.example.kpchl.whiskeyworld.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.adapters.CustomCommentList;
import com.example.kpchl.whiskeyworld.adapters.CustomList;
import com.example.kpchl.whiskeyworld.using_classes.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CommentsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Comments";


    ArrayList<Comment> commentList = new ArrayList<Comment>();
    private String whiskeyName;
    private ArrayList<String> commentsList = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    DatabaseReference myRefz;
    DatabaseReference myRefi;
    private TextView btnAddComment;
    private EditText commentText;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String email;
    private String uid;
    private ListView commentListView;
    private ArrayAdapter<String> commentAdapter;
    private String string;
    private String string2;
    private String string3;
    private String string4;
    private String provider;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private CustomCommentList customCommentList;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comments, container, false);

        whiskeyName = getArguments().getString("name");

        btnAddComment = view.findViewById(R.id.commentButton);
        commentText = view.findViewById(R.id.commentText);

        myRefx = FirebaseDatabase.getInstance().getReference("Products");
        myRefy = myRefx.child(whiskeyName);

        btnAddComment.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        email = currentUser.getEmail();
        myRefz = myRefy.child("Comments");

        commentListView = view.findViewById(R.id.commentListView);
        commentAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, commentsList);
       // commentListView.setAdapter(commentAdapter);
        customCommentList = new CustomCommentList(getContext(), commentList);
        commentListView.setAdapter(customCommentList);
       mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                customCommentList = new CustomCommentList(getContext(), commentList);
                commentListView.setAdapter(customCommentList);
                mySwipeRefreshLayout.setRefreshing(false);


            }
        });


        myRefy.child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                string = dataSnapshot.child("email").getValue(String.class);
                string2 = dataSnapshot.child("comment").getValue(String.class);
                string3 = dataSnapshot.child("userUrl").getValue(String.class);
                string4 = dataSnapshot.child("provider").getValue(String.class);
                commentList.add(new Comment(string2, string,string3,string4 ));
                customCommentList.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;

    }

    @Override
    public void onClick(View v) {
        if (v == btnAddComment) {
            addComent();
        }
    }

    private void update(){


    }
    private void addComent() {
        if(!currentUser.isAnonymous()){
        provider = currentUser.getProviders().get(0);}
        myRefz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    LayoutInflater inflater = getLayoutInflater();

                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) view.findViewById(R.id.toast_layout_root));

                    ImageView image = (ImageView) layout.findViewById(R.id.image);
                    //Picasso.get().load(imageUrl).into(image);
                    image.setImageResource(R.drawable.ic_comments);
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("You have already commented on this product");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    commentText.setText("");

                } else {

                    String comment = commentText.getText().toString();
                    if(!comment.isEmpty()) {
                        if(currentUser.isAnonymous()){
                            commentText.setText("");
                            LayoutInflater inflater = getLayoutInflater();

                            View layout = inflater.inflate(R.layout.toast,
                                    (ViewGroup) view.findViewById(R.id.toast_layout_root));

                            ImageView image = (ImageView) layout.findViewById(R.id.image);
                            image.setImageResource(R.drawable.ic_comments);
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("You can not add comments as guest");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }else{
                        if(provider.equals("facebook.com")) {
                            Comment uComment = new Comment(comment, email,currentUser.getPhotoUrl().toString(),provider);
                            myRefz.child(uid).setValue(uComment);
                        }else{
                            Comment uComment = new Comment(comment, email,"empty",provider);
                            myRefz.child(uid).setValue(uComment);
                        }
                        commentText.setText("");
                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        ImageView image = (ImageView) layout.findViewById(R.id.image);
                        image.setImageResource(R.drawable.ic_comments);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("Thank you for your opinion");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                    }else{
                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        ImageView image = (ImageView) layout.findViewById(R.id.image);
                        image.setImageResource(R.drawable.ic_comments);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("Comment can not be empty");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}