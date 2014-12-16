package com.example.androidclient;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
public class PJ implements Serializable{
	
	//ATTRIBUTES:
	
	int pjID;
	String pjName;
	String pjGoal;
	String pjManager;
	Date pjDeadline;
	Timestamp pjLastModified;
	
	//CONSTRUCTOR:
	
	public PJ(int pjID, String pjName, String pjGoal, String pjManager, Date pjDeadline, Timestamp pjLastModified){
		this.pjID = pjID;
		this.pjName = pjName;
		this.pjGoal = pjGoal;
		this.pjManager = pjManager;
		this.pjDeadline = pjDeadline;
		this.pjLastModified = pjLastModified;
	}

	//GETTERS:
	
	public int getPjID() {
		return pjID;
	}

	public String getPjName() {
		return pjName;
	}

	public String getPjGoal() {
		return pjGoal;
	}

	public Date getPjDeadline() {
		return pjDeadline;
	}

	public String getPjManager() {
		return pjManager;
	}
	
	public Timestamp getPjLastModified() {
		return pjLastModified;
	}

	//SETTERS:
	
	public void setPjID(int pjID) {
		this.pjID = pjID;
	}

	public void setPjName(String pjName) {
		this.pjName = pjName;
	}

	public void setPjGoal(String pjGoal) {
		this.pjGoal = pjGoal;
	}

	public void setPjDeadline(Date pjDeadline) {
		this.pjDeadline = pjDeadline;
	}

	public void setPjManager(String pjManager) {
		this.pjManager = pjManager;
	}
	
	public void setPjLastModified(Timestamp pjLastModified) {
		this.pjLastModified = pjLastModified;
	}
	
}
