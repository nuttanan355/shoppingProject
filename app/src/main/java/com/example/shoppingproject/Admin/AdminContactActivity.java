package com.example.shoppingproject.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class AdminContactActivity extends AppCompatActivity {

    private MaterialEditText txtIdLine,txtEmail;
    private MaterialEditText txtUrlFacebook;
    private Button btnSeveContact;
    private String checkNumber = "";
private DatabaseReference AdminRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contact);

        txtUrlFacebook=findViewById(R.id.contact_txt_url_facebook);
        txtIdLine=findViewById(R.id.contact_txt_id_line);
        txtEmail=findViewById(R.id.contact_txt_mail);

        btnSeveContact=findViewById(R.id.btn_save_contact);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            checkNumber = getIntent().getExtras().get("NumberAdmin").toString();
        }

        AdminRef = FirebaseDatabase.getInstance().getReference().child("Admins").child(checkNumber);


        btnSeveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });


    }
    private void updateContact() {

        if (TextUtils.isEmpty(txtUrlFacebook.getText().toString())) {
            Toast.makeText(this, "ต้องระบุ URL Facebook", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(txtIdLine.getText().toString())) {
            Toast.makeText(this, "ต้องระบุ ID Line", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            Toast.makeText(this, "ต้องระบุ E-Mail", Toast.LENGTH_SHORT).show();

        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("อัปเดตข้อมูล");
            progressDialog.setMessage("โปรดรอสักครู่ขณะที่กำลังอัปเดตข้อมูลบัญชีของคุณ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap<String, Object> userMap = new HashMap<>();

            userMap.put("contact facebook", txtUrlFacebook.getText().toString());
            userMap.put("contact line", txtIdLine.getText().toString());
            userMap.put("contact mail", txtEmail.getText().toString());
            AdminRef.updateChildren(userMap);

            progressDialog.dismiss();
            startActivity(new Intent(AdminContactActivity.this, AdminHomeActivity.class));
            Toast.makeText(AdminContactActivity.this, "อัปเดตข้อมูลสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showDataAdmin() {

        AdminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String urlFacebook = dataSnapshot.child("contact facebook").getValue().toString();
                String idLine = dataSnapshot.child("contact line").getValue().toString();
                String eMail = dataSnapshot.child("contact mail").getValue().toString();

                txtUrlFacebook.setText(urlFacebook);
                txtIdLine.setText(idLine);
                txtEmail.setText(eMail);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        showDataAdmin();

    }


}
