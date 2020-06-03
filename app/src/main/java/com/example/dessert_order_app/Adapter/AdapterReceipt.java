package com.example.dessert_order_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dessert_order_app.Modle.Receipt;
import com.example.dessert_order_app.R;

import java.util.ArrayList;

public class AdapterReceipt extends RecyclerView.Adapter<AdapterReceipt.ReceiptHolder> {

    private ArrayList<Receipt> receiptsList;
    private Context mContext;

    public AdapterReceipt(ArrayList<Receipt> receiptsList, Context mContext) {
        this.receiptsList = receiptsList;
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public ReceiptHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_receipt, null, false);

        return new ReceiptHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptHolder holder, int position) {
        final Receipt receipt = receiptsList.get(position);
        holder.setTxt_receipt_address("Deliver Address: " + receipt.getAddress());
        holder.setTxt_receipt_date("Order Date: " + receipt.getDate());
        holder.setTxt_receipt_order_id("Order ID: " + receipt.getOrder_id());
        holder.setTxt_receipt_phone("Phone Number: " + receipt.getPhone());
        holder.setTxt_receipt_pid("Product ID: " +receipt.getPid());
        holder.setTxt_receipt_pname("Product Name: " +receipt.getPname());
        holder.setTxt_receipt_quantity("Quantity: " + receipt.getQuantity());
        holder.setTxt_receipt_time("Order Time: " + receipt.getTime());
        holder.setTxt_receipt_total_amount("Total Amount: $" +receipt.getTotal_amount());
        holder.setTxt_receipt_user_id("User ID: " + receipt.getUser_id());
        holder.setTxt_receipt_user_name("Order User: " + receipt.getUser_name());

    }


    @Override
    public int getItemCount() {
        return this.receiptsList.size();
    }


    public class ReceiptHolder extends RecyclerView.ViewHolder {
        private TextView txt_receipt_date, txt_receipt_time, txt_receipt_phone, txt_receipt_order_id, txt_receipt_pid, txt_receipt_pname, txt_receipt_quantity, txt_receipt_total_amount, txt_receipt_user_name, txt_receipt_address, txt_receipt_user_id;

        public ReceiptHolder(View itemView) {
            super(itemView);
            txt_receipt_address = itemView.findViewById(R.id.txt_receipt_address);
            txt_receipt_date = itemView.findViewById(R.id.txt_receipt_date);
            txt_receipt_order_id = itemView.findViewById(R.id.txt_receipt_order_id);
            txt_receipt_phone = itemView.findViewById(R.id.txt_receipt_phone);
            txt_receipt_pid = itemView.findViewById(R.id.txt_receipt_pid);
            txt_receipt_pname = itemView.findViewById(R.id.txt_receipt_pname);
            txt_receipt_quantity = itemView.findViewById(R.id.txt_receipt_quantity);
            txt_receipt_time = itemView.findViewById(R.id.txt_receipt_time);
            txt_receipt_total_amount = itemView.findViewById(R.id.txt_receipt_total_amount);
            txt_receipt_user_id = itemView.findViewById(R.id.txt_receipt_user_id);
            txt_receipt_user_name = itemView.findViewById(R.id.txt_receipt_user_name);

        }

        public void setTxt_receipt_date(String date) {
            txt_receipt_date.setText(date);
        }

        public void setTxt_receipt_time(String time) {
            txt_receipt_time.setText(time);
        }

        public void setTxt_receipt_phone(String phone) {
            txt_receipt_phone.setText(phone);
        }

        public void setTxt_receipt_order_id(String order_id) {
            txt_receipt_order_id.setText(order_id);
        }

        public void setTxt_receipt_pid(String pid) {
            txt_receipt_pid.setText(pid);
        }

        public void setTxt_receipt_pname(String pname) {
            txt_receipt_pname.setText(pname);
        }

        public void setTxt_receipt_quantity(String quantity) {
            txt_receipt_quantity.setText(quantity);
        }

        public void setTxt_receipt_total_amount(String amount) {
            txt_receipt_total_amount.setText(amount);
        }

        public void setTxt_receipt_user_name(String name) {
            txt_receipt_user_name.setText(name);
        }

        public void setTxt_receipt_address(String address) {
            txt_receipt_address.setText(address);
        }

        public void setTxt_receipt_user_id(String user_id) {
            txt_receipt_user_id.setText(user_id);
        }
    }


}

