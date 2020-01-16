package com.example.shoppingproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Model.Cart;
import com.example.shoppingproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdersHistoryActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ordersRef;

    private String oederID = "";
    private TextView txtOrederName, txtOrederPhone, txtOrederAddress, txtOrederTotal,txtOrederPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        txtOrederName = findViewById(R.id.user_history_order_name);
        txtOrederPhone = findViewById(R.id.user_history_order_phone);
        txtOrederAddress = findViewById(R.id.user_history_order_address);
        txtOrederTotal = findViewById(R.id.user_history_order_total);
        txtOrederPackage = findViewById(R.id.user_history_order_package);

        oederID = getIntent().getStringExtra("oid");

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(oederID);

        showOrders();
        showOrderList();




    }

    private void showOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String oName = dataSnapshot.child("name").getValue().toString();
                String oPhone = dataSnapshot.child("phone").getValue().toString();
                String oAddress = dataSnapshot.child("address").getValue().toString();
                String oTotal = dataSnapshot.child("totalAmount").getValue().toString();

                String oPackage = dataSnapshot.child("package").getValue().toString();


                txtOrederName.setText(oName);
                txtOrederPhone.setText(oPhone);
                txtOrederAddress.setText(oAddress);
                txtOrederTotal.setText(oTotal);
                txtOrederPackage.setText(oPackage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showOrderList() {
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(ordersRef.child("orderList"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("จำนวน " + model.getQuantity());
                holder.txtProductPrice.setText("ราคา = " + model.getPrice() + " ฿");
                holder.txtProductDescription.setText(model.getDiscount());
                holder.txtProductName.setText(model.getPname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(v);
                return holder;
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}
