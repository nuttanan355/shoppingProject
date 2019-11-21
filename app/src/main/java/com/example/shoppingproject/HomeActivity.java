package com.example.shoppingproject;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

import android.view.Menu;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ClipData.Item navHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                if (destination.getId() == R.id.nav_home) {
                    Toast.makeText(HomeActivity.this, "home", Toast.LENGTH_LONG).show();
                }
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
                    Toast.makeText(HomeActivity.this, "nav_settings", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_logout) {
                    Toast.makeText(HomeActivity.this, "nav_logout", Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
//        onOptionsItemSelected(this,R.id.nav_host_fragment);
    }


}
