package pim;

import java.io.Serializable;

public class ProjectMember implements Serializable {
	
	//ATTRIBUTES:
	
	int pjID;
	int mbID;
	String mbName;
	String mbEmail;
	String pjmbRole;
	boolean isManager;
    boolean isActive;
	
	//CONSTRUCTOR:
	
	public ProjectMember(int pjID, int mbID, String mbName, String mbEmail, String pjmbRole, boolean isManager, boolean isActive){
		this.pjID = pjID;
		this.mbID = mbID;
		this.mbName = mbName;
		this.mbEmail = mbEmail;
		this.pjmbRole = pjmbRole;
        this.isManager = isManager;
        this.isActive = isActive;
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
	
	public boolean getIsManager() {
		return isManager;
	}

    public boolean getIsActive() {
        return isActive;
    }

    //SETTERS:
	
}

