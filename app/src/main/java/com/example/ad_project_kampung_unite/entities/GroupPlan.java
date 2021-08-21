package com.example.ad_project_kampung_unite.entities;

import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;

import java.time.LocalDate;
import java.util.List;

public class GroupPlan {
    private int id;
    private String planName;
    private String storeName;
    private LocalDate shoppingDate;
    private String pickupAddress;
    private LocalDate pickupDate;
    private GroupPlanStatus groupPlanStatus;
    private CombinedPurchaseList combinedPurchaseList;
    private List<AvailableTime> availableTimes;
    private List<GroceryList> groceryLists;
    private List<HitchRequest> groupPlan_hitchers;

    public GroupPlan() {
    }
    public GroupPlan(String storeName, LocalDate shoppingDate, String pickupAddress, LocalDate pickupDate) {
        this.storeName = storeName;
        this.shoppingDate = shoppingDate;
        this.pickupAddress = pickupAddress;
        this.pickupDate = pickupDate;
    }
    public GroupPlan(int id, String planName, String storeName, LocalDate shoppingDate, String pickupAddress, LocalDate pickupDate, GroupPlanStatus groupPlanStatus, CombinedPurchaseList combinedPurchaseList, List<AvailableTime> availableTimes, List<GroceryList> groceryLists, List<HitchRequest> groupPlan_hitchers) {
        this.id = id;
        this.planName = planName;
        this.storeName = storeName;
        this.shoppingDate = shoppingDate;
        this.pickupAddress = pickupAddress;
        this.pickupDate = pickupDate;
        this.groupPlanStatus = groupPlanStatus;
        this.combinedPurchaseList = combinedPurchaseList;
        this.availableTimes = availableTimes;
        this.groceryLists = groceryLists;
        this.groupPlan_hitchers = groupPlan_hitchers;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
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

    public GroupPlanStatus getGroupPlanStatus() {
        return groupPlanStatus;
    }

    public void setGroupPlanStatus(GroupPlanStatus groupPlanStatus) {
        this.groupPlanStatus = groupPlanStatus;
    }

    public CombinedPurchaseList getCombinedPurchaseList() {
        return combinedPurchaseList;
    }

    public void setCombinedPurchaseList(CombinedPurchaseList combinedPurchaseList) {
        this.combinedPurchaseList = combinedPurchaseList;
    }

    public List<AvailableTime> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<AvailableTime> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public void setGroceryLists(List<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
    }

    public List<HitchRequest> getGroupPlan_hitchers() {
        return groupPlan_hitchers;
    }

    public void setGroupPlan_hitchers(List<HitchRequest> groupPlan_hitchers) {
        this.groupPlan_hitchers = groupPlan_hitchers;
    }

    public GroupPlan(int id, String planName, String storeName, LocalDate shoppingDate, String pickupAddress, LocalDate pickupDate, GroupPlanStatus groupPlanStatus) {
        this.id = id;
        this.planName = planName;
        this.storeName = storeName;
        this.shoppingDate = shoppingDate;
        this.pickupAddress = pickupAddress;
        this.pickupDate = pickupDate;
        this.groupPlanStatus = groupPlanStatus;
    }
}
