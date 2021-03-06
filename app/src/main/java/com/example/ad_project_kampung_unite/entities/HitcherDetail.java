package com.example.ad_project_kampung_unite.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class HitcherDetail implements Serializable {
    private int id;
    private LocalDateTime prefPickupTimeFrom;
    private String prefPickupLocation;
    private List<HitchRequest> hitchRequestList;

    //test commit

    public HitcherDetail() {
    }
    public HitcherDetail(LocalDateTime prefPickupTimeFrom, String prefPickupLocation) {
        this.prefPickupTimeFrom = prefPickupTimeFrom;
        this.prefPickupLocation = prefPickupLocation;
    }
    public HitcherDetail(int id, LocalDate prefDate, LocalDateTime prefPickupTimeFrom, String prefPickupLocation, List<HitchRequest> hitchRequestList) {
        this.id = id;
        this.prefPickupTimeFrom = prefPickupTimeFrom;
        this.prefPickupLocation = prefPickupLocation;
        this.hitchRequestList = hitchRequestList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getPrefPickupTimeFrom() {
        return prefPickupTimeFrom;
    }

    public void setPrefPickupTimeFrom(LocalDateTime prefPickupTimeFrom) {
        this.prefPickupTimeFrom = prefPickupTimeFrom;
    }

    public String getPrefPickupLocation() {
        return prefPickupLocation;
    }

    public void setPrefPickupLocation(String prefPickupLocation) {
        this.prefPickupLocation = prefPickupLocation;
    }

    public List<HitchRequest> getHitchRequestList() {
        return hitchRequestList;
    }

    public void setHitchRequestList(List<HitchRequest> hitchRequestList) {
        this.hitchRequestList = hitchRequestList;
    }
}
