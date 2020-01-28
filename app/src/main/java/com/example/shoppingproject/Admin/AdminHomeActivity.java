package com.example.shoppingproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    private DatabaseReference unverifiedProductsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

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

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

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

//        FirebaseRecyclerOptions<Products> options=
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(unverifiedProductsRef.orderByChild("sid")
//                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();
//
//        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=
//                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductState.setText("State :"+model.getProductState());
//                        holder.txtProductPrice.setText("Price = " + model.getPrice() + " ฿");
//                        holder.txtProductDescription.setText(model.getDescription());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                final String productID = model.getPid();
//
//                                CharSequence options[] = new CharSequence[]
//                                        {
//                                                "Yes",
//                                                "No"
//                                        };
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this);
//                                builder.setTitle("ต้องการลบสินค้านี้หรือไม่ แน่ใจไหม ?");
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int position) {
//                                        if (position == 0) {
//                                            DeleteProductState(productID);
//                                        }
//                                        if (position == 1) {
//                                        }
//                                    }
//                                });
//                                builder.show();
//                            }
//
//
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_view, parent, false);
//                        ItemViewHolder holder = new ItemViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(unverifiedProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                        .setQuery(unverifiedProductsRef.orderByChild("productState"), Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("ราคา " + model.getPrice() + " ฿");
                        holder.txtProductDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                    Intent intent = new Intent(AdminHomeActivity.this, AdminEditProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
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

//    private void DeleteProductState(String productID)
//    {
//        unverifiedProductsRef.child(productID)
//                .removeValue()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        Toast.makeText(AdminHomeActivity.this,"ลบสำเร็จแล้ว",Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//    }
}



//        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//
//
//
//                return false;
//            }
//        };



//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home,
//                R.id.navigation_dashboard,
//                R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);