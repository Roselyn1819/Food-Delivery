package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.classes.Pizza;

import java.util.ArrayList;
import java.util.Locale;

public class PizzaAdapter extends ArrayAdapter<Pizza> {
    Context context;
    private static ArrayList<Pizza> pizzaArrayList = new ArrayList<>();

    public PizzaAdapter(@NonNull Context context, ArrayList<Pizza> pizzaArrayList) {
        super(context, R.layout.layout_pizza, pizzaArrayList);
        this.context = context;
        this.pizzaArrayList = pizzaArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pizza, null, false);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        ImageView ivPizza = view.findViewById(R.id.ivPizza);

        tvName.setText(pizzaArrayList.get(position).getName().toUpperCase());
        tvPrice.setText(pizzaArrayList.get(position).getPrice().toUpperCase());
        Glide.with(context).load(pizzaArrayList.get(position).getImage()).into(ivPizza);
        return view;
    }
}
