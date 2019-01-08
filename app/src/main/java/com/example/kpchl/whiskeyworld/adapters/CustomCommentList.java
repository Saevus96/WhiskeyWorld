package com.example.kpchl.whiskeyworld.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.product.CommentsFragment;
import com.example.kpchl.whiskeyworld.using_classes.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomCommentList extends ArrayAdapter<Comment> {

    public class ViewHolder {
        TextView comment;
        TextView email;
        ImageView image;
    }

    public CustomCommentList(Context context, List<Comment> objects) {
        super(context, R.layout.single_comment, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        ViewHolder viewHolder;

        if(convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_comment, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.comment = row.findViewById(R.id.userComment);
            viewHolder.email= row.findViewById(R.id.userEmail);
            viewHolder.image= row.findViewById(R.id.userImage);

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
        return row;
    }
}