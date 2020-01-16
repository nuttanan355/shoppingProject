package com.example.shoppingproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent Intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(Intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.this.finish();
    }
}

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (firebaseUser!=null)
//        {
//            Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//
//    }
//
//    private void AllowAccess(final String phone, final String password) {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child("Users").child(phone).exists()) {
//                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
//
//                    if (usersData.getPhone().equals(phone) && usersData.getPassword().equals(password)) {
//                        Toast.makeText(MainActivity.this, "กรุณาสักครู่คุณได้เข้าสู่ระบบ", Toast.LENGTH_SHORT).show();
//                        loadingBar.dismiss();
//
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        Prevalent.currentOnlineUser= usersData;
//                        startActivity(intent);
//                    } else {
//
//                        loadingBar.dismiss();
//                        Toast.makeText(MainActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(MainActivity.this, "บัญชีนี้ " + phone + "ไม่มีหมายเลข", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
