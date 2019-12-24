package com.example.shoppingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccount_btn;
    private EditText txtUaername,txtPhonenumber,txtPassword;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccount_btn = (Button) findViewById(R.id.btn_register);

        txtUaername = (EditText) findViewById(R.id.register_txt_username);
        txtPhonenumber = (EditText) findViewById(R.id.register_txt_phone_number);
        txtPassword = (EditText) findViewById(R.id.register_txt_password);

        loadingBar=new ProgressDialog(this);


        CreateAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String txtname = txtUaername.getText().toString();
        String txtphone = txtPhonenumber.getText().toString();
        String txtpassword = txtPassword.getText().toString();

        if(TextUtils.isEmpty(txtname)){
            Toast.makeText(this,"กรุณากรอกชื่อ.....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(txtphone)){
            Toast.makeText(this,"กรุณากรอกเบอร์โทรศัพท์.....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(txtpassword)){
            Toast.makeText(this,"กรุณากรอกรหัสผ่าน.....",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("โปรดรอสักครู่ขณะนี้เรากำลังตรวจสอบข้อมูล");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(txtname,txtphone,txtpassword);
        }


    }

    private void ValidatephoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {

                    HashMap<String, Object>userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"ขอแสดงความยินดีบัญชีของคุณได้ถูกสร้างขึ้นแล้ว",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"เครือข่ายผิดพลาด,โปรดลองอีกครั้ง...",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(RegisterActivity.this,"หมายเลข"+phone+"มีอยู่แล้ว",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"โปรดลองอีกครั้งโดยใช้เบอร์โทรศัพท์อื่น",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}