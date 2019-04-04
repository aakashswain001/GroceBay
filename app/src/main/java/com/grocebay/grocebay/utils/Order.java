package com.grocebay.grocebay.utils;

public class Order {
    int id;
    String order_details, order_date;
    int price;
    String address, status;

    public Order() {
    }

    public Order(int id, String order_details, String order_date, int price, String address, String status) {
        this.id = id;
        this.order_details = order_details;
        this.order_date = order_date;
        this.price = price;
        this.address = address;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setOrder_details(String order_details) {
        this.order_details = order_details;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
