package com.example.ad_project_kampung_unite.entities;

import java.time.LocalDateTime;
import java.util.List;

public class HitcherDetail {

    private int id;
    private LocalDateTime prefPickupTimeFrom; // calculate pickupTimeTo with this attribute. E.g. prefPickupTime + 3
    private String prefPickupLocation;
    private List<HitchRequest> hitchRequests;
    private GroceryList groceryList;
}
