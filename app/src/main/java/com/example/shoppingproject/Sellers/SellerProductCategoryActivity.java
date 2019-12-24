package com.example.shoppingproject.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView tShirts,sportsTShirts,female,sweather;
    private ImageView glasses,pursesBags,hats,shoess;
    private ImageView headPhone,laptops,watches,mobiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);


        //-------------set------1-------------------------
        tShirts =(ImageView)findViewById(R.id.t_shirts);
        sportsTShirts=(ImageView)findViewById(R.id.sports_t_shirts);
        female=(ImageView)findViewById(R.id.female_dresses);
        sweather=(ImageView)findViewById(R.id.sweather);
        //-----------END-set---1--------------------------

        //-------------set------2-------------------------
        glasses =(ImageView)findViewById(R.id.glasses);
        pursesBags=(ImageView)findViewById(R.id.purses_bags);
        hats=(ImageView)findViewById(R.id.hats);
        shoess=(ImageView)findViewById(R.id.shoess);
        //-----------END-set---2--------------------------

        //-------------set------3-------------------------
        headPhone =(ImageView)findViewById(R.id.headphoness);
        laptops=(ImageView)findViewById(R.id.laptops);
        watches=(ImageView)findViewById(R.id.watches);
        mobiles=(ImageView)findViewById(R.id.mobiles);
        //-----------END-set---3--------------------------


        //-------------------------1----------------------------

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","tShires");
                startActivity(intent);
            }
        });

        sportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Sports tShirts");
                startActivity(intent);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Sweather");
                startActivity(intent);
            }
        });

        //----------------------EDN---1----------------------------

        //-------------------------2----------------------------

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        pursesBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Purses Bags");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Hats");
                startActivity(intent);
            }
        });

        shoess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Shoess");
                startActivity(intent);
            }
        });

        //----------------------EDN---2----------------------------

        //-------------------------3----------------------------

        headPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Head Phone");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Mobiles");
                startActivity(intent);
            }
        });

        //----------------------EDN---3----------------------------


    }
}
