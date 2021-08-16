package com.example.ad_project_kampung_unite;

import java.util.Date;

public class GroceryList {
    private String name, date, time, totalitems, status;

    public GroceryList(String name, String date, String time, String totalitems, String status){
        this.name = name;
        this.date = date;
        this.time = time;
        this.totalitems = totalitems;
        this.status = status;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalitems() {
        return totalitems;
    }

    public void setTotalitems(String totalitems) {
        this.totalitems = totalitems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}