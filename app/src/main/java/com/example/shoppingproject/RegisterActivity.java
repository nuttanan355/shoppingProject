package com.example.shoppingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccount_btn, Oto_btn;
    private EditText txtUaername, txtPhonenumber, txtPassword, txtNameSurname;
    private ProgressDialog loadingBar;

    private CountryCodePicker countryCodePicker;
    private EditText codeOtpText;
    private String checker = "", phoneNumber = "";
    private RelativeLayout RrLayoutInputPhone, RrLayoutInputOTP;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        CreateAccount_btn = (Button) findViewById(R.id.btn_register);
        Oto_btn = (Button) findViewById(R.id.btn_otp);

        txtUaername = (EditText) findViewById(R.id.register_txt_username);
        txtPhonenumber = (EditText) findViewById(R.id.register_txt_phone_number);
        txtPassword = (EditText) findViewById(R.id.register_txt_password);
        txtNameSurname = (EditText) findViewById(R.id.register_txt_name_surname);

        codeOtpText = (EditText) findViewById(R.id.code_otp_text);
        RrLayoutInputPhone = (RelativeLayout) findViewById(R.id.phoneAuth);
        RrLayoutInputOTP = (RelativeLayout) findViewById(R.id.OtpAuth);

        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(txtPhonenumber);


        loadingBar = new ProgressDialog(this);

        Oto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = codeOtpText.getText().toString();

                if (verificationCode.equals("")) {
                    Toast.makeText(RegisterActivity.this, "โปรดเขียนรหัสยืนยันก่อน", Toast.LENGTH_SHORT).show();

                } else {
                    loadingBar.setTitle("การยืนยัน OTP");
                    loadingBar.setMessage("โปรดรอสักครู่ขณะที่เรากำลังตรวจสอบหมายเลข OTP ของคุณ.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        CreateAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(RegisterActivity.this, "หมายเลขโทรศัพท์ไม่ถูกต้อง ...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                RrLayoutInputPhone.setVisibility(View.VISIBLE);
                RrLayoutInputOTP.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = s;
//                mResendToken=forceResendingToken;

                RrLayoutInputPhone.setVisibility(View.GONE);
                RrLayoutInputOTP.setVisibility(View.VISIBLE);

                loadingBar.dismiss();
                Toast.makeText(RegisterActivity.this, "ส่งรหัสแล้วโปรดตรวจสอบ", Toast.LENGTH_SHORT).show();


            }
        };

    }

    private void CreateAccount() {

        String txtname = txtUaername.getText().toString();
        String txtnameSurname = txtNameSurname.getText().toString();
        final String txtphone = txtPhonenumber.getText().toString();
        String txtpassword = txtPassword.getText().toString();

        if (TextUtils.isEmpty(txtname)) {
            Toast.makeText(this, "กรุณากรอกชื่อ.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(txtnameSurname)) {
            Toast.makeText(this, "กรุณากรอกชื่อ - นามสกุล.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(txtphone)) {
            Toast.makeText(this, "กรุณากรอกเบอร์โทรศัพท์.....", Toast.LENGTH_SHORT).show();
        } else if (txtphone.length() < 10) {
            Toast.makeText(this, "เบอร์โทรศัพท์ไม่ถูกต้อง.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(txtpassword)) {
            Toast.makeText(this, "รหัสผ่านจะต้องมีอย่างน้อย 6 ตัวอักษร.....", Toast.LENGTH_SHORT).show();
        } else if (txtpassword.length() < 6) {
            Toast.makeText(this, "รหัสผ่านจะต้องมีอย่างน้อย 6 ตัวอักษร.....", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("สร้างบัญชี");
            loadingBar.setMessage("โปรดรอสักครู่ขณะนี้เรากำลังตรวจสอบข้อมูล");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.child("Users").child(txtphone).exists())) {

                        phoneNumber = countryCodePicker.getFullNumberWithPlus();

                        loadingBar.setTitle("การยืนยันหมายเลขโทรศัพท์");
                        loadingBar.setMessage("โปรดรอสักครู่ขณะที่เรากำลังตรวจสอบหมายเลขโทรศัพท์" + phoneNumber);
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS
                                , RegisterActivity.this, mCallbacks);


                    } else {

                        Toast.makeText(RegisterActivity.this, "หมายเลข" + txtphone + "มีอยู่แล้ว", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "โปรดลองอีกครั้งโดยใช้เบอร์โทรศัพท์อื่น", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        String txtname = txtUaername.getText().toString();
        String txtnameSurname = txtNameSurname.getText().toString();
        String txtpassword = txtPassword.getText().toString();
        final String txtphone = txtPhonenumber.getText().toString();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        final HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("phone", txtphone);
        userdataMap.put("name surname", txtnameSurname);
        userdataMap.put("password", txtpassword);
        userdataMap.put("name", txtname);
        userdataMap.put("phoneOrder", txtphone);
        userdataMap.put("address", "");
        userdataMap.put("postalCode", "");



//        final HashMap<String, Object> SecurityMap = new HashMap<>();
//        SecurityMap.put("answer1", txtphone);
//        SecurityMap.put("answer2", txtphone);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingBar.dismiss();
//                            Toast.makeText(RegisterActivity.this, "ขอแสดงความยินดีคุณเข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();
//                            CreateAccount();

                            RootRef.child("Users").child(txtphone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "ขอแสดงความยินดีบัญชีของคุณได้ถูกสร้างขึ้นแล้ว", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();

                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(RegisterActivity.this, "เครือข่ายผิดพลาด,โปรดลองอีกครั้ง...", Toast.LENGTH_SHORT).show();
                                                    }

                                        }
                                    });


                        } else {
                            loadingBar.show();
                            String e = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Error :" + e, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void ValidatephoneNumber(final String name, final String phone, final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
//                .child("Users").child(phone);

        final HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("phone", phone);
        userdataMap.put("password", password);
        userdataMap.put("name", name);

        final HashMap<String, Object> SecurityMap = new HashMap<>();
        SecurityMap.put("answer1", phone);
        SecurityMap.put("answer2", phone);


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())) {
                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    RootRef.child("Users").child(phone).child("Security Questions").updateChildren(SecurityMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "ขอแสดงความยินดีบัญชีของคุณได้ถูกสร้างขึ้นแล้ว", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                loadingBar.dismiss();
                                                Toast.makeText(RegisterActivity.this, "เครือข่ายผิดพลาด,โปรดลองอีกครั้ง...", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            });

                } else {

                    Toast.makeText(RegisterActivity.this, "หมายเลข" + phone + "มีอยู่แล้ว", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "โปรดลองอีกครั้งโดยใช้เบอร์โทรศัพท์อื่น", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
