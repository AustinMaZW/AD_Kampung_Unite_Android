package com.example.ad_project_kampung_unite.entities;

import com.example.ad_project_kampung_unite.entities.enums.RequestStatus;

import java.time.LocalDateTime;

public class HitchRequest {
    private int id;
    private LocalDateTime pickupTimeChosen;
    private boolean buyerConfirmTransaction;
    private boolean hitcherConfirmTransaction;
    private RequestStatus requestStatus;
    private GroupPlan groupPlan;
    private HitcherDetail hitcherDetail;

}
