package cmr;

import java.io.Serializable;
import java.util.Calendar;

public class ActionItem implements Serializable {

	/**
	 * GENERATED Serial Version ID
	 */
	private static final long serialVersionUID = 7801183387972982584L;
	
	// 	ATTRIBUTES:
	int				number;
	String			action;
	String			responsibility;
	java.util.Date	deadline;
	String			status;
	String			remark;

	/**
	 * 請注意：
	 * 
	 * ACT class 的 constructor 包含了有一個 java.sql.Date 的參數
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
	
	// CONSTRUCTOR for Front-End:
	public ActionItem(int number, String action, String responsibility, java.sql.Date deadline, String status, String remark) {
		this.number = number;
		this.action = action;
		this.responsibility = responsibility;
		this.deadline = deadline;
		this.status = status;
		this.remark = remark;
	}
	
	public ActionItem(int number, String action, String responsibility, java.util.Date deadline, String status, String remark) {
		this.number = number;
		this.action = action;
		this.responsibility = responsibility;
		this.deadline = deadline;
		this.status = status;
		this.remark = remark;
	}
	
	public ActionItem(String action, String responsibility, Calendar deadline, String status, String remark) {
		this.number = 0;
		this.action = action;
		this.responsibility = responsibility;
		this.deadline = new java.sql.Date(deadline.getTimeInMillis());
		this.status = status;
		this.remark = remark;
	}

	// GETTERS:
	public static long getSerialversionuid() { return serialVersionUID; }
	public int getNumber() { return number; }
	public String getAction() { return action; }
	public String getResponsibility() { return responsibility; }
	public java.util.Date getDeadline() { return deadline; }
	public String getStatus() { return status; }
	public String getRemark() { return remark; }

	// SETTERS:
	public void setNumber(int number) { this.number = number; }
	public void setAction(String action) { this.action = action; }
	public void setResponsibility(String responsibility) { this.responsibility = responsibility; }
	public void setDeadline(java.util.Date deadline) { this.deadline = deadline; }
	public void setStatus(String status) { this.status = status; }
	public void setRemark(String remark) { this.remark = remark; }
	
}



