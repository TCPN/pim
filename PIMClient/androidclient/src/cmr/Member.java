package cmr;

import java.io.Serializable;
import java.sql.Timestamp;

public class Member implements Serializable {
		
	/**
	 * GENERATED Serial Version ID
	 */
	private static final long serialVersionUID = -6822233683937707214L;
	
	//ATTRIBUTES:
	int			memberID;
	String		memberEmail;
	String		memberPassword;
	String		memberName;
	Timestamp	memberLastModified;
	
	//CONSTRUCTOR:
	public Member(int memberID, String memberEmail, String memberPassword, String memberName, Timestamp memberLastModified) {
		this.memberID = memberID;
		this.memberEmail = memberEmail;
		this.memberPassword = memberPassword;
		this.memberName = memberName;
		this.memberLastModified = memberLastModified;
	}

	//GETTERS:
	public static long getSerialversionuid() { return serialVersionUID; }
	public int getMemberID() { return memberID; }
	public String getMemberEmail() { return memberEmail; }
	public String getMemberPassword() { return memberPassword; }
	public String getMemberName() { return memberName; }
	public Timestamp getMemberLastModified() { return memberLastModified; }

	//SETTERS:
	public void setMemberID(int memberID) { this.memberID = memberID; }
	public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }
	public void setMemberPassword(String memberPassword) { this.memberPassword = memberPassword; }
	public void setMemberName(String memberName) { this.memberName = memberName; }
	public void setMemberLastModified(Timestamp memberLastModified) { this.memberLastModified = memberLastModified; }
	
}

