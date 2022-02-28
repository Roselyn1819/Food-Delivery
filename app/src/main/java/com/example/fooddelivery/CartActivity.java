package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewOnReceiveContentListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.adapter.CartAdapter;
import com.example.fooddelivery.classes.CartItem;
import com.example.fooddelivery.classes.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference, orderReference;
    String uid;
    TextView tvNoData;

    ListView lvCart;
    CartAdapter cartAdapter;
    private static ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
    TextView tvTotal;
    String orders = "";
    int _total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvNoData = findViewById(R.id.tvNoData);
        lvCart = findViewById(R.id.lvCart);
        tvTotal = findViewById(R.id.tvTotal);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        uid = firebaseUser.getUid();

        getCartItems();
    }

    private void getCartItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemArrayList.clear();
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                        cartItemArrayList.add(cartItem);

                    }
                    cartAdapter = new CartAdapter(CartActivity.this, cartItemArrayList);
                    lvCart.setAdapter(cartAdapter);

                } else {

                }
                if (cartItemArrayList.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                    lvCart.setVisibility(View.GONE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                    lvCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getTotal(View view) {
        if (cartItemArrayList.isEmpty()) {
            Toast.makeText(this, "Add items to cart first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < cartItemArrayList.size(); i++) {
            int total = cartItemArrayList.get(i).getTotal();

            int qty = cartItemArrayList.get(i).getQty();

            String order = cartItemArrayList.get(i).getName() + " (" + qty + ")";
            orders = orders + order + "\n";
            _total = _total + total;

        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.order_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView tvOrders, tvTotal;

        tvOrders = dialog.findViewById(R.id.orderSummary);
        tvTotal = dialog.findViewById(R.id.tvTotal);

        tvOrders.setText(orders);
        tvTotal.setText(String.valueOf("TOTAL: " + _total));

        String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Button btnPlaceOrder = dialog.findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderReference = FirebaseDatabase.getInstance().getReference("orders").child(uid);
                Order order = new Order(currentDate, currentTime, orders, String.valueOf(_total));

                orderReference.child(currentDate + " " + currentTime).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.removeValue();
                        Toast.makeText(CartActivity.this, "Order placed!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });


    }
}