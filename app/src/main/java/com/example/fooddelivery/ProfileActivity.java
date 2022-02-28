package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fooddelivery.adapter.OrderAdapter;
import com.example.fooddelivery.classes.Order;
import com.example.fooddelivery.classes.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    TextView tvName, tvAddress, tvContact,tvNoData;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference, orderReference;
    private static ArrayList<Order> orderArrayList = new ArrayList<Order>();
    ListView lvOrders;
    OrderAdapter orderAdapter;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvContact = findViewById(R.id.tvContact);
        lvOrders = findViewById(R.id.lvOrders);
        tvNoData=findViewById(R.id.tvNoData);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        orderReference = FirebaseDatabase.getInstance().getReference("orders").child(uid);

        getUser();
        getOrders();
    }

    private void getOrders() {
        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    orderArrayList.add(order);
                }
                orderAdapter = new OrderAdapter(ProfileActivity.this, orderArrayList);
                lvOrders.setAdapter(orderAdapter);

                if (orderArrayList.isEmpty()){
                    tvNoData.setVisibility(View.VISIBLE);
                    lvOrders.setVisibility(View.GONE);
                }else {
                    tvNoData.setVisibility(View.GONE);
                    lvOrders.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                tvName.setText(users.getName().toUpperCase());
                tvContact.setText("Contact: " + users.getContact());
                tvAddress.setText("Address: " + users.getAddress().toUpperCase());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void logout(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Logout");
        alert.setMessage("You sure you want to logout?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent newIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogInterface.dismiss();
                startActivity(newIntent);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();


    }
}