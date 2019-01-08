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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AddressCustomList extends ArrayAdapter<SingleAddressInfo> {
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
        ImageView popupImage;
        String key;
    }

    public AddressCustomList(Context context, List<SingleAddressInfo> objects) {

        super(context, R.layout.single_list_address, objects);
        this.object=objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;

        if(convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_list_address, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name_surname = row.findViewById(R.id.name_surname);
            viewHolder.email= row.findViewById(R.id.email_);
            viewHolder.address= row.findViewById(R.id.address);
            viewHolder.phone= row.findViewById(R.id.phone);
            viewHolder.postcode_city= row.findViewById(R.id.postcode_city);
            viewHolder.popupImage= row.findViewById(R.id.popupImage);
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

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("Addresses");
        myRefy = myRefx.child(uid);

        viewHolder.popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                popup.getMenuInflater().inflate(R.menu.popup_menu,
                        popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:

                                Intent intent = new Intent(getApplicationContext(), EditAddress.class);
                                intent.putExtra("key", viewHolder.key);
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);


                                break;
                            case R.id.delete:
                                notifyDataSetChanged();
                                myRefy.child(viewHolder.key).setValue(null);
                                object.remove(position);
                                AddressCustomList.this.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
            }
        });
        return row;
    }
}