package com.example.kpchl.whiskeyworld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.using_classes.SingleCreditCardInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CreditCardCustomList extends ArrayAdapter<SingleCreditCardInfo> {
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    Context context;
    List<SingleCreditCardInfo>object;
    private String uid;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public class ViewHolder {
        TextView last4;
        TextView expire;
        ImageView brand;
        String key;
        ImageView popupImage;
    }

    public CreditCardCustomList(Context context, List<SingleCreditCardInfo> objects) {

        super(context, R.layout.single_list_creditcard, objects);
        this.object=objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;

        if(convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_list_creditcard, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.last4 = row.findViewById(R.id.last4);
            viewHolder.expire= row.findViewById(R.id.expire);
            viewHolder.brand= row.findViewById(R.id.brand);

            viewHolder.popupImage= row.findViewById(R.id.popupImage);

            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        SingleCreditCardInfo item = getItem(position);
        viewHolder.key = item.getKey();
                viewHolder.last4.setText(item.getLast4());
        viewHolder.expire.setText(item.getExp_month()+"/"+item.getExp_year());
        if(item.getBrand().equals("Visa")){
            viewHolder.brand.setImageResource(R.drawable.visa);
        }else if(item.getBrand().equals("MasterCard")){
            viewHolder.brand.setImageResource(R.drawable.mastercard);
        }

        //viewHolder.key = item.getKey();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        myRefx = FirebaseDatabase.getInstance().getReference("stripe_customers");
        myRefy = myRefx.child(uid);

        viewHolder.popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                popup.getMenuInflater().inflate(R.menu.credit_card_popup_menu,
                        popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                notifyDataSetChanged();
                                myRefy.child("sources").child(viewHolder.key).setValue(null);
                                object.remove(position);
                                CreditCardCustomList.this.notifyDataSetChanged();
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