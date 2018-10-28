package com.tech.arinzedroid.starchoice.models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class ProductModel {

    String id;
    String productName;
    double price;
    String desc;
    boolean isActive;
    Date dateCreated = new Date();

    public ProductModel(){

    }

    @Override
    public String toString(){
        return productName != null ? productName : "";
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
