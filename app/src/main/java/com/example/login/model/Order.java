package com.example.login.model;

public class Order {
    private String id,userId,phone,address,date,type;
    long sum;
    Boolean waiting,shipping,received;

    public Order() {
    }

    public Order(String id, String userId, String phone, String address, String date, String type, long sum, Boolean waiting, Boolean shipping, Boolean received) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.type = type;
        this.sum = sum;
        this.waiting = waiting;
        this.shipping = shipping;
        this.received = received;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public Boolean getWaiting() {
        return waiting;
    }

    public void setWaiting(Boolean waiting) {
        this.waiting = waiting;
    }

    public Boolean getShipping() {
        return shipping;
    }

    public void setShipping(Boolean shipping) {
        this.shipping = shipping;
    }

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }
}
