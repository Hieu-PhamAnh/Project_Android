package com.example.login.model;

public class Item {

    private String id;
    private String name;
    private long price;
    private long imprice;
    private long instore;
    private String brand;
    private String img;

    public Item() {
    }

    public Item(String id, String name, long price, long imprice, long instore, String brand, String img) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imprice = imprice;
        this.instore = instore;
        this.brand = brand;
        this.img = img;
    }

    public long getImprice() {
        return imprice;
    }

    public void setImprice(long imprice) {
        this.imprice = imprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getInstore() {
        return instore;
    }

    public void setInstore(long instore) {
        this.instore = instore;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
