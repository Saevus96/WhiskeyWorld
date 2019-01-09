package com.example.kpchl.whiskeyworld.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.product.CommentsFragment;
import com.example.kpchl.whiskeyworld.using_classes.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomCommentList extends ArrayAdapter<Comment> {
    private String productName;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String email;
    private String uid;
    DatabaseReference myRefx;
    private  List<Comment>objects;
    public class ViewHolder {
        TextView comment;
        TextView email;
        ImageView image;
        TextView delete;
    }

    public CustomCommentList(Context context, List<Comment> objects, String productName) {
        super(context, R.layout.single_comment, objects);
        this.productName = productName;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;

        if(convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_comment, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.comment = row.findViewById(R.id.userComment);
            viewHolder.email= row.findViewById(R.id.userEmail);
            viewHolder.image= row.findViewById(R.id.userImage);
            viewHolder.delete = row.findViewById(R.id.deleteCom);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        Comment item = getItem(position);
        viewHolder.comment.setText(item.getComment());
        viewHolder.email.setText(item.getEmail());


        if(item.getProvider().equals("facebook.com"))
        {
            Picasso.get().load(item.getUserUrl()).into(viewHolder.image);

        }
        else{
            viewHolder.image.setImageResource(R.drawable.person);
        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        email = currentUser.getEmail();
        myRefx = FirebaseDatabase.getInstance().getReference("Products")
                .child(productName).child("Comments");

        if(email.equals(item.getEmail())){
            viewHolder.delete.setVisibility(View.VISIBLE);
        }else{
            viewHolder.delete.setVisibility(View.GONE);
        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRefx.child(uid).setValue(null);
                objects.remove(position);
                CustomCommentList.this.notifyDataSetChanged();
            }
        });
        return row;
    }
}