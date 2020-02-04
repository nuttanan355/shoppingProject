package com.example.shoppingproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Model.Products;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.example.shoppingproject.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type = "";
    private String checkLogin = "";

    private Spinner spinnerCategory;

    private DatabaseReference AdminRef;

//    private static final String TAG_HOME_FRAGMENT = "fragment_test_blank";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            checkLogin = getIntent().getExtras().get("CheckLogin").toString();
        }

        AdminRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("0873887533");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //----------btn cart--ล่างจอ--------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                if (!checkLogin.equals("LoginFalse")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
        //---------END-btn cart--ล่างจอ--------------




        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_cart,
                R.id.nav_search,
                R.id.nav_new_order,
                R.id.nav_settings,
                R.id.nav_logout).setDrawerLayout(drawer).build();


        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //----------------set For Menu Items Clickable-----------------

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments)
            {
                if (destination.getId() == R.id.nav_cart) {
                    if (!checkLogin.equals("LoginFalse")) {
                        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                        startActivity(intent);
                    }
                }
                if (destination.getId() == R.id.nav_search) {
                    if (!checkLogin.equals("LoginFalse")) {
                        Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                        startActivity(intent);
                    }
                }
                if (destination.getId() == R.id.nav_new_order) {
                    if (!checkLogin.equals("LoginFalse")) {
                        Intent intent = new Intent(HomeActivity.this, NewOrdersActivity.class);
                        startActivity(intent);
                    }

                }
                if (destination.getId() == R.id.nav_settings) {

                    if (!checkLogin.equals("LoginFalse")) {
                        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }
                if (destination.getId() == R.id.nav_logout) {

                    if (!checkLogin.equals("LoginFalse")) {
                        Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_LONG).show();

                        Paper.book().destroy();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        //-------END------set For Menu Items Clickable-----------------

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if (checkLogin.equals("LoginTrue")) {

            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }else
            {
                userNameTextView.setText("เข้าสู่ระบบ");

                userNameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




    }




    private void showProduct(FirebaseRecyclerOptions<Products> options) {

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

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("checkLogin",checkLogin);
                                intent.putExtra("checkCart","noInCart");
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_contact) {
            showDataAdmin();
        }
        return false;

    }

    private void showDataAdmin() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = getLayoutInflater();


        builder.setTitle("ติดต่อเรา");
        View view = inflater.inflate(R.layout.layout_contact, null);
        builder.setView(view);

        final TextView txtUrlFacebook = (TextView) view.findViewById(R.id.btn_facebook);
        final TextView txtIdLine = (TextView) view.findViewById(R.id.btn_line);
        final TextView txtEmail = (TextView) view.findViewById(R.id.btn_mail);
//        final ImageButton btn_spain = (ImageButton) view.findViewById(R.id.btn_spain);


        AdminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String urlFacebook = dataSnapshot.child("contact facebook").getValue().toString();
                String idLine = dataSnapshot.child("contact line").getValue().toString();
                String eMail = dataSnapshot.child("contact mail").getValue().toString();

//                txtUrlFacebook.setText(urlFacebook);
                txtIdLine.setText("   "+"ID : "+idLine);
                txtEmail.setText("   : "+eMail);

                txtUrlFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
//                            .setQuery(ProductsRef.orderByChild("category").equalTo("Female Dresses"), Products.class)
                                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==1)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("tShires"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==2)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Sports tShirts"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==3)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Female Dresses"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==4)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Sweather"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==5)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Glasses"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==6)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Purses Bags"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==7)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Hats"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==8)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Shoess"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==9)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Head Phone"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==10)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Laptops"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==11)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Watches"), Products.class)
//                    .setQuery(ProductsRef, Products.class)
                                    .build();

                    showProduct(options);
                }
                if (position==12)
                {
                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(ProductsRef.orderByChild("category").equalTo("Mobiles"), Products.class)
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
                    .setQuery(ProductsRef, Products.class)
                                .build();

                showProduct(options);
            }
        });




    }



}
