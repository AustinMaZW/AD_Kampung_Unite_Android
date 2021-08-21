package com.example.ad_project_kampung_unite.entities.enums;

public enum HitchBuyRole {
	HITCHER("Hitcher"),
	BUYER("Buyer");
	
	private final String displayHitchBuyRole;
	
	HitchBuyRole (String displayHitchBuyRole){
		this.displayHitchBuyRole = displayHitchBuyRole;
	}
	
	public String getHitchBuyRole() {
		return displayHitchBuyRole;
	}
}
