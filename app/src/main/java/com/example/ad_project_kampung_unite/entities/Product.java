package com.example.ad_project_kampung_unite.entities;

public class Product {
    private int id;
    private String name, description, imgURL, category;

    public Product(int id, String name, String description, String category, String imgURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imgURL = imgURL;
    }

    public int getProductId() {
        return id;
    }

    public void setProductId(int productId) {
        this.id = productId;
    }

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public String getProductDescription() {
        return description;
    }

    public void setProductDescription(String productDescription) {
        this.description = productDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
