package com.example.kpchl.whiskeyworld.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kpchl.whiskeyworld.R;
import com.example.kpchl.whiskeyworld.user_information.MyOrderInformation;
import com.example.kpchl.whiskeyworld.user_information.MyOrders;
import com.example.kpchl.whiskeyworld.using_classes.OrderClass;
import com.example.kpchl.whiskeyworld.using_classes.SingleCreditCardInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MyOrdersAdapter extends ArrayAdapter<OrderClass> {
    DatabaseReference myRefx;
    DatabaseReference myRefy;
    Context context;
    List<OrderClass> object;
    private String uid;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public class ViewHolder {
        TextView date;
        TextView status;
        TextView cost;

    }

    public MyOrdersAdapter(Context context, List<OrderClass> objects) {

        super(context, R.layout.single_order, objects);
        this.object = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;

        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.single_order, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.date = row.findViewById(R.id.orderDate);
            viewHolder.status = row.findViewById(R.id.orderStatus);
            viewHolder.cost = row.findViewById(R.id.orderCost);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        final OrderClass item = getItem(position);

        viewHolder.date.setText(item.getOrderDate());
        viewHolder.status.setText(item.getStatus());
        viewHolder.cost.setText(item.getPayValue());

        if(item.getStatus().equals("PENDING")){
            viewHolder.status.setTextColor(Color.RED);
        }else{
            viewHolder.status.setTextColor(Color.parseColor("#33cc66"));
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), item.getOrderDate(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MyOrderInformation.class);
                intent.putExtra("orderDate", item.getOrderDate());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });
        return row;
    }
}
