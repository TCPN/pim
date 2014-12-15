package com.example.androidclient;
import java.sql.Timestamp;

public class MB implements Cloneable {
	
	//ATTRIBUTES:
	
	int mbID;
	String mbEmail;
	String mbPassword;
	String mbName;
	Timestamp mbLastModified;
	
	//CONSTRUCTOR:
	
	public MB(int mbID, String mbEmail, String mbPassword, String mbName, Timestamp mbLastModified){
		this.mbID = mbID;
		this.mbEmail = mbEmail;
		this.mbPassword = mbPassword;
		this.mbName = mbName;
		this.mbLastModified = mbLastModified;
	}
	
	
	public MB(MB mb){
		this.mbID = 123456789;
		this.mbEmail = "next@mysql.org";
		this.mbPassword = "13579";
		this.mbName = "Monday Blue";
		this.mbLastModified = null;
	}

	//GETTERS:
	
	public int getMbID() {
		return mbID;
	}

	public String getMbEmail() {
		return mbEmail;
	}

	public String getMbPassword() {
		return mbPassword;
	}

	public String getMbName() {
		return mbName;
	}

	public Timestamp getMbLastModified() {
		return mbLastModified;
	}

	//SETTERS:
	
	public void setMbID(int mbID) {
		this.mbID = mbID;
	}

	public void setMbEmail(String mbEmail) {
		this.mbEmail = mbEmail;
	}

	public void setMbPassword(String mbPassword) {
		this.mbPassword = mbPassword;
	}

	public void setMbName(String mbName) {
		this.mbName = mbName;
	}

	public void setMbLastModified(Timestamp mbLastModified) {
		this.mbLastModified = mbLastModified;
	}
	
	
}


