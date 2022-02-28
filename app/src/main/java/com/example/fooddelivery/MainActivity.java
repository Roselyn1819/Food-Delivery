package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.adapter.PizzaAdapter;
import com.example.fooddelivery.classes.Pizza;
import com.example.fooddelivery.classes.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Dialog dialog;
    private static ArrayList<Pizza> pizzaArrayList = new ArrayList<>();
    PizzaAdapter pizzaAdapter;
    ListView lvPizza;

    TextView tvName;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        lvPizza = findViewById(R.id.lvPizza);
        tvName = findViewById(R.id.tvName);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        getUser();
        showDialog();
        addPizza();

        pizzaAdapter = new PizzaAdapter(this, pizzaArrayList);
        lvPizza.setAdapter(pizzaAdapter);

        lvPizza.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = pizzaArrayList.get(i).getName();
                String desc = pizzaArrayList.get(i).getDescription();
                String price = pizzaArrayList.get(i).getPrice();
                int pic = pizzaArrayList.get(i).getImage();

                startActivity(new Intent(MainActivity.this, PizzaDetails.class)
                        .putExtra("name", name)
                        .putExtra("desc", desc)
                        .putExtra("price", price)
                        .putExtra("pic", pic));
            }
        });
    }

    private void getUser() {
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                tvName.setText("Welcome " + users.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addPizza() {
        pizzaArrayList.clear();

        Pizza p1 = new Pizza("Cheese", "80", "Cheesy pizza", R.drawable.pizza);
        Pizza p2 = new Pizza("Ham & Cheese", "100", "Ham & Cheesy pizza", R.drawable.pizza);
        Pizza p3 = new Pizza("Hawaiian", "100", "Hawaiian pizza", R.drawable.pizza);
        Pizza p4 = new Pizza("Pepperoni", "100", "Pepperoni pizza", R.drawable.pizza);
        Pizza p5 = new Pizza("Cheese Overload", "100", "Cheesy overload pizza", R.drawable.pizza);
        Pizza p6 = new Pizza("Ham & Cheese Overload", "100", "Ham and Cheesy pizza", R.drawable.pizza);
        Pizza p7 = new Pizza("Hawaiian Overload", "100", "Hawaiian pizza", R.drawable.pizza);
        Pizza p8 = new Pizza("Primo's supreme", "100", "Primo's pizza", R.drawable.pizza);

        pizzaArrayList.add(p1);
        pizzaArrayList.add(p2);
        pizzaArrayList.add(p3);
        pizzaArrayList.add(p4);
        pizzaArrayList.add(p5);
        pizzaArrayList.add(p6);
        pizzaArrayList.add(p7);
        pizzaArrayList.add(p8);
    }

    private void showDialog() {
        dialog.setContentView(R.layout.welcome_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void goToCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

    public void goToProfile(View view) {
        startActivity(new Intent(this,ProfileActivity.class));
    }
}