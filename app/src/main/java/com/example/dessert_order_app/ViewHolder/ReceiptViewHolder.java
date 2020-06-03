package com.example.dessert_order_app.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dessert_order_app.R;

public class ReceiptViewHolder extends RecyclerView.ViewHolder {

    public View view;
    private TextView txt_receipt_date, txt_receipt_time, txt_receipt_phone, txt_receipt_order_id, txt_receipt_pid, txt_receipt_pname, txt_receipt_quantity, txt_receipt_total_amount, txt_receipt_user_name, txt_receipt_address, txt_receipt_user_id;


    public ReceiptViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        txt_receipt_address = view.findViewById(R.id.txt_receipt_address);
        txt_receipt_date = view.findViewById(R.id.txt_receipt_date);
        txt_receipt_order_id = view.findViewById(R.id.txt_receipt_order_id);
        txt_receipt_phone = view.findViewById(R.id.txt_receipt_phone);
        txt_receipt_pid = view.findViewById(R.id.txt_receipt_pid);
        txt_receipt_pname = view.findViewById(R.id.txt_receipt_pname);
        txt_receipt_quantity = view.findViewById(R.id.txt_receipt_quantity);
        txt_receipt_time = view.findViewById(R.id.txt_receipt_time);
        txt_receipt_total_amount = view.findViewById(R.id.txt_receipt_total_amount);
        txt_receipt_user_id = view.findViewById(R.id.txt_receipt_user_id);
        txt_receipt_user_name = view.findViewById(R.id.txt_receipt_user_name);


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

    public TextView getTxt_receipt_phone() {
        return txt_receipt_phone;
    }

    public void setTxt_receipt_phone(TextView txt_receipt_phone) {
        this.txt_receipt_phone = txt_receipt_phone;
    }

    public TextView getTxt_receipt_order_id() {
        return txt_receipt_order_id;
    }

    public void setTxt_receipt_order_id(TextView txt_receipt_order_id) {
        this.txt_receipt_order_id = txt_receipt_order_id;
    }

    public TextView getTxt_receipt_pid() {
        return txt_receipt_pid;
    }

    public void setTxt_receipt_pid(TextView txt_receipt_pid) {
        this.txt_receipt_pid = txt_receipt_pid;
    }

    public TextView getTxt_receipt_pname() {
        return txt_receipt_pname;
    }

    public void setTxt_receipt_pname(TextView txt_receipt_pname) {
        this.txt_receipt_pname = txt_receipt_pname;
    }

    public TextView getTxt_receipt_quantity() {
        return txt_receipt_quantity;
    }

    public void setTxt_receipt_quantity(TextView txt_receipt_quantity) {
        this.txt_receipt_quantity = txt_receipt_quantity;
    }

    public TextView getTxt_receipt_total_amount() {
        return txt_receipt_total_amount;
    }

    public void setTxt_receipt_total_amount(TextView txt_receipt_total_amount) {
        this.txt_receipt_total_amount = txt_receipt_total_amount;
    }

    public TextView getTxt_receipt_user_name() {
        return txt_receipt_user_name;
    }

    public void setTxt_receipt_user_name(TextView txt_receipt_user_name) {
        this.txt_receipt_user_name = txt_receipt_user_name;
    }

    public TextView getTxt_receipt_address() {
        return txt_receipt_address;
    }

    public void setTxt_receipt_address(TextView txt_receipt_address) {
        this.txt_receipt_address = txt_receipt_address;
    }

    public TextView getTxt_receipt_user_id() {
        return txt_receipt_user_id;
    }

    public void setTxt_receipt_user_id(TextView txt_receipt_user_id) {
        this.txt_receipt_user_id = txt_receipt_user_id;
    }
}
