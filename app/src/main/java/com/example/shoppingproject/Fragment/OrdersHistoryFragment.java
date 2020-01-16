package com.example.shoppingproject.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Model.NewOrders;
import com.example.shoppingproject.OrdersHistoryActivity;
import com.example.shoppingproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OrdersHistoryFragment extends Fragment {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_history, container, false);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = view.findViewById(R.id.orders_list3);
        ordersList.setLayoutManager(new LinearLayoutManager(getContext()));

        ordersRef.orderByChild("state shipped").equalTo("shipped");

        return view;

    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress,userShippingCity;
//        public Button btnShowOrder;


        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address);
//            userShippingCity =itemView.findViewById(R.id.order_city);

//            btnShowOrder = itemView.findViewById(R.id.btn_show_all_products);
//            btnShowOrder.setVisibility();

        }
    }




    @Override
    public void onStart() {
        super.onStart();


//        FirebaseRecyclerOptions<NewOrders>
//                options = new FirebaseRecyclerOptions.Builder<NewOrders>()
//                .setQuery(ordersRef.orderByChild("userName")
//                        .equalTo(Prevalent.currentOnlineUser.getPhone()), NewOrders.class).build();

        FirebaseRecyclerOptions<NewOrders>
                options = new FirebaseRecyclerOptions.Builder<NewOrders>()
                .setQuery(ordersRef.orderByChild("state shipped")
                        .equalTo("shipped"), NewOrders.class).build();

        FirebaseRecyclerAdapter<NewOrders, AdminOrdersViewHolder>
                adapter = new FirebaseRecyclerAdapter<NewOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final NewOrders model) {
                holder.userName.setText("ชื่อผู้สั่งซื้อ : " + model.getName());
                holder.userPhoneNumber.setText("เบอร์โทร : " + model.getPhone());
                holder.userTotalPrice.setText("ราคารวม : " + model.getTotalAmount() + "THB");
                holder.userDateTime.setText("วันที่ : " + model.getDate() + " เวลา :" + model.getTime());
                holder.userShippingAddress.setText("ที่อยู่ : " + model.getAddress());
//                holder.userShippingCity.setText("จังหวัด : " + model.getCity());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                            String uID = getRef(position).getKey();
//
                        Intent intent = new Intent(getActivity(), OrdersHistoryActivity.class);
//                            intent.putExtra("uid", uID);
                        intent.putExtra("oid", model.getOid());
                        startActivity(intent);
                    }
                });


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

}
