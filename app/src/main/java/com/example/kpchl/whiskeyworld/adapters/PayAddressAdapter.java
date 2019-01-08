package com.example.kpchl.whiskeyworld.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.user_information.EditAddress;
import com.example.kpchl.whiskeyworld.using_classes.SingleAddressInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PayAddressAdapter extends ArrayAdapter<SingleAddressInfo> {
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    Context context;
    List<SingleAddressInfo>object;
    private String uid;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public class ViewHolder {
        TextView name_surname;
        TextView address;
        TextView phone;
        TextView email;
        TextView country;
        TextView postcode_city;

        String key;
    }

    public PayAddressAdapter(Context context, List<SingleAddressInfo> objects) {

        super(context, R.layout.single_pay_address, objects);
        this.object=objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;

        if(convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_pay_address, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name_surname = row.findViewById(R.id.name_surname);
            viewHolder.email= row.findViewById(R.id.email_);
            viewHolder.address= row.findViewById(R.id.address);
            viewHolder.phone= row.findViewById(R.id.phone);
            viewHolder.postcode_city= row.findViewById(R.id.postcode_city);

            viewHolder.country= row.findViewById(R.id.country_);

            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        SingleAddressInfo item = getItem(position);

        viewHolder.name_surname.setText(item.getName()+" "+item.getSurname());
        viewHolder.email.setText(item.getEmail());
        viewHolder.address.setText(item.getAddress());
        viewHolder.phone.setText(item.getPhoneNumber());
        viewHolder.postcode_city.setText(item.getPostCode()+", "+item.getCity());
        viewHolder.country.setText(item.getCountry());
        viewHolder.key = item.getKey();
        return row;
    }
}