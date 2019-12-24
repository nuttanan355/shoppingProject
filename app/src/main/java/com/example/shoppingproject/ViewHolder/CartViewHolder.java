package com.example.shoppingproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Interface.ItemClickListner;
import com.example.shoppingproject.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    public ImageView imgProduct;
    private ItemClickListner itemClickListner;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
        imgProduct=itemView.findViewById(R.id.imageViewChild);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
