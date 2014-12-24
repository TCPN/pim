package pim;

import java.io.Serializable;
import java.util.Date;

public class Project implements Serializable{
	
	//ATTRIBUTES:
	
	int pjID;
	String pjName;
	String pjGoal;
	int pjManagerID;
	String pjManagerName;
	Date pjDeadline;
	
	//CONSTRUCTOR:
	
	public Project(int pjID, String pjName, String pjGoal, int pjManagerID, String pjManagerName, Date pjDeadline){
		this.pjID = pjID;
		this.pjName = pjName;
		this.pjGoal = pjGoal;
		this.pjManagerID = pjManagerID;
		this.pjManagerName = pjManagerName;
		this.pjDeadline = pjDeadline;
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
		return pjManagerName;
	}
	
	//SETTERS:
	
}
