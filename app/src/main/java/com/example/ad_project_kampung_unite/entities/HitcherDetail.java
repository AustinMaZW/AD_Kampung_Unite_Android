package com.example.ad_project_kampung_unite.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HitcherDetail {
    private int id;
    private LocalDate prefDate;
    private LocalTime prefPickupTimeFrom;
    private String prefPickupLocation;
    private List<HitchRequest> hitchRequestList;

    public HitcherDetail() {
    }

    public HitcherDetail(int id, LocalDate prefDate, LocalTime prefPickupTimeFrom, String prefPickupLocation, List<HitchRequest> hitchRequestList) {
        this.id = id;
        this.prefDate = prefDate;
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

    public LocalDate getPrefDate() {
        return prefDate;
    }

    public void setPrefDate(LocalDate prefDate) {
        this.prefDate = prefDate;
    }

    public LocalTime getPrefPickupTimeFrom() {
        return prefPickupTimeFrom;
    }

    public void setPrefPickupTimeFrom(LocalTime prefPickupTimeFrom) {
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
