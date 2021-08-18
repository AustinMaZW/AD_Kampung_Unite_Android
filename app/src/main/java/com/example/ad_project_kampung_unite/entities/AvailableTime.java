package com.example.ad_project_kampung_unite.entities;

import java.time.LocalTime;

public class AvailableTime {
	private int id;
	private GroupPlan groupPlanAT;
	private LocalTime pickupSlots;

	public AvailableTime() {
	}

	public AvailableTime(int id, GroupPlan groupPlanAT, LocalTime pickupSlots) {
		this.id = id;
		this.groupPlanAT = groupPlanAT;
		this.pickupSlots = pickupSlots;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GroupPlan getGroupPlanAT() {
		return groupPlanAT;
	}

	public void setGroupPlanAT(GroupPlan groupPlanAT) {
		this.groupPlanAT = groupPlanAT;
	}

	public LocalTime getPickupSlots() {
		return pickupSlots;
	}

	public void setPickupSlots(LocalTime pickupSlots) {
		this.pickupSlots = pickupSlots;
	}
}
