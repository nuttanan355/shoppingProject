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
    private TextView txtTotalAmount, txtMsg1,txtTotalDeliver;

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
        txtTotalDeliver=(TextView) findViewById(R.id.text_total_deliver);



        txtMsg1 = (TextView) findViewById(R.id.msg1);



        NextProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                txtTotalAmount.setText("Total Price = " + String.valueOf(overTotalPrice) + " ฿");

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice+45));
                startActivity(intent);
                overTotalPrice=0;
                finish();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();



//        CheckOrderSate();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {


//                Products products = dataSnapshot.getValue(Products.class);
                holder.btnEditQuantity.setNumber(model.getQuantity());


                holder.txtProductPrice.setText("ราคา "+ "฿" + model.getPrice());
                holder.txtProductDescription.setText(model.getDiscount());
                holder.txtProductName.setText(model.getPname());
                Picasso.get().load(model.getImage()).into(holder.imageViewProduct);

//                Picasso.get().load(pImage).into(imageView);

                int oneTypeProductPrice = Integer.valueOf(model.getPrice()) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;


                NextProductBtn.setVisibility(View.VISIBLE);

                txtTotalDeliver.setText("฿" + String.valueOf(45));
                txtTotalAmount.setText("฿" + String.valueOf(overTotalPrice+45));


                holder.layoutCartEdit.setVisibility(View.VISIBLE);

                holder.btnDeleteProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cartListRef.child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                .child(model.getPid()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(CartActivity.this, "ลบสินค้าสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
//                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
//
//                                                        startActivity(intent);
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

                        String number = holder.btnEditQuantity.getNumber();

                        cartListRef.child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                .child(model.getPid()).child("quantity").setValue(number);

                    }
                });



//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CharSequence options[] = new CharSequence[]{
//
//                                "แก้ไขจำนวน",
//                                "ลบออก"
//                        };
//                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
//                        builder.setTitle("ตัวเลือกรถเข็น :");
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if (i == 0) {
//                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
//                                    intent.putExtra("CheckLogin", "LoginTrue");
//                                    intent.putExtra("pid", model.getPid());
//                                    startActivity(intent);
//                                }
//                                if (i == 1) {
////                                    cartListRef
////                                            .child(Prevalent.currentOnlineUser.getPhone())
////                                            .child("Products")
////                                            .child(model.getPid())
////                                            .removeValue()
////                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                @Override
////                                                public void onComplete(@NonNull Task<Void> task) {
////
////                                                    if (task.isSuccessful()) {
////                                                        Toast.makeText(CartActivity.this, "ลบสินค้าสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
//////                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
//////                                                        startActivity(intent);
////                                                    }
////                                                }
////                                            });
//                                }
//
//                            }
//                        });
//                        builder.show();
//                    }
//                });
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

    private void CheckOrderSate() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingstate = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingstate.equals("shipped")) {
                        txtTotalAmount.setText("คำสั่งซื้อ" + userName + "\n จัดส่งเรียบร้อยแล้ว");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("ขอแสดงความยินดีการสั่งซื้อขั้นสุดท้ายของคุณได้รับการจัดส่งเรียบร้อยแล้ว");
                        NextProductBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "สามารถซื้อสินค้าเพิ่มเติมได้, เมื่อได้รับคำสั่งซื้อครั้งสุดท้าย", Toast.LENGTH_SHORT).show();
                    } else if (shippingstate.equals("not shipped")) {
                        txtTotalAmount.setText("สถานะการจัดส่ง = ไม่ได้จัดส่ง");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProductBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "สามารถซื้อสินค้าเพิ่มเติมได้, เมื่อได้รับคำสั่งซื้อครั้งสุดท้าย", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
