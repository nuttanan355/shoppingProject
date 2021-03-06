package com.example.shoppingproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.Admin.AdminHomeActivity;
import com.example.shoppingproject.Model.Users;
import com.example.shoppingproject.Prevalent.Prevalent;
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


        Paper.init(this);

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey);

            }
            else
                {

                    myThread();
                }

        } else {

            myThread();

        }

    }

    private void myThread() {

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("CheckLogin","LoginFalse");
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
//                    Users adminData = dataSnapshot.child("Admins").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone) && usersData.getPassword().equals(password)) {

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("CheckLogin","LoginTrue");
                        Prevalent.currentOnlineUser = usersData;
                        startActivity(intent);

                    } else {

                        Toast.makeText(MainActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                    }

                }else if (dataSnapshot.child("Admins").child(phone).exists())
                {
                    Toast.makeText(MainActivity.this, "ยินดีต้อนรับ Admin\n คุณเข้าสู่ระบบสำเร็จแล้ว..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NumberAdmin",phone);
                    startActivity(intent);
                    finish();
                }


                else {

                    Toast.makeText(MainActivity.this, "บัญชีนี้ " + phone + "ไม่มีหมายเลข", Toast.LENGTH_SHORT).show();
                    myThread();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
