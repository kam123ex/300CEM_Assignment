package com.example.a300cem_assignment.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_assignment.R;

public class ReceiptViewHolder extends RecyclerView.ViewHolder {

    public View view;
    private TextView txt_receipt_date, txt_receipt_time, txt_receipt_product_name, txt_receipt_quantity, txt_receipt_amount, txt_receipt_user, txt_receipt_address;


    public ReceiptViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        txt_receipt_date = view.findViewById(R.id.txt_receipt_date);
        txt_receipt_time = view.findViewById(R.id.txt_receipt_time);
        txt_receipt_product_name = view.findViewById(R.id.txt_receipt_product_name);
        txt_receipt_quantity = view.findViewById(R.id.txt_receipt_quantity);
        txt_receipt_amount = view.findViewById(R.id.txt_receipt_amount);
        txt_receipt_user = view.findViewById(R.id.txt_receipt_user);
        txt_receipt_address = view.findViewById(R.id.txt_receipt_address);


    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTxt_receipt_date() {
        return txt_receipt_date;
    }

    public void setTxt_receipt_date(TextView txt_receipt_date) {
        this.txt_receipt_date = txt_receipt_date;
    }

    public TextView getTxt_receipt_time() {
        return txt_receipt_time;
    }

    public void setTxt_receipt_time(TextView txt_receipt_time) {
        this.txt_receipt_time = txt_receipt_time;
    }

    public TextView getTxt_receipt_product_name() {
        return txt_receipt_product_name;
    }

    public void setTxt_receipt_product_name(TextView txt_receipt_product_name) {
        this.txt_receipt_product_name = txt_receipt_product_name;
    }

    public TextView getTxt_receipt_quantity() {
        return txt_receipt_quantity;
    }

    public void setTxt_receipt_quantity(TextView txt_receipt_quantity) {
        this.txt_receipt_quantity = txt_receipt_quantity;
    }

    public TextView getTxt_receipt_amount() {
        return txt_receipt_amount;
    }

    public void setTxt_receipt_amount(TextView txt_receipt_amount) {
        this.txt_receipt_amount = txt_receipt_amount;
    }

    public TextView getTxt_receipt_user() {
        return txt_receipt_user;
    }

    public void setTxt_receipt_user(TextView txt_receipt_user) {
        this.txt_receipt_user = txt_receipt_user;
    }

    public TextView getTxt_receipt_address() {
        return txt_receipt_address;
    }

    public void setTxt_receipt_address(TextView txt_receipt_address) {
        this.txt_receipt_address = txt_receipt_address;
    }
}
