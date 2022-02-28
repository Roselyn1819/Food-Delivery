package com.example.fooddelivery.classes;

public class Order {
    String date, time, orders, total;

    public Order() {

    }

    public Order(String date, String time, String orders, String total) {
        this.date = date;
        this.time = time;
        this.orders = orders;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
