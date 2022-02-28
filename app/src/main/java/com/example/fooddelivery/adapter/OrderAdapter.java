package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;
import com.example.fooddelivery.classes.Order;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order> {
    Context context;
    private static ArrayList<Order> orderArrayList = new ArrayList<>();

    public OrderAdapter(@NonNull Context context, ArrayList<Order> orderArrayList) {
        super(context, R.layout.layout_order, orderArrayList);
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_order, null, false);
        TextView tvDateTime = view.findViewById(R.id.tvDateTime);
        TextView tvOrder = view.findViewById(R.id.tvOrder);
        TextView tvTotal = view.findViewById(R.id.tvTotal);

        String date = orderArrayList.get(position).getDate();
        String time = orderArrayList.get(position).getTime();

        tvDateTime.setText(date + " " + time);
        tvOrder.setText(orderArrayList.get(position).getOrders());
        tvTotal.setText(orderArrayList.get(position).getTotal());

        return view;
    }
}
