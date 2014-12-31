package cmr;

import java.io.Serializable;

public class ProjectMember implements Serializable {

	/**
	 * GENERATED Serial Version ID
	 */
	private static final long serialVersionUID = 7717544616530502056L;
	
	//ATTRIBUTES:
	int 	projectID;
	int 	memberID;
	String 	memberName;
	String 	memberEmail;
	String 	projectMemberRole;
	boolean activated = false;
	
	//CONSTRUCTOR:
	public ProjectMember(int projectID, int memberID, String memberName, String memberEmail, String projectMemberRole, boolean activated) {
		this.projectID = projectID;
		this.memberID = memberID;
		this.memberName = memberName;
		this.memberEmail = memberEmail;
		this.projectMemberRole = projectMemberRole;
		this.activated = activated;
	}

	//GETTERS:
	public static long getSerialversionUid() { return serialVersionUID; }
	public int getProjectID() { return projectID; }
	public int getMemberID() { return memberID; }
	public String getMemberName() { return memberName; }
	public String getMemberEmail() { return memberEmail; }
	public String getProjectMemberRole() { return projectMemberRole; }
	public boolean getActivated() { return activated; }

	//SETTERS:
	public void setProjectID(int projectID) { this.projectID = projectID; }
	public void setMemberID(int memberID) { this.memberID = memberID; }
	public void setMemberName(String memberName) { this.memberName = memberName; }
	public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }
	public void setProjectMemberRole(String projectMemberRole) { this.projectMemberRole = projectMemberRole; }
	public void setActivated(boolean acceptance) { this.activated = acceptance; }
	
}


