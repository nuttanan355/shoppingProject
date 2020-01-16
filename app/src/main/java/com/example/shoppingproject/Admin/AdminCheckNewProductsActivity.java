package com.example.shoppingproject.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCheckNewProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference unverifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_products);


        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        recyclerView=findViewById(R.id.admin_approve_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerOptions<Products>options=
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(unverifiedProductsRef.orderByChild("productState")
//                                .equalTo("Not Approved"),Products.class).build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder>adapter=
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductPrice.setText("ราคา = " + model.getPrice() + " ฿");
//                        holder.txtProductDescription.setText(model.getDescription());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                final String productID = model.getPid();
//
//                                CharSequence options[] = new CharSequence[]
//                                        {
//                                                "Yes",
//                                                "No"
//                                        };
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProductsActivity.this);
//                                builder.setTitle("ต้องการอนุมัติสินค้านี้หรือไม่ คุณแน่ใจไหม ??");
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int position) {
//                                        if (position == 0) {
//                                            ChangeProductState(productID);
//                                        }
//                                        if (position == 1) {
//                                        }
//                                    }
//                                });
//                                builder.show();
//                            }
//
//
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_view, parent, false);
//                        ProductViewHolder holder = new ProductViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }

//    private void ChangeProductState(String productID)
//    {
//        unverifiedProductsRef.child(productID)
//                .child("productState").setValue("การอนุมัติ")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task)
//            {
//                Toast.makeText(AdminCheckNewProductsActivity.this,"กำลังอนุมัติรายการสินค้านี้.",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}
