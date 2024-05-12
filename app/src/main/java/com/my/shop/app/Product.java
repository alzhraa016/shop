package com.my.shop.app;

import android.os.Parcelable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class Product implements Serializable {
    private String pId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    public Product() {
        // Empty constructor needed for Firebase
    }

    public Product(String pId, String name, String description, double price, String imageUrl) {
        this.pId = pId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Static method to convert Firestore DocumentSnapshot to Product object
    public static Product fromDocumentSnapshot(DocumentSnapshot snapshot) {
        Product product = new Product();
        product.setpId(snapshot.getId());
        product.setName(snapshot.getString("name"));
        product.setDescription(snapshot.getString("description"));
        product.setPrice(snapshot.getDouble("price"));
        product.setImageUrl(snapshot.getString("path"));
        return product;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}

