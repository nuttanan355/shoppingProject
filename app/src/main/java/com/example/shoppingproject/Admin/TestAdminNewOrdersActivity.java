package com.example.shoppingproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Model.NewOrders;
import com.example.shoppingproject.Prevalent.Prevalent;
import com.example.shoppingproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestAdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

// -----------------------------------------------------------   ไม่ใช้ ------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<NewOrders>
                options = new FirebaseRecyclerOptions.Builder<NewOrders>()
                .setQuery(ordersRef, NewOrders.class).build();

        FirebaseRecyclerAdapter<NewOrders, AdminOrdersViewHolder>
                adapter = new FirebaseRecyclerAdapter<NewOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final NewOrders model) {
                holder.userName.setText("ชื่อผู้สั่งซื้อ : " + model.getFullName());
                holder.userPhoneNumber.setText("เบอร์โทร : " + model.getPhoneRecipient());
                holder.userTotalPrice.setText("ราคารวม : " + model.getOrderTotalAmount() + " THB");
                holder.userDateTime.setText("วันที่ : " + model.getOrderDate() + "\nเวลา : " + model.getOrderTime());
                holder.userShippingAddress.setText("ที่อยู่ : " + model.getAddress());
//                holder.userShippingCity.setText("จังหวัด : " + model.getCity());

//                holder.btnShowOrder.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String uID = getRef(position).getKey();
//
//                        Intent intent = new Intent(TestAdminNewOrdersActivity.this, AdminViewOrdersProductActivity.class);
//                        intent.putExtra("uid", uID);
//                        startActivity(intent);
//                    }
//                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            String uID = getRef(position).getKey();
//
                            Intent intent = new Intent(TestAdminNewOrdersActivity.this, AdminViewOrdersProductActivity.class);
                            intent.putExtra("uid", uID);
                            startActivity(intent);

                    }
                });

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CharSequence options[] = new CharSequence[]
//                                {
//                                        "ใช่", "ไม่"
//                                };
//                        AlertDialog.Builder builder = new AlertDialog.Builder(TestAdminNewOrdersActivity.this);
//                        builder.setTitle("จัดส่งสินค้าตามคำสั่งซื้อหรือไม่ ?");
//
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                if (i == 0) {
//                                    String uID = getRef(position).getKey();
//                                    OrderHistory();
//                                    RemoverOrder(uID);
//                                } else {
//                                    finish();
//                                }
//                            }
//                        });
//                        builder.show();
//                    }
//                });

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void OrderHistory()
    {


        final DatabaseReference ProOrderRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Processing Orders")
                .child(Prevalent.currentOnlineUser.getPhone());


        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProOrderRef.setValue(dataSnapshot.getValue());

//                orderRef.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress,userShippingCity;
        public Button btnShowOrder;


        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address);
//            userShippingCity =itemView.findViewById(R.id.order_city);

//            btnShowOrder = itemView.findViewById(R.id.btn_show_all_products);

        }
    }

    private void RemoverOrder(String uID) {

        ordersRef.child(uID).removeValue();

    }

}
