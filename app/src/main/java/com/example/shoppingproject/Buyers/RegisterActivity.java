package com.example.shoppingproject.Buyers;

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

import com.example.shoppingproject.R;
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
            Toast.makeText(this,"Please write your name.....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(txtphone)){
            Toast.makeText(this,"Please write your Phone Number.....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(txtpassword)){
            Toast.makeText(this,"Please write your Password.....",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
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
                                        Toast.makeText(RegisterActivity.this,"Congratulations, your account has been created",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network Error: Please try again after some time...",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(RegisterActivity.this,"This"+phone+" already exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again using another phone number.",Toast.LENGTH_SHORT).show();
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
