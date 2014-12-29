package pim;

import java.io.Serializable;
import java.util.Date;

public class MeetingMinutes implements Serializable {

    int pjID;
    int mmID;
    Date lastModifyTime;
	public MeetingMinutesContent content;

	public MeetingMinutes(
        int mmID,
		int pjID,
        Date lastModifyTime,
        MeetingMinutesContent content
	){
        this.mmID = mmID;
        this.pjID = pjID;
        this.lastModifyTime = lastModifyTime;
        this.content = content;
	}
	public MeetingMinutes(MeetingMinutesAbstract mmab)
	{
		this.lastModifyTime = mmab.getLastModifyTime() ;
		this.content.meetingTime = mmab.getMeetingTime() ;
        this.content.objective = mmab.getObjective() ;
		this.mmID = mmab.getmmid() ;
		this.pjID = mmab.getpjid() ;
	}

    public int getpjid() { return pjID; }
    public int getmmid() { return mmID; }
    public Date getLastModifyTime() { return lastModifyTime; }
    public MeetingMinutesContent getContent(){return content ;}
}
