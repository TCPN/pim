package pim_data;

import java.io.Serializable;
import java.util.Date;

public class MeetingMinutesAbstract implements Serializable {

	int pjID;
	int mmID;
	Date lastModifyTime;
    Date meetingTime;
    String objective;

	public MeetingMinutesAbstract(
		int pjID,
        int mmID,
        Date lastModifyTime,
		Date meetingTime,
		String objective
	){
		this.pjID = pjID;
        this.mmID = mmID;
        this.lastModifyTime = lastModifyTime;
		this.meetingTime = meetingTime;
		this.objective = objective;
	}

	// GETTERS:
    public int getPJId() { return pjID; }
	public int getMMId() { return mmID; }
	public Date getLastModifyTime() { return lastModifyTime; }
    public Date getMeetingTime() {return meetingTime;}
    public String getObjective() {return objective;}
}
