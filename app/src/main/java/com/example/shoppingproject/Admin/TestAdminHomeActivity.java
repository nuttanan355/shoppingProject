package com.example.shoppingproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingproject.HomeActivity;
import com.example.shoppingproject.MainActivity;
import com.example.shoppingproject.R;

public class TestAdminHomeActivity extends AppCompatActivity {

    private Button LogoutBtn, CheckOrdersBtn, btnMaintainProducts,btnCheckApproveProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_admin_home);

        LogoutBtn = (Button) findViewById(R.id.btn_admin_logout);
        CheckOrdersBtn = (Button) findViewById(R.id.btn_check_orders);
        btnMaintainProducts = (Button) findViewById(R.id.btn_maintain);
        btnCheckApproveProduct=(Button)findViewById(R.id.btn_check_approve_product);

        btnMaintainProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestAdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestAdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestAdminHomeActivity.this, TestAdminNewOrdersActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });

        btnCheckApproveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestAdminHomeActivity.this, AdminCheckNewProductsActivity.class);
                startActivity(intent);
            }
        });

    }
}
