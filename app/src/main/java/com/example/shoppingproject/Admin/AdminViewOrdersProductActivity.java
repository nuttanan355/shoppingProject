package com.example.shoppingproject.Admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Model.Cart;
import com.example.shoppingproject.R;
import com.example.shoppingproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewOrdersProductActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ordersRef,ordersProRef;

    private String oederID = "";
    private TextView txtOrederName, txtOrederPhone, txtOrederAddress, txtOrederTotal;
    private EditText editOrederPackage;

    private Button BTNSaveOrder,BTNDeleteOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_orders_product);


        txtOrederName = findViewById(R.id.view_order_name);
        txtOrederPhone = findViewById(R.id.view_order_phone);
        txtOrederAddress = findViewById(R.id.view_order_address);
        txtOrederTotal = findViewById(R.id.view_order_total);

        editOrederPackage = findViewById(R.id.view_order_package);

        BTNDeleteOrder=findViewById(R.id.btn_delete_order);
        BTNSaveOrder=findViewById(R.id.btn_save_order);



        oederID = getIntent().getStringExtra("oid");

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(oederID);

//        ordersProRef = FirebaseDatabase.getInstance().getReference()
//                .child("OrdersPro").child(userID);



        showOrders();
        showOrderList();

        BTNSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOeder();
            }
        });

        BTNDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOeder();
            }
        });



    }

    private void DeleteOeder() {


        CharSequence options[] = new CharSequence[]
                                        {
                                                "ใช่",
                                                "ไม่"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewOrdersProductActivity.this);
                                builder.setTitle("ต้องการลบคำสั่งซื้อนี้หรือไม่??");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        if (position == 0) {
                                            ordersRef.removeValue();
                                        }
                                        if (position == 1) {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();

    }

    private void SendOeder() {

        final String txtPackage=editOrederPackage.getText().toString();

        if (txtPackage.equals(""))
        {
            Toast.makeText(this,"กรุณาใส่เลขพัสดุ",Toast.LENGTH_SHORT).show();
        }else
            {
                ordersRef.child("state approve").setValue("approve").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        ordersRef.child("package").setValue(txtPackage);
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        ordersProRef.setValue(dataSnapshot.getValue());
//                                        ordersRef.removeValue();
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                            }
//                        });

                    }
                });



            }


    }

    private void showOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String oName = dataSnapshot.child("name").getValue().toString();
                String oPhone = dataSnapshot.child("phone").getValue().toString();
                String oAddress = dataSnapshot.child("address").getValue().toString();
                String oTotal = dataSnapshot.child("totalAmount").getValue().toString();

                String oPackage = dataSnapshot.child("package").getValue().toString();


                txtOrederName.setText(oName);
                txtOrederPhone.setText(oPhone);
                txtOrederAddress.setText(oAddress);
                txtOrederTotal.setText(oTotal);

                editOrederPackage.setText(oPackage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showOrderList() {
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(ordersRef.child("orderList"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("จำนวน " + model.getQuantity());
                holder.txtProductPrice.setText("ราคา = " + model.getPrice() + " ฿");
                holder.txtProductDescription.setText(model.getDiscount());
                holder.txtProductName.setText(model.getPname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(v);
                return holder;
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public void onStop() {
        super.onStop();
        AdminViewOrdersProductActivity.this.finish();
    }
}
