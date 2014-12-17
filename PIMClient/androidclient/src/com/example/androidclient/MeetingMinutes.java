package com.example.androidclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MeetingMinutes implements Serializable {

	public Date dateTime;
	public String location;
	public String facilitator;
	public String recorder;
	public String objective;
	public ArrayList<Participant> parList;
	public String agenda;
	public String issue;
	public ArrayList<ActionItem> actList;
	
	// ATTRIBUTES for DB storage:
	int pjID;
	int mmID;
	
	/**
	 */

	public MeetingMinutes(
		Date dateTime, 
		String location,
		String facilitator,
		String recorder,
		String objective,
		ArrayList<Participant> parList,
		String agenda,
		String issue,
		ArrayList<ActionItem> actList
	){
		this.dateTime = dateTime;
		this.location = location;
		this.facilitator = facilitator;
		this.recorder = recorder;
		this.objective = objective;
		this.parList = parList;
		this.agenda = agenda;
		this.issue = issue;
		this.actList = actList;
	}
	
	// GETTERS:
	public int getmmID(){return mmID;}
	public int getpjID(){return pjID;}
	// SETTERS:

	
}
