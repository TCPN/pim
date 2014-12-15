package com.example.androidclient;

public class PJMB {
	
	//ATTRIBUTES:
	
	int pjID;
	int mbID;
	String mbName;
	String mbEmail;
	String pjmbRole;
	
	//CONSTRUCTOR:
	
	public PJMB(int pjID, int mbID, String mbName, String mbEmail, String pjmbRole){
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

	public int getMbID() {
		return mbID;
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
	
	public void setPjID(int pjID) {
		this.pjID = pjID;
	}

	public void setMbID(int mbID) {
		this.mbID = mbID;
	}

	public void setMbName(String mbName) {
		this.mbName = mbName;
	}

	public void setMbEmail(String mbEmail) {
		this.mbEmail = mbEmail;
	}

	public void setPjmbRole(String pjmbRole) {
		this.pjmbRole = pjmbRole;
	}
	
}

