package com.example.shoppingproject.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditProductsActivity extends AppCompatActivity {
    private Button btnApplyChange, btnDelete;
    private EditText name, price, description;
    private ImageView imageView;

//    private static final int GalleryPick = 1;
//    private Uri ImageUri;

    private String productID = "0";
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_products);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        //EditText
        name = findViewById(R.id.txt_edit_product_name);
        price = findViewById(R.id.txt_edit_product_price);
        description = findViewById(R.id.txt_edit_product_description);
        imageView = findViewById(R.id.txt_edit_product_image);

        //BUTTON
        btnDelete = findViewById(R.id.btn_delete_product);
        btnApplyChange = findViewById(R.id.btn_apply_changes);


        displaySpecificProductInfo();


        btnApplyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChange();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenGallery();
//            }
//        });

    }

    private void deleteProduct() {

        CharSequence options[] = new CharSequence[]
                {
                        "ใช่",
                        "ไม่"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditProductsActivity.this);
        builder.setTitle("ต้องการลบสินค้านี้ใช่ไหม ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (position == 0) {
                    productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(AdminEditProductsActivity.this, AdminHomeActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(AdminEditProductsActivity.this, "ลบสินค้าสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
                if (position == 1) {
                    finish();
                }
            }
        });
        builder.show();

    }

//    private void OpenGallery() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, GalleryPick);
//
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
//            ImageUri = data.getData();
////            InputProductImage.setI
//            imageView.setImageURI(ImageUri);
//
//        }
//
//    }


    private void ApplyChange() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals("")) {
            Toast.makeText(this, "กรุณาใส่ชื่อสินค้า", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {
            Toast.makeText(this, "กรุณาใส่ราคาสินค้า", Toast.LENGTH_SHORT).show();
        } else if (pDescription.equals("")) {
            Toast.makeText(this, "กรุณาใส่รายละเอียดสินค้า", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("ProductID", productID);
            productMap.put("ProductDescription", pDescription);
            productMap.put("ProductPrice", pPrice);
            productMap.put("ProductName", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditProductsActivity.this, "การเปลี่ยนแปลงสำเร็จ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminEditProductsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String pName = dataSnapshot.child("ProductName").getValue().toString();
                    String pPrice = dataSnapshot.child("ProductPrice").getValue().toString();
                    String pDescription = dataSnapshot.child("ProductDescription").getValue().toString();
                    String pImage = dataSnapshot.child("ProductImage").getValue().toString();


                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
