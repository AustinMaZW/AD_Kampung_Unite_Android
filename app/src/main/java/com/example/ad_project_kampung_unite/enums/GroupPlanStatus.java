package com.example.ad_project_kampung_unite.enums;

public enum GroupPlanStatus {
    AVAILABLE("Available"),
    SHOPPINGCOMPLETED("Shopping Completed"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String displayStatus;

    GroupPlanStatus(String displayStatus){
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus(){return displayStatus;}
}
