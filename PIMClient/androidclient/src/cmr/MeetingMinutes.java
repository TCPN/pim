package cmr;

import java.io.Serializable;
import java.util.List;
import java.sql.Timestamp;

public class MeetingMinutes implements Serializable {

	/**
	 * 	GENERATED Serial Version ID
	 */
	private static final long serialVersionUID = -8615222126848891677L;
	
	// ATTRIBUTES for MM document: (以下是老師的  MM 範本內所包含的所有元素)
	java.util.Date		dateTime;
	String				location;
	String				facilitator;
	String				recorder;
	String				objective;
	List<String>		participantList;
	String				agenda;
	String				issue;
	List<ActionItem>	actionItemList;
	
	// ATTRIBUTES for DB storage:
	int			projectID;
	int			meetingMinutesID;
	Timestamp	meetingMinutesLastModified;
	
	/**
	 * 請注意：
	 * 
	 * @param dateTime
	 * 若依據老師的 MM 格式，Date 與 Time 是分開表列的。但 java.sql.Date 型態的 data 已同時包含了這兩個元素，
	 * 所以在此將兩者合而為一，變數名稱為 dateTime。前端可透過 getter 將 dateTime 的值一次取出，再分開顯示即可。
	 * 
	 * @param parList
	 * 這是<PAR> 物件的 List。<PAP> 類別已 implements Serializable，其物件可被 序列化。
	 * 
	 * @param actList
	 * 這是<ACT> 物件的 List。<ACT> 類別已 implements Serializable，其物件可被 序列化。
	 * 
	 * @param mmLastModified
	 * 前端使用的 MM constructor 不會包含此參數
	 * 後端使用的 MM constructor 必須包含此參數。
	 */

	// 這是後端專用的建構式，前端請根據實作上的需要，在另外兩個建構式中任選一個使用。
	// CONSTRUCTOR for Back-End (後端使用):
	public MeetingMinutes(
		java.util.Date dateTime, 
		String location,
		String facilitator,
		String recorder,
		String objective,
		List<String> participantList,
		String agenda,
		String issue,
		List<ActionItem> actionItemList,
		int projectID,
		int meetingMinutesID,
		Timestamp meetingMinutesLastModified	// ��ݨϥΪ� constructor �h�F DB �۰ʥͦ��� �ɶ��W�O�C
	) {
		// for MM document
		this.dateTime = dateTime;
		this.location = location;
		this.facilitator = facilitator;
		this.recorder = recorder;
		this.objective = objective;
		this.participantList = participantList;
		this.agenda = agenda;
		this.issue = issue;
		this.actionItemList = actionItemList;
		// for DB storage
		this.projectID = projectID;
		this.meetingMinutesID = meetingMinutesID;
		this.meetingMinutesLastModified = meetingMinutesLastModified;
	}
	
	// 前端：如果可以直接將 List<PAR> 與 List<ACT> 包進 MM 一併序列化的話就使用以下這個建構式。
	// CONSTRUCTOR for Front-End 1(前端使用) --> 
	public MeetingMinutes(
		java.util.Date dateTime, 
		String location,
		String facilitator,
		String recorder,
		String objective,
		List<String> participantList,
		String agenda,
		String issue,
		List<ActionItem> actionItemList,
		int projectID
	) {
		this.dateTime = dateTime;
		this.location = location;
		this.facilitator = facilitator;
		this.recorder = recorder;
		this.objective = objective;
		this.participantList = participantList;
		this.agenda = agenda;
		this.issue = issue;
		this.actionItemList = actionItemList;
		this.projectID = projectID;
		// 前端使用的 constructor 不包含 DB 自動生成的 mmID 與 mmLastModified 時間戳記 (因為生成物件時不可能有此參數)
	}
	
	// 前端：如果不能直接將 List<PAR> 與 List<ACT> 包進 MM 一併序列化的話就使用以下這個建構式。
	// CONSTRUCTOR for Front-End 2(前端使用) --> 
	public MeetingMinutes(
		java.util.Date dateTime, 
		String location,
		String facilitator,
		String recorder,
		String objective,
		String agenda,
		String issue,
		int projectID
	) {
		this.dateTime = dateTime;
		this.location = location;
		this.facilitator = facilitator;
		this.recorder = recorder;
		this.objective = objective;
		this.agenda = agenda;
		this.issue = issue;
		this.projectID = projectID;
		// 前端使用的 constructor 不包含 DB 自動生成的 mmID 與 mmLastModified 時間戳記 (因為生成物件時不可能有此參數)
	}
	
	// GETTERS:
	public static long getSerialversionuid() { return serialVersionUID; }
	public java.util.Date getDateTime() { return dateTime; }
	public String getLocation() { return location; }
	public String getFacilitator() { return facilitator; }
	public String getRecorder() { return recorder; }
	public String getObjective() { return objective; }
	public List<String> getParticipantList() { return participantList; }
	public String getAgenda() { return agenda; }
	public String getIssue() { return issue; }
	public List<ActionItem> getActionItemList() { return actionItemList; }

	// SETTERS:
	public void setDateTime(java.util.Date dateTime) { this.dateTime = dateTime; }
	public void setLocation(String location) { this.location = location; }
	public void setFacilitator(String facilitator) { this.facilitator = facilitator; }
	public void setRecorder(String recorder) { this.recorder = recorder; }
	public void setObjective(String objective) { this.objective = objective; }
	public void setParticipantList(List<String> participantList) { this.participantList = participantList; }
	public void setAgenda(String agenda) { this.agenda = agenda; }
	public void setIssue(String issue) { this.issue = issue; }
	public void setActionItemList(List<ActionItem> actionItemList) { this.actionItemList = actionItemList; }

}
