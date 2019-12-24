package com.example.shoppingproject.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.Admin.AdminHomeActivity;
import com.example.shoppingproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SellerLoginActivity extends AppCompatActivity {

    private Button btnloginSeller;
    private EditText emailInput,passwordInput;

    private ProgressDialog loadingBar;

    private FirebaseAuth sAuth;
    private DatabaseReference rootRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        btnloginSeller=findViewById(R.id.btn_login_seller);

        emailInput=findViewById(R.id.login_seller_email);
        passwordInput=findViewById(R.id.login_seller_password);

        sAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);


        btnloginSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });



    }

    private void loginSeller()
    {
        final String email=emailInput.getText().toString();
        final String password =passwordInput.getText().toString();

        if (!email.equals("")&&!password.equals(""))
        {
            loadingBar.setTitle("เข้าสู่ระบบบัญชีผู้ขาย");
            loadingBar.setMessage("โปรดรอสักครู่ขณะที่กำลังตรวจสอบข้อมูล");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            sAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Intent intent =new Intent(SellerLoginActivity.this, AdminHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(SellerLoginActivity.this, "กรุณากรอกแบบฟอร์มลงทะเบียน", Toast.LENGTH_LONG).show();
        }


    }
}
