package com.example.ad_project_kampung_unite.enums;

public enum Timeslot {
	TS1("8"),
	TS2("9"),
	TS3("10"),
	TS4("11"),
	TS5("12"),
	TS6("13"),
	TS7("14"),
	TS8("15"),
	TS9("16"),
	TS10("17"),
	TS11("18"),
	TS12("19"),
	TS13("20");
	
	private final String displayTimeslot;
	
	Timeslot (String displayTimeslot){
		this.displayTimeslot = displayTimeslot;
	}
	
	public String getTimeslot() {
		return displayTimeslot;
	}
	
}