package com.example.shoppingproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.MainActivity;
import com.example.shoppingproject.Model.Products;
import com.example.shoppingproject.R;
import com.example.shoppingproject.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference adminProductsRef;

    private Spinner adminSpinnerCategory;
    private String checkNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            checkNumber = getIntent().getExtras().get("NumberAdmin").toString();
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_home:

                        Intent intentHome = new Intent(AdminHomeActivity.this, AdminHomeActivity.class);
                        startActivity(intentHome);
//                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_add:
//                        mTextMessage.setText(R.string.title_add);
                        Intent intentCate = new Intent(AdminHomeActivity.this, AdminProductCategoryActivity.class);
                        startActivity(intentCate);

                        return true;
                    case R.id.navigation_orders:
//                        mTextMessage.setText(R.string.title_add);
                        Intent intentOrder = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                        intentOrder.putExtra("Admin", "Admin");
                        startActivity(intentOrder);

                        return true;
                    case R.id.navigation_contact:
//                        mTextMessage.setText(R.string.title_add);
                        Intent intentContact = new Intent(AdminHomeActivity.this, AdminContactActivity.class);
                        intentContact.putExtra("NumberAdmin", checkNumber);
                        startActivity(intentContact);

                        return true;
                    case R.id.navigation_logout:
//                        mTextMessage.setText(R.string.title_logout);
//                        final FirebaseAuth sAuth;
//                        sAuth =FirebaseAuth.getInstance();
//                        sAuth.signOut();
                        Paper.book().destroy();
                        Intent intentMain = new Intent(AdminHomeActivity.this, MainActivity.class);
                        intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentMain);
                        finish();
                        Toast.makeText(AdminHomeActivity.this, "Logout", Toast.LENGTH_LONG).show();


                        return true;

                }
                return false;
            }
        });

        adminProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=findViewById(R.id.seller_home_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

    @Override
    protected void onStart() {
        super.onStart();

        adminSpinnerCategory = (Spinner) findViewById(R.id.admin_spinner_category);

        adminSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
//                            .setQuery(ProductsRef.orderByChild("category").equalTo("Female Dresses"), Products.class)
                                    .setQuery(adminProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==1)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("tShires"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==2)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Sports tShirts"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==3)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Female Dresses"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==4)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Sweather"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==5)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Glasses"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==6)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Purses Bags"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==7)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Hats"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==8)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Shoess"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==9)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Head Phone"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==10)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Laptops"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==11)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Watches"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==12)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(adminProductsRef.orderByChild("ProductCategory").equalTo("Mobiles"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                FirebaseRecyclerOptions<Products> options =
                        new FirebaseRecyclerOptions.Builder<Products>()
//                                .setQuery(ProductsRef.orderByChild("category").equalTo("Mobiles"), Products.class)
                                .setQuery(adminProductsRef, Products.class)
                                .build();

                showProduct(options);
            }
        });



    }



    private void showProduct(FirebaseRecyclerOptions<Products> options) {

//        FirebaseRecyclerOptions<Products> options =
////                new FirebaseRecyclerOptions.Builder<Products>()
//////                        .setQuery(unverifiedProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
////                        .setQuery(adminProductsRef.orderByChild("productState"), Products.class)
////                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getProductName());
                        holder.txtProductPrice.setText("ราคา " + model.getProductPrice() + " ฿");
                        holder.txtProductDescription.setText(model.getProductDescription());
                        Picasso.get().load(model.getProductImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent = new Intent(AdminHomeActivity.this, AdminEditProductsActivity.class);
                                intent.putExtra("pid", model.getProductID());
                                startActivity(intent);


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_view, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



}

