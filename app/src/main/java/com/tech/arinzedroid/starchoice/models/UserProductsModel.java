package com.tech.arinzedroid.starchoice.models;

import com.tech.arinzedroid.starchoice.util.RandomString;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class UserProductsModel {

    String id = new RandomString().nextString();
    String userId;
    String productId;
    Date dateCreated;
    Date dateUpdated = new Date();
    boolean active = true;
    boolean paidFully;
    double amtPaid = 0;
    ProductModel productModel;
    UserModel userModel;


    public UserProductsModel(){

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPaidFully() {
        return paidFully;
    }

    public void setPaidFully(boolean paidFully) {
        this.paidFully = paidFully;
    }

    public double getAmtPaid() {
        return amtPaid;
    }

    public void setAmtPaid(double amtPaid) {
        this.amtPaid = amtPaid;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
