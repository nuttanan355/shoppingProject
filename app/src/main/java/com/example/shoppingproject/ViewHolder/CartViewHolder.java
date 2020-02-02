package com.example.shoppingproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingproject.Interface.ItemClickListner;
import com.example.shoppingproject.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductDescription;
    public ImageView imageViewProduct,btnDeleteProducts;
    public ElegantNumberButton btnEditQuantity;
    public LinearLayout layoutCartEdit;
    private ItemClickListner itemClickListner;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
        txtProductDescription=itemView.findViewById(R.id.cart_product_description);
        imageViewProduct=itemView.findViewById(R.id.imageViewChild);

        btnDeleteProducts=itemView.findViewById(R.id.btn_delete_product_cart);
        btnEditQuantity=itemView.findViewById(R.id.btn_edit_quantity);
        layoutCartEdit=itemView.findViewById(R.id.layout_cart_edit);

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
