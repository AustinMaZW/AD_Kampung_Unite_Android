package com.example.ad_project_kampung_unite.entities;

import com.example.ad_project_kampung_unite.entities.enums.GLStatus;
import com.example.ad_project_kampung_unite.entities.enums.HitchBuyRole;

import java.io.Serializable;
import java.util.List;

public class GroceryList implements Serializable {
    private int id;
    private String name;
    private HitchBuyRole role;
    private GLStatus status;
    private String cancelReason;
    private List<GroceryItem> groceryItems;
    private UserDetail userDetail;
    private GroupPlan groupPlanGL;
    private HitcherDetail hitcherDetail;

    public GroceryList() {
    }

    public GroceryList(int id, String name, HitchBuyRole role, GLStatus status, String cancelReason, List<GroceryItem> groceryItems, UserDetail userDetail, GroupPlan groupPlanGL, HitcherDetail hitcherDetail) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.status = status;
        this.cancelReason = cancelReason;
        this.groceryItems = groceryItems;
        this.userDetail = userDetail;
        this.groupPlanGL = groupPlanGL;
        this.hitcherDetail = hitcherDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HitchBuyRole getRole() {
        return role;
    }

    public void setRole(HitchBuyRole role) {
        this.role = role;
    }

    public GLStatus getStatus() {
        return status;
    }

    public void setStatus(GLStatus status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<GroceryItem> getGroceryItems() {
        return groceryItems;
    }

    public void setGroceryItems(List<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public GroupPlan getGroupPlanGL() {
        return groupPlanGL;
    }

    public void setGroupPlanGL(GroupPlan groupPlanGL) {
        this.groupPlanGL = groupPlanGL;
    }

    public HitcherDetail getHitcherDetail() { return hitcherDetail; }

    public void setHitcherDetail(HitcherDetail hitcherDetail) { this.hitcherDetail = hitcherDetail; }
}