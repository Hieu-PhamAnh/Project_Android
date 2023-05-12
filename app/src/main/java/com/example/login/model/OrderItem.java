package com.example.login.model;

public class OrderItem{
    private String id, userId, orderId, itemId;
    private long quantity,itemprice;
    private Boolean ordered;

    public OrderItem() {

    }

    public OrderItem(String id, String userId, String orderId, String itemId, long quantity, long itemprice, Boolean ordered) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.itemprice = itemprice;
        this.ordered = ordered;
    }

    public long getItemprice() {
        return itemprice;
    }

    public void setItemprice(long itemprice) {
        this.itemprice = itemprice;
    }

    public Boolean getOrdered() {
        return ordered;
    }

    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
