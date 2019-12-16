package com.example.shoppingproject.ViewHolder;

//import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingproject.Interface.ItemClickListner;
import com.example.shoppingproject.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductState;
    public ImageView imageView;
    public ItemClickListner listner;


    public ItemViewHolder(View itemView) {
        super(itemView);

        imageView =(ImageView)itemView.findViewById(R.id.seller_product_image);
        txtProductDescription=(TextView)itemView.findViewById(R.id.seller_product_description);
        txtProductName =(TextView)itemView.findViewById(R.id.seller_product_name);
        txtProductPrice=(TextView)itemView.findViewById(R.id.seller_product_price);
        txtProductState=(TextView)itemView.findViewById(R.id.seller_product_state);


    }
    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v,getAdapterPosition(),false);

    }
}
