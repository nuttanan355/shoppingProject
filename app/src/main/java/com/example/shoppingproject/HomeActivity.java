package com.example.shoppingproject;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.example.shoppingproject.Model.Products;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.example.shoppingproject.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");

//MenuItem =(MenuItem)findViewById(R.id.recycler_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //----------btn cart--ล่างจอ--------------
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //---------END-btn cart--ล่างจอ--------------


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_cart,
                R.id.nav_orders,
                R.id.nav_categories,
                R.id.nav_settings,
                R.id.nav_logout).setDrawerLayout(drawer).build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //----------------set For Menu Items Clickable-----------------

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                if (destination.getId() == R.id.nav_home) {
//                    Toast.makeText(HomeActivity.this, "home", Toast.LENGTH_LONG).show();
//                }
                if (destination.getId() == R.id.nav_cart) {
                    Toast.makeText(HomeActivity.this, "nav_cart", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_orders) {
                    Toast.makeText(HomeActivity.this, "nav_orders", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_categories) {
                    Toast.makeText(HomeActivity.this, "nav_categories", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_settings) {
                    Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                    startActivity(intent);
//                    Toast.makeText(HomeActivity.this, "nav_settings", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_logout) {
                    Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_LONG).show();

                    Paper.book().destroy();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {

                }
            }
        });

        //-------END------set For Menu Items Clickable-----------------

        View headerView =navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView =headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart(){

        super.onStart();
        FirebaseRecyclerOptions<Products>options=
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef,Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder>adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price = "+model.getPrice()+" ฿");
                        holder.txtProductDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder =new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();

    }

}


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        getMenuInflater().inflate(R.menu.activity_home_drawer, (Menu) item);
////        switch (item.getItemId()) {
////
////            case R.id.nav_cart:
////
////            case R.id.nav_logout:
////                Paper.book().destroy();
////
////                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                startActivity(intent);
////                finish();
////            default:
//////                return true;
////                return super.onOptionsItemSelected(item);
////        }
//////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//////        drawer.closeDrawer(GravityCompat.START);
//////        return true;
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            // Handle the camera action
//        } else if (id == R.id.nav_cart) {
//
//        } else if (id == R.id.nav_orders) {
//
//        } else if (id == R.id.nav_categories) {
//
//        } else if (id == R.id.nav_settings) {
//
//        } else if (id == R.id.nav_logout) {
//            Paper.book().destroy();
////
//            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return super.onOptionsItemSelected(item);
//
//    }
