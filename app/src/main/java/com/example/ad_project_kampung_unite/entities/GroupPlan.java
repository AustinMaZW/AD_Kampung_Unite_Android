package com.example.ad_project_kampung_unite.entities;

import java.time.LocalDate;

public class GroupPlan {
    private int id;
    private String storeName;
    private LocalDate shoppingDate;
    private String pickupAddress;
    private LocalDate pickupDate;
    //private GroupPlanStatus groupPlanStatus;      //add the enum class later..

    public GroupPlan(int id, String storeName, LocalDate shoppingDate, String pickupAddress, LocalDate pickupDate) {
        this.id = id;
        this.storeName = storeName;
        this.shoppingDate = shoppingDate;
        this.pickupAddress = pickupAddress;
        this.pickupDate = pickupDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public LocalDate getShoppingDate() {
        return shoppingDate;
    }

    public void setShoppingDate(LocalDate shoppingDate) {
        this.shoppingDate = shoppingDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }
}
