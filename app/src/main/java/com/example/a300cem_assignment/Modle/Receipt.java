package com.example.a300cem_assignment.Modle;

public class Receipt {

    private String order_id, pname, total_amount, address, date, phone, quantity, time, user_name;


    public Receipt(String order_id, String pname, String total_amount, String address, String date, String phone, String quantity, String time, String user_name) {
        this.order_id = order_id;
        this.pname = pname;
        this.total_amount = total_amount;
        this.address = address;
        this.date = date;
        this.phone = phone;
        this.quantity = quantity;
        this.time = time;
        this.user_name = user_name;
    }

    public Receipt() {

    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
