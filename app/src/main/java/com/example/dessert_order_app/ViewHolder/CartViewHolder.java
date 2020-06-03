package com.example.dessert_order_app.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dessert_order_app.Interface.ItemClickListener;
import com.example.dessert_order_app.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View view;

    public TextView txt_item_name, txt_item_quantity, txt_item_price;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        txt_item_name = itemView.findViewById(R.id.txt_item_name);
        txt_item_quantity = itemView.findViewById(R.id.txt_item_quantity);
        txt_item_price = itemView.findViewById(R.id.txt_item_price);

    }


    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);
    }


    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
