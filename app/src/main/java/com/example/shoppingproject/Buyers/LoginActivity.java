package com.example.shoppingproject.Buyers;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.Admin.AdminHomeActivity;
import com.example.shoppingproject.Model.Users;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.example.shoppingproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText txtNumber, txtPassword;
    private Button btn_login;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private CheckBox chbRememberMe;

    private TextView AdminLink, NotAdminLink,ForgetPasswordLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        txtNumber = (EditText) findViewById(R.id.login_txt_username);
        txtPassword = (EditText) findViewById(R.id.login_txt_password);

        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        ForgetPasswordLink=(TextView)findViewById(R.id.forget_password_link);



        chbRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        loadingBar = new ProgressDialog(this);

        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","Login");
                startActivity(intent);
            }
        });

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
            Toast.makeText(this, "กรุณากรอกหมายเลขโทรศัพท์.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(txtpassword)) {
            Toast.makeText(this, "กรุณากรอกรหัสผ่าน.....", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("โปรดรอสักครู่ขณะนี้เรากำลังตรวจสอบข้อมูล");
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

                            Toast.makeText(LoginActivity.this, "ยินดีต้อนรับ คุณเข้าสู่ระบบสำเร็จ..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                        } else if (parentDbName.equals("Users")) {

                            Toast.makeText(LoginActivity.this, "เข้าสู่ระบบสำเร็จ...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Prevalent.currentOnlineUser = usersData;
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }

                    } else {

                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "บัญชีนี้ " + phone + " ไม่มีหมายเลข", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
