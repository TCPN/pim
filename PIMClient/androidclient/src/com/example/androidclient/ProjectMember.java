package com.example.androidclient;

import java.io.Serializable;

public class ProjectMember implements Serializable {
	
	//ATTRIBUTES:
	
	int pjID;
	int mbID;
	String mbName;
	String mbEmail;
	String pjmbRole;
	String role;
	boolean isManager;
	
	//CONSTRUCTOR:
	
	public ProjectMember(int pjID, int mbID, String mbName, String mbEmail, String pjmbRole){
		this.pjID = pjID;
		this.mbID = mbID;
		this.mbName = mbName;
		this.mbEmail = mbEmail;
		this.pjmbRole = pjmbRole;
	}

	//GETTERS:
	
	public int getPjID() {
		return pjID;
	}

	public String getMbName() {
		return mbName;
	}

	public String getMbEmail() {
		return mbEmail;
	}

	public String getPjmbRole() {
		return pjmbRole;
	}

	//SETTERS:
	
}

