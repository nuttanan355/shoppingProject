package com.example.shoppingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.Model.Users;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.example.shoppingproject.Admin.AdminHomeActivity;
import com.example.shoppingproject.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent Intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(Intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }
}

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (firebaseUser!=null)
//        {
//            Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//
//    }
//
//    private void AllowAccess(final String phone, final String password) {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child("Users").child(phone).exists()) {
//                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
//
//                    if (usersData.getPhone().equals(phone) && usersData.getPassword().equals(password)) {
//                        Toast.makeText(MainActivity.this, "กรุณาสักครู่คุณได้เข้าสู่ระบบ", Toast.LENGTH_SHORT).show();
//                        loadingBar.dismiss();
//
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        Prevalent.currentOnlineUser= usersData;
//                        startActivity(intent);
//                    } else {
//
//                        loadingBar.dismiss();
//                        Toast.makeText(MainActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(MainActivity.this, "บัญชีนี้ " + phone + "ไม่มีหมายเลข", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
