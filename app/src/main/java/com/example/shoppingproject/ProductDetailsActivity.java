package com.example.shoppingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout layoutProductDetails;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName,titleProductName;
    private String productID = "0", inCart = "",checkLogin="";

//    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        productID = getIntent().getStringExtra("pid");

        checkLogin = getIntent().getExtras().get("checkLogin").toString();

        inCart = getIntent().getExtras().get("checkCart").toString();



layoutProductDetails=(LinearLayout)findViewById(R.id.layout_product_details);

        addCartBtn = (Button) findViewById(R.id.btn_add_to_cart);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);

        productImage = (ImageView) findViewById(R.id.product_image_details);


        titleProductName =(TextView)findViewById(R.id.title_product_name_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);





        getProductDetails();

        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (checkLogin.equals("LoginTrue")) {
                   addingToCartList();
               }else
                   {
                       Toast.makeText(ProductDetailsActivity.this, "เข้าสู่ระบบก่อน", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                       startActivity(intent);
                       finish();

               }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (inCart.equals("trueInCart"))
        {
            addCartBtn.setVisibility(View.GONE);
            layoutProductDetails.setVisibility(View.GONE);


        }


//        CheckOrderSate();
    }

    private void addingToCartList() {

        String saveCurrentDate, saveCurrentTime;
        String ListRandomKay;

        final DatabaseReference productImageRef = FirebaseDatabase.getInstance()
                .getReference().child("Products")
                .child(productID).child("ProductImage");

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance()
                .getReference().child("Cart List")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        ListRandomKay = saveCurrentDate + "," + saveCurrentTime;

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("ProductID", productID);
        cartMap.put("ProductName", productName.getText().toString());
        cartMap.put("ProductPrice", productPrice.getText().toString());
//        cartMap.put("image","");
//        cartMap.put("date", saveCurrentDate);
//        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("ProductDescription", productDescription.getText().toString());


        productImageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartListRef.child("ProductImage").setValue(dataSnapshot.getValue());

                cartListRef.updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ProductDetailsActivity.this, "เพิ่มในรถเข็นแล้ว", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            intent.putExtra("CheckLogin", "LoginTrue");
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getProductDetails() {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Products products = dataSnapshot.getValue(Products.class);
                    titleProductName.setText(products.getProductName());
                    productName.setText(products.getProductName());
                    productPrice.setText(products.getProductPrice());
                    productDescription.setText(products.getProductDescription());
                    Picasso.get().load(products.getProductImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProductDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();


            }
        });
    }

//    private void CheckOrderSate() {
//        DatabaseReference orderRef;
//        orderRef = FirebaseDatabase.getInstance().getReference()
//                .child("Orders")
//                .child(Prevalent.currentOnlineUser.getPhone());
//        orderRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String shippingstate = dataSnapshot.child("state").getValue().toString();
////                    String userName = dataSnapshot.child("userName").getValue().toString();
//
//                    if (shippingstate.equals("shipped")) {
//                        state = "Order Shipped";
//                    } else if (shippingstate.equals("not shipped")) {
//                        state = "Order Placed";
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
