package com.example.shoppingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingproject.Model.Cart;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.example.shoppingproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProductBtn;
    private TextView txtTotalAmount, txtMsg1, txtTotalDeliver;

    private int overTotalPrice = 0;
//    private int overTotalDeliver = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProductBtn = (Button) findViewById(R.id.btn_next_product);
        txtTotalAmount = (TextView) findViewById(R.id.text_total_price);
        txtTotalDeliver = (TextView) findViewById(R.id.text_total_deliver);


        txtMsg1 = (TextView) findViewById(R.id.msg1);


        NextProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                txtTotalAmount.setText("Total Price = " + String.valueOf(overTotalPrice) + " ฿");

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice + 45));
                startActivity(intent);
                overTotalPrice = 0;
                finish();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {


                int oneTypeProductPrice = Integer.valueOf(model.getProductPrice()) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                NextProductBtn.setVisibility(View.VISIBLE);
                txtTotalDeliver.setText("฿" + String.valueOf(45));
                txtTotalAmount.setText("฿" + String.valueOf(overTotalPrice + 45));


//                Products products = dataSnapshot.getValue(Products.class);
                holder.btnEditQuantity.setNumber(model.getQuantity());


                holder.txtProductPrice.setText("ราคา " + "฿" + model.getProductPrice());
                holder.txtProductDescription.setText(model.getProductDescription());
                holder.txtProductName.setText(model.getProductName());
                Picasso.get().load(model.getProductImage()).into(holder.imageViewProduct);

//                Picasso.get().load(pImage).into(imageView);


                holder.layoutCartEdit.setVisibility(View.VISIBLE);

                holder.btnDeleteProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cartListRef.child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                .child(model.getProductID()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(CartActivity.this, "ลบสินค้าสำเร็จแล้ว", Toast.LENGTH_SHORT).show();

                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);

                                            return;
                                        }
                                    }
                                });


                    }
                });

                holder.btnEditQuantity.setRange(1, 50);
                holder.btnEditQuantity.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!holder.btnEditQuantity.getNumber().equals(model.getQuantity())) {

                            String number = holder.btnEditQuantity.getNumber();

                            cartListRef.child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                    .child(model.getProductID()).child("quantity").setValue(number);

                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                });

//                checkCart trueInCart
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getProductID());
                        intent.putExtra("checkLogin","LoginTrue");
                        intent.putExtra("checkCart","trueInCart");
                        finish();
                        startActivity(intent);

                    }
                });




            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(v);
                return holder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}




