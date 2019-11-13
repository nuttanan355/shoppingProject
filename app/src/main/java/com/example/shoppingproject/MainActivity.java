package com.example.shoppingproject;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoppingproject.Model.Users;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button login_btn,register_btn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = (Button) findViewById(R.id.btn_welcome_login);
        register_btn=(Button)findViewById(R.id.btn_account);

        loadingBar=new ProgressDialog(this);

        Paper.init(this);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey =Paper.book().read(Prevalent.UserPasswordKey);


        if(UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait......");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }

        }

    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData =dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone) && usersData.getPassword().equals(password))
                    {
                        Toast.makeText(MainActivity.this,"Please wait, you are already logged in...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }else {

                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this,"Password is incorrect.",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this,"Accout with this " +phone+"number do not exists.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
