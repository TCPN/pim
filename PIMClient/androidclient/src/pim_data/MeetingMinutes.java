package pim_data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MeetingMinutes implements Serializable {

    int pjID;
    int mmID;
    Date lastModifyTime;
    Date meetingTime;
    String location;
    String facilitator;
    String recorder;
    String objective;
    ArrayList<String> parList;
    String agenda;
    String issue;
    ArrayList<ActionItem> actList;


    public MeetingMinutes(
        int mmID,
		int pjID,
        Date lastModifyTime,
        Date meetingTime,
        String location,
        String facilitator,
        String recorder,
        String objective,
        ArrayList<String> parList,
        String agenda,
        String issue,
        ArrayList<ActionItem> actList
	){
        this.mmID = mmID;
        this.pjID = pjID;
        this.lastModifyTime = lastModifyTime;
        this.meetingTime = meetingTime;
        this.location = location;
        this.facilitator = facilitator;
        this.recorder = recorder;
        this.objective = objective;
        this.parList = parList;
        this.agenda = agenda;
        this.issue = issue;
        this.actList = actList;
	}
	public MeetingMinutes(MeetingMinutesAbstract mmab)
	{
		this.lastModifyTime = mmab.getLastModifyTime() ;
		this.meetingTime = mmab.getMeetingTime() ;
        this.objective = mmab.getObjective() ;
		this.mmID = mmab.getMMId() ;
		this.pjID = mmab.getPJId() ;
	}
    public MeetingMinutes(int pjID, int mmID, Date lastModifyTime, MeetingMinutesContent mmc)
    {
        this.pjID = pjID;
        this.mmID = mmID;
        this.lastModifyTime = lastModifyTime;
        this.meetingTime = mmc.meetingTime;
        this.location = mmc.location;
        this.facilitator = mmc.facilitator;
        this.recorder = mmc.recorder;
        this.objective = mmc.objective;
        this.parList = mmc.parList;
        this.agenda = mmc.agenda;
        this.issue = mmc.issue;
        this.actList = mmc.actList;
    }
    public MeetingMinutesContent getContent(){return new MeetingMinutesContent(this) ;}

    public int getPjID() {
        return pjID;
    }

    public void setPjID(int pjID) {
        this.pjID = pjID;
    }

    public int getMmID() {
        return mmID;
    }

    public void setMmID(int mmID) {
        this.mmID = mmID;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Date getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Date meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public ArrayList<String> getParList() {
        return parList;
    }

    public void setParList(ArrayList<String> parList) {
        this.parList = parList;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public ArrayList<ActionItem> getActList() {
        return actList;
    }

    public void setActList(ArrayList<ActionItem> actList) {
        this.actList = actList;
    }
}
