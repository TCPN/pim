package pim_data;

import java.io.Serializable;

public class Member implements Serializable{
	
	//ATTRIBUTES:
	
	int mbID;
	String mbEmail;
	String mbName;
	
	//CONSTRUCTOR:
	
	public Member(int mbID, String mbEmail, String mbName){
		this.mbID = mbID;
		this.mbEmail = mbEmail;
		this.mbName = mbName;
	}

	public int getMbID() {
		return mbID;
	}

    public String getMbEmail() {
        return mbEmail;
    }

    public void setMbEmail(String mbEmail) {
        this.mbEmail = mbEmail;
    }

    public String getMbName() {
        return mbName;
    }

    public void setMbName(String mbName) {
        this.mbName = mbName;
    }
}