package com.example.ad_project_kampung_unite.entities.enums;

public enum GroupPlanStatus {
    AVAILABLE("Open to hitch requests"),
    SHOPPINGCOMPLETED("Shopping Completed"),
    CLOSED("Time to buy groceries"),
    CANCELLED("Cancelled");

    private final String displayStatus;

    GroupPlanStatus(String displayStatus){
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus(){return displayStatus;}
}
