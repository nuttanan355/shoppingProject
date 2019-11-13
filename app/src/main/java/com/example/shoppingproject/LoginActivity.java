package com.example.shoppingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingproject.Model.Users;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText txtNumber, txtPassword;
    private Button btn_login;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private CheckBox chbRememberMe;

    private TextView AdminLink, NotAdminLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        txtNumber = (EditText) findViewById(R.id.login_txt_username);
        txtPassword = (EditText) findViewById(R.id.login_txt_password);

        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);


        chbRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        loadingBar = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }

        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";

            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }

    private void loginUser() {
        String txtphone = txtNumber.getText().toString();
        String txtpassword = txtPassword.getText().toString();

        if (TextUtils.isEmpty(txtphone)) {
            Toast.makeText(this, "Please write your Phone Number.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(txtpassword)) {
            Toast.makeText(this, "Please write your Password.....", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccout(txtphone, txtpassword);
        }

    }

    private void AllowAccessToAccout(final String phone, final String password) {

        if (chbRememberMe.isChecked()) {

            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone) && usersData.getPassword().equals(password)) {
                        if (parentDbName.equals("Admins")) {

                            Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                        } else if (parentDbName.equals("Users")) {

                            Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }

                    } else {

                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
