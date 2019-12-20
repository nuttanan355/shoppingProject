package com.example.shoppingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingproject.Model.Products;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

        private Button addCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "0",state ="Normal";

//    private DatabaseReference productsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);



        productID = getIntent().getStringExtra("pid");

        addCartBtn=(Button) findViewById(R.id.btn_add_to_cart);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);

        productImage = (ImageView) findViewById(R.id.product_image_details);

        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        getProductDetails();
        
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();

                if (state.equals("Order Placed")||state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"สามารถซื้อสินค้าเพิ่มเติมคำสั่งซื้อของคุณจะถูกส่งหรือยืนยัน",Toast.LENGTH_LONG).show();
                }
                else
                    {
                        addingToCartList();
                    }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderSate();
    }

    private void addingToCartList() {

        String saveCurrentDate,saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("dd MMM yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount",productDescription.getText().toString());

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                  .child("Products").child(productID)
                                  .updateChildren(cartMap)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){
                                              Toast.makeText(ProductDetailsActivity.this,"เพิ่มในรถเข็นแล้ว",Toast.LENGTH_SHORT).show();

                                              Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                              startActivity(intent);
                                          }
                                      }
                                  });
                      }
                    }
                });
    }

    private void getProductDetails() {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {

                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProductDetailsActivity.this,"ERROR",Toast.LENGTH_SHORT).show();


            }
        });
    }
    private void CheckOrderSate(){
        DatabaseReference orderRef;
        orderRef =FirebaseDatabase.getInstance().getReference()
                .child("Order")
                .child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String shippingstate = dataSnapshot.child("state").getValue().toString();
//                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingstate.equals("shipped"))
                    {
                        state ="Order Shipped";
                    }
                    else if(shippingstate.equals("not shipped"))
                    {
                        state ="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
