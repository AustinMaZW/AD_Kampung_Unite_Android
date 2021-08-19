package com.example.ad_project_kampung_unite.entities;

import com.example.ad_project_kampung_unite.entities.enums.RequestStatus;

import java.time.LocalDateTime;

public class HitchRequest {
    private int Id;
    private LocalDateTime pickupTimeChosen;
    private boolean buyerConfirmTransaction;
    private boolean hitcherConfirmTransaction;
    private RequestStatus requestStatus;
    private GroupPlan groupPlan;
    private HitcherDetail hitcherDetail;

    public HitchRequest() {
    }

    public HitchRequest(int id, LocalDateTime pickupTimeChosen, boolean buyerConfirmTransaction, boolean hitcherConfirmTransaction, RequestStatus requestStatus, GroupPlan groupPlan, HitcherDetail hitcherDetail) {
        Id = id;
        this.pickupTimeChosen = pickupTimeChosen;
        this.buyerConfirmTransaction = buyerConfirmTransaction;
        this.hitcherConfirmTransaction = hitcherConfirmTransaction;
        this.requestStatus = requestStatus;
        this.groupPlan = groupPlan;
        this.hitcherDetail = hitcherDetail;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDateTime getPickupTimeChosen() {
        return pickupTimeChosen;
    }

    public void setPickupTimeChosen(LocalDateTime pickupTimeChosen) {
        this.pickupTimeChosen = pickupTimeChosen;
    }

    public boolean isBuyerConfirmTransaction() {
        return buyerConfirmTransaction;
    }

    public void setBuyerConfirmTransaction(boolean buyerConfirmTransaction) {
        this.buyerConfirmTransaction = buyerConfirmTransaction;
    }

    public boolean isHitcherConfirmTransaction() {
        return hitcherConfirmTransaction;
    }

    public void setHitcherConfirmTransaction(boolean hitcherConfirmTransaction) {
        this.hitcherConfirmTransaction = hitcherConfirmTransaction;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public GroupPlan getGroupPlan() {
        return groupPlan;
    }

    public void setGroupPlan(GroupPlan groupPlan) {
        this.groupPlan = groupPlan;
    }

    public HitcherDetail getHitcherDetail() {
        return hitcherDetail;
    }

    public void setHitcherDetail(HitcherDetail hitcherDetail) {
        this.hitcherDetail = hitcherDetail;
    }
}
