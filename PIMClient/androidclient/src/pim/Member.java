package pim;

import java.io.Serializable;

public class Member implements Serializable{
	
	//ATTRIBUTES:
	
	int mbID;
	public String mbEmail;
	public String mbName;
	
	//CONSTRUCTOR:
	
	public Member(int mbID, String mbEmail, String mbName){
		this.mbID = mbID;
		this.mbEmail = mbEmail;
		this.mbName = mbName;
	}
	
	//GETTERS:
	
	public int getMbID() {
		return mbID;
	}

	//SETTERS:

	
}