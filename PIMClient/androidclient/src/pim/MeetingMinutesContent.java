package pim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MeetingMinutesContent implements Serializable{

    public Date meetingTime;
    public String location;
    public String facilitator;
    public String recorder;
    public String objective;
    public ArrayList<Participant> parList;
    public String agenda;
    public String issue;
    public ArrayList<ActionItem> actList;
	
    public MeetingMinutesContent(
        Date meetingTime,
        String location,
        String facilitator,
        String recorder,
        String objective,
        ArrayList<Participant> parList,
        String agenda,
        String issue,
        ArrayList<ActionItem> actList
    ){
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
}
