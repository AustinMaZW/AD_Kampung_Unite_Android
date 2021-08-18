package com.example.ad_project_kampung_unite.enums;

public enum GLStatus {
	COMPLETED("Completed"),
	ACCEPTED("Accepted"),
	PENDING("Pending"),
	CANCELLED("Cancelled"),
	DELETED("Deleted");
	
	private final String displayGLStatus;
	
	GLStatus (String displayGLStatus){
		this.displayGLStatus = displayGLStatus;
	}
	
	public String getGLStatus() {
		return displayGLStatus;
	}
	
}
