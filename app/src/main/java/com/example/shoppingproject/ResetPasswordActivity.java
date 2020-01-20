package com.example.shoppingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.concurrent.TimeUnit;

public class ResetPasswordActivity extends AppCompatActivity {

    private String phoneNumber = "";
//    private TextView pageTitle, titlteQuestions, titlteQuestionsNewUser;

    private EditText inputPhoneNumber, inputOtp,inputNewPassword;

    private ProgressDialog loadingBar;

//        , question1, question2;
private CountryCodePicker countryCodePicker;

    private Button verifyBTN, verifyOtpBTN,btnNewPassword;
    private RelativeLayout RrLayoutInputPhone, RrLayoutInputOTP,RrLayoutInputNewPassword;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        RrLayoutInputPhone = (RelativeLayout) findViewById(R.id.relative_layout_reset_pssword);
        RrLayoutInputOTP = (RelativeLayout) findViewById(R.id.relative_layout_reset_pssword_otp);
        RrLayoutInputNewPassword = (RelativeLayout) findViewById(R.id.relative_layout_reset_pssword_input);

        inputPhoneNumber = (EditText) findViewById(R.id.find_phone_number);
        inputOtp = (EditText) findViewById(R.id.find_phone_number_otp);
        inputNewPassword = (EditText) findViewById(R.id.find_reset_pssword_input);

        verifyBTN = (Button) findViewById(R.id.btn_verify);
        verifyOtpBTN = (Button) findViewById(R.id.btn_verify_otp);
        btnNewPassword = (Button) findViewById(R.id.btn_reset_password);

        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(inputPhoneNumber);

        loadingBar = new ProgressDialog(this);

        verifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        verifyOtpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String verificationCode = inputOtp.getText().toString();
                if (verificationCode.equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, "โปรดเขียนรหัสยืนยันก่อน", Toast.LENGTH_SHORT).show();

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

        btnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newPassword();

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(ResetPasswordActivity.this, "หมายเลขโทรศัพท์ไม่ถูกต้อง ...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                RrLayoutInputPhone.setVisibility(View.VISIBLE);
                RrLayoutInputOTP.setVisibility(View.GONE);
                RrLayoutInputNewPassword.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = s;
//                mResendToken=forceResendingToken;

                RrLayoutInputPhone.setVisibility(View.GONE);
                RrLayoutInputOTP.setVisibility(View.VISIBLE);
                RrLayoutInputNewPassword.setVisibility(View.GONE);

                loadingBar.dismiss();
                Toast.makeText(ResetPasswordActivity.this, "ส่งรหัสแล้วโปรดตรวจสอบ", Toast.LENGTH_SHORT).show();


            }
        };



    }


    private void verifyUser() {

        final String phone = inputPhoneNumber.getText().toString();

        if (!phone.equals("")&&phone.length() >9) {

            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String mPhone = dataSnapshot.child("phone").getValue().toString();
                        if (phone.equals(mPhone)) {


                            phoneNumber = countryCodePicker.getFullNumberWithPlus();

                            loadingBar.setTitle("การยืนยันหมายเลขโทรศัพท์");
                            loadingBar.setMessage("โปรดรอสักครู่ขณะที่เรากำลังตรวจสอบหมายเลขโทรศัพท์" + phoneNumber);
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS
                                    , ResetPasswordActivity.this, mCallbacks);



                        }

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "ไม่มีหมายเลขโทรศัพท์นี้ กรุณาสร้างบัญชีผู้ใช้ก่อน", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(ResetPasswordActivity.this, "หมายเลขโทรศัพท์ไม่ถูกต้อง", Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {



        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingBar.dismiss();
//                            Toast.makeText(RegisterActivity.this, "ขอแสดงความยินดีคุณเข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();
                            RrLayoutInputPhone.setVisibility(View.GONE);
                            RrLayoutInputOTP.setVisibility(View.GONE);
                            RrLayoutInputNewPassword.setVisibility(View.VISIBLE);

                        } else {
                            loadingBar.show();
                            String e = task.getException().toString();
                            Toast.makeText(ResetPasswordActivity.this, "Error :" + e, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void newPassword() {

        final String phone = inputPhoneNumber.getText().toString();
        String newPass = inputNewPassword.getText().toString();

        final DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(phone);


        if (newPass.length()<6){
            Toast.makeText(this, "รหัสผ่านจะต้องมีอย่างน้อย 6 ตัวอักษร.....", Toast.LENGTH_SHORT).show();
        }
        else
            {
//                ref.addListenerForSingleValueEvent(new L);
                ref.child("password").setValue(newPass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "เปลียนรหัสผ่านสำเร็จแล้ว", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });



            }
    }


//    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
//                            builder.setTitle("รหัสผ่านใหม่");
//
//    final EditText newPassword = new EditText(ResetPasswordActivity.this);
//                            newPassword.setHint("กรอกรหัสผ่านที่นี่...");
//                            builder.setView(newPassword);
//
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            if (!newPassword.getText().toString().equals("") && newPassword.length() >= 6) {
//                ref.child("password").setValue(newPassword.getText().toString())
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(ResetPasswordActivity.this, "เปลียนรหัสผ่านสำเร็จแล้ว", Toast.LENGTH_LONG).show();
//
//                                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                }
//                            }
//                        });
//            }
//        }
//    });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            dialogInterface.cancel();
//        }
//    });
//                            builder.show();
}
