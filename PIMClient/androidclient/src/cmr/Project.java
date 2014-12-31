package cmr;

import java.io.Serializable;
import java.sql.Timestamp;

public class Project implements Serializable {

	/**
	 * GENERATED Serial Version ID
	 */
	private static final long serialVersionUID = 5243300156449791626L;

	//ATTRIBUTES:
	int				projectID;
	String			projectName;
	String			projectGoal;
	String			projectManager;
	java.sql.Date	projectDeadline;
	Timestamp		projectLastModified;
	
	/**
	 * 請注意：
	 * 
	 * PJ class 的 constructor 包含了有一個 java.sql.Date 的參數
	 * 
	 * 一般 Java 所使用的 Date 型態都是 java.util.Date
	 * 但 MySQL 需要的是 java.sql.Date
	 * 
	 * 以下是將 java.util.Date 轉換成 java.sql.Date 的範例：
	 *
	 *import java.util.Date
	 *
	 *Calendar uDate = new GregorianCalendar(1900, 9, 26); // yyyy, M, dd
	 *java.sql.Date sDate = new java.sql.Date(uDate.getTimeInMillis());
	 *
	 */
	
	//CONSTRUCTOR:
	
	public Project(int projectID, String projectName, String projectGoal, String projectManager, java.sql.Date projectDeadline, Timestamp projectLastModified) {
		this.projectID = projectID;
		this.projectName = projectName;
		this.projectGoal = projectGoal;
		this.projectManager = projectManager;
		this.projectDeadline = projectDeadline;
		this.projectLastModified = projectLastModified;
	}
	
	public Project(int projectID, String projectName, String projectGoal, String projectManager, java.util.Date projectDeadline, Timestamp projectLastModified) {
		this.projectID = projectID;
		this.projectName = projectName;
		this.projectGoal = projectGoal;
		this.projectManager = projectManager;
		this.projectDeadline = new java.sql.Date(projectDeadline.getTime());;
		this.projectLastModified = projectLastModified;
	}

	//GETTERS:
	public static long getSerialversionuid() { return serialVersionUID; }
	public int getProjectID() { return projectID; }
	public String getProjectName() { return projectName; }
	public String getProjectGoal() { return projectGoal; }
	public java.sql.Date getProjectDeadline() { return projectDeadline; }
	public String getProjectManager() { return projectManager; }
	public Timestamp getProjectLastModified() { return projectLastModified; }

	//SETTERS:
	public void setProjectID(int projectID) { this.projectID = projectID; }
	public void setProjectName(String projectName) { this.projectName = projectName; }
	public void setProjectGoal(String projectGoal) { this.projectGoal = projectGoal; }
	public void setProjectDeadline(java.sql.Date projectDeadline) { this.projectDeadline = projectDeadline; }
	public void setProjectManager(String projectManager) { this.projectManager = projectManager; }
	public void setProjectLastModified(Timestamp projectLastModified) { this.projectLastModified = projectLastModified; }
	
}

