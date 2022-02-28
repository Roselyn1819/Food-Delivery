package com.example.fooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.R;
import com.example.fooddelivery.classes.CartItem;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {
    Context context;
    int qty;
    int price;
    private static ArrayList<CartItem> cartItemArrayList = new ArrayList<>();

    public CartAdapter(@NonNull Context context, ArrayList<CartItem> cartItemArrayList) {
        super(context, R.layout.layout_cart_item, cartItemArrayList);
        this.context = context;
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item, null, false);
        ImageView ivPizza = view.findViewById(R.id.ivPizza);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvPrice = view.findViewById(R.id.tvPrice);

        qty = 1;
        price = cartItemArrayList.get(position).getPrice();
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnMinus = view.findViewById(R.id.btnMinus);
        TextView tvQty = view.findViewById(R.id.tvQty);

        tvQty.setText(String.valueOf(qty));
        tvPrice.setText(String.valueOf(price));
        cartItemArrayList.get(position).setTotal(qty * price);
        cartItemArrayList.get(position).setQty(qty);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty < 5) {
                    qty++;
                    tvQty.setText(String.valueOf(qty));
                    tvPrice.setText(String.valueOf(qty * price));
                    cartItemArrayList.get(position).setTotal(qty * price);
                    cartItemArrayList.get(position).setQty(qty);
                } else {
                    Toast.makeText(context, "Max of 5 orders per item", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty == 1) {
                    Toast.makeText(context, "Quantity must not be less than 1", Toast.LENGTH_SHORT).show();
                } else {
                    qty--;
                    tvQty.setText(String.valueOf(qty));
                    tvPrice.setText(String.valueOf(qty * price));
                    cartItemArrayList.get(position).setTotal(qty * price);
                    cartItemArrayList.get(position).setQty(qty);
                }
            }
        });

        Glide.with(context).load(cartItemArrayList.get(position).getPhoto()).into(ivPizza);

        tvName.setText(cartItemArrayList.get(position).getName());

        return view;
    }

}
