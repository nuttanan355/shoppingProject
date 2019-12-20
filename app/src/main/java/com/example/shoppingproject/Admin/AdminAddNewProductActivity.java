package com.example.shoppingproject.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductBTN;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private ImageView InputProductImage;

    private static final int GalleryPick = 1;

    private Uri ImageUri;

    private String productRandomKay, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef, sellersRef;

    private String sName, sPhone, sEmail, sAddress, sID;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        loadingBar = new ProgressDialog(this);

//        Toast.makeText(this,CategoryName,Toast.LENGTH_SHORT).show();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
//        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");


        AddNewProductBTN = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);

//-----------------SET OnClick------------------------
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();

            }
        });

//---------END-----SET OnClick------------------------

//        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        sName = dataSnapshot.child("name").getValue().toString();
//                        sPhone = dataSnapshot.child("phone").getValue().toString();
//                        sEmail = dataSnapshot.child("email").getValue().toString();
//                        sAddress = dataSnapshot.child("address").getValue().toString();
//                        sID = dataSnapshot.child("sid").getValue().toString();
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

    }

    private void OpenGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GalleryPick);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);

        }

    }

    private void ValidateProductData() {

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "กรุณใส่รูปภาพ...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "กรุณากรอกรายละเอียดสินค้า...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "กรุณากรอกราคาสินค้า...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "กรุณากรอกชื่อสินค้า...", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("เพิ่มสินค้าใหม่");
        loadingBar.setMessage("กรุณารอสักครู่ขณะที่กำลังเพิ่มสินค้าใหม่");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKay = saveCurrentDate + "," + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKay + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        downloadImageUrl = task.getResult().toString();

                        Toast.makeText(AdminAddNewProductActivity.this, "อัปโหลดรูปภาพสินค้าสำเร็จแล้ว", Toast.LENGTH_SHORT).show();

                        SaveProductInfoToDatabase();
                    }
                });
            }
        });


    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKay);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        //---------------Seller---------------
//        productMap.put("sellerName", sName);
//        productMap.put("sellerAddress", sAddress);
//        productMap.put("sellerPhone", sPhone);
//        productMap.put("sellerEmail", sEmail);
//        productMap.put("sid", sID);
//        productMap.put("productState", "Not Approved");
        //-----------------END SELLER---------------


        ProductsRef.child(productRandomKay).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "เพิ่มสินค้าสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
