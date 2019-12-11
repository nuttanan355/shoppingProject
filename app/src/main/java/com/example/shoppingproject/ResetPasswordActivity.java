package com.example.shoppingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingproject.Model.Users;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titlteQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = (TextView) findViewById(R.id.page_title);
        titlteQuestions = (TextView) findViewById(R.id.title_question);

        phoneNumber = (EditText) findViewById(R.id.find_phone_number);
        question1 = (EditText) findViewById(R.id.question_1);
        question2 = (EditText) findViewById(R.id.question_2);

        verifyBTN = (Button) findViewById(R.id.btn_verify);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);


        if (check.equals("settings")) {
            pageTitle.setText("Set Questions");
            titlteQuestions.setText("Please set Answers for the Following Security Questions?");
            verifyBTN.setText("Set");

            displayPreviousAnsers();

            verifyBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setAnswers();

                }
            });

        } else if (check.equals("Login")) {
            phoneNumber.setVisibility(View.VISIBLE);

            verifyBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });

        }
    }

    private void setAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please answer both question.", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);
//                        userdataMap.put("name",name);
            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "you have set security questions successfully.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }

                }
            });

        }
    }

    private void displayPreviousAnsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verifyUser() {
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString();
        final String answer2 = question2.getText().toString();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {

            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String mPhone = dataSnapshot.child("phone").getValue().toString();
//                    if (phone.equals(mPhone))
//                    {

                        if (dataSnapshot.hasChild("Security Questions")) {
                            String ans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)) {
                                Toast.makeText(ResetPasswordActivity.this, "your 1st answer is wrong.", Toast.LENGTH_LONG).show();
                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "your 2nd answer is wrong.", Toast.LENGTH_LONG).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write Password here...");
                                builder.setView(newPassword);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully.", Toast.LENGTH_LONG).show();

                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();

                            }
                        }
//                    }
                        else {
                            Toast.makeText(ResetPasswordActivity.this, "you have not set the security questions.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number not exist.", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            {
                Toast.makeText(ResetPasswordActivity.this, "please complete the form.", Toast.LENGTH_LONG).show();
            }
    }
}
