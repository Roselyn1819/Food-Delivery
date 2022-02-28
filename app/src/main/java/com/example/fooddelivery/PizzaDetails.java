package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddelivery.classes.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PizzaDetails extends AppCompatActivity {
    TextView tvName, tvPrice, tvDesc;
    ImageView ivPizza;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    String name, price, photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_details);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDesc = findViewById(R.id.tvDesc);
        ivPizza = findViewById(R.id.ivPizza);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        photo = String.valueOf(intent.getIntExtra("pic", 0));
        tvName.setText(name);
        tvPrice.setText(price);
        tvDesc.setText(intent.getStringExtra("desc"));
        Glide.with(this).load(intent.getIntExtra("pic", 0)).into(ivPizza);
    }

    public void addToCart(View view) {
        String uid = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(uid).child(name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(PizzaDetails.this, "This item is already in your cart.", Toast.LENGTH_SHORT).show();
                } else {
                    int _photo = Integer.parseInt(photo);
                    int _price = Integer.parseInt(price);
                    CartItem cartItem1 = new CartItem(name, _photo, _price);
                    databaseReference.setValue(cartItem1);

                    Toast.makeText(PizzaDetails.this, "Added item to cart.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}