package com.example.shoppingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private MaterialEditText fullNameEditText, userPhoneEditText, addressEditText, postalCodeEditText;
    private CircleImageView profileImageView;
    private TextView textUserName, btnSecuityQuestion;

    private Button btnUpdateText;

    private Uri imageUri;
    private String myUri = "", checker = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePunctureRef;
//    private String c


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePunctureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);

        fullNameEditText = (MaterialEditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (MaterialEditText) findViewById(R.id.settings_phone_number);
        addressEditText = (MaterialEditText) findViewById(R.id.settings_address);
        postalCodeEditText = (MaterialEditText) findViewById(R.id.settings_postal_code);
        textUserName = (TextView) findViewById(R.id.settings_user_name);

        btnSecuityQuestion = (TextView) findViewById(R.id.btn_security_questions);


        btnUpdateText = (Button) findViewById(R.id.btn_update_settings);


        btnSecuityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);

            }
        });

        btnUpdateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void updateOnlyUserInfo() {

        if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            Toast.makeText(this, "ต้องระบุชื่อ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {
            Toast.makeText(this, "ต้องระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();

        } else if ((userPhoneEditText.getText().toString()).length() < 10) {
            Toast.makeText(this, "ต้องระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(this, "ต้องระบุที่อยู่", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(postalCodeEditText.getText().toString())) {
            Toast.makeText(this, "รหัสไปรษณีย์", Toast.LENGTH_SHORT).show();

        } else if ((postalCodeEditText.getText().toString()).length() < 5) {
            Toast.makeText(this, "รหัสไปรษณีย์", Toast.LENGTH_SHORT).show();

        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("อัปเดตข้อมูล");
            progressDialog.setMessage("โปรดรอสักครู่ขณะที่กำลังอัปเดตข้อมูลบัญชีของคุณ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            HashMap<String, Object> userMap = new HashMap<>();

            userMap.put("nameSurname", fullNameEditText.getText().toString());
            userMap.put("address", addressEditText.getText().toString());
            userMap.put("phoneRecipient", userPhoneEditText.getText().toString());
            userMap.put("postalCode", postalCodeEditText.getText().toString());
            ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

            progressDialog.dismiss();
            startActivity(new Intent(SettingsActivity.this, HomeActivity.class).putExtra("CheckLogin", "LoginTrue"));
            Toast.makeText(SettingsActivity.this, "อัปเดตข้อมูลสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "มีข้ผิดพลาด, ลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            Toast.makeText(this, "ต้องระบุชื่อ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {
            Toast.makeText(this, "ต้องระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();

        } else if ((userPhoneEditText.getText().toString()).length() < 10) {
            Toast.makeText(this, "ต้องระบุเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(this, "ต้องระบุที่อยู่", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(postalCodeEditText.getText().toString())) {
            Toast.makeText(this, "รหัสไปรษณีย์", Toast.LENGTH_SHORT).show();

        } else if ((postalCodeEditText.getText().toString()).length() < 5) {
            Toast.makeText(this, "รหัสไปรษณีย์", Toast.LENGTH_SHORT).show();

        } else if (checker.equals("clicked")) {
            uploadImage();
        }

    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("โปรดรอสักครู่ขณะที่กำลังอัปเดตข้อมูลบัญชีของคุณ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePunctureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
 .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                Uri downloadUri = task.getResult();
                                myUri = downloadUri.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(Prevalent.currentOnlineUser.getPhone());


                                HashMap<String, Object> userMap = new HashMap<>();

                                userMap.put("fullName", fullNameEditText.getText().toString());
                                userMap.put("address", addressEditText.getText().toString());
                                userMap.put("phoneRecipient", userPhoneEditText.getText().toString());
                                userMap.put("image", myUri);

                                ref.updateChildren(userMap);

                                progressDialog.dismiss();
                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class).putExtra("CheckLogin", "LoginTrue"));
                                Toast.makeText(SettingsActivity.this, "อัปเดตข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "ไม่ได้เลือกรูปภาพ", Toast.LENGTH_SHORT).show();
        }

    }


    private void userInfoDisplay() {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String fullname = dataSnapshot.child("fullName").getValue().toString();
                    String username = dataSnapshot.child("userName").getValue().toString();
                    String phone = dataSnapshot.child("phoneRecipient").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();
                    String postalCode = dataSnapshot.child("postalCode").getValue().toString();

                    textUserName.setText(username);
                    fullNameEditText.setText(fullname);
                    userPhoneEditText.setText(phone);
                    addressEditText.setText(address);
                    postalCodeEditText.setText(postalCode);


                    if (dataSnapshot.child("image").exists()) {

                        String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        userInfoDisplay();
    }
}
