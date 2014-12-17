package com.example.androidclient;

/**
 * last update time: 2014/12/17 03:37 by Daniel
 * MeetingMinutes as parameter directly. only serialize it when passing the socket and storing it at server.
 * create_new_project take parameter "deadline" as class Date object
 * remove class MMinfo & MBinfo, because we can fill the not need fields(mbPassword, mmContents) with empty values(null,0)
 * all functions throws Exception
 * 
 * I think "return boolean" is not useful for telling failure reason, so we can change them into void
 */

import java.util.*;

public interface API {
/*[01]*/	public Member login(String userEmail, String userPassword) throws Exception;
/*[02]*/	public boolean forget_password(String userEmail) throws Exception;
/*[11]*/	public boolean register(String userEmail, String userPassword, String userName) throws Exception;											// return boolean or the new memberID ?
/*[12]*/	public Member get_member_setting(int mbID) throws Exception;																					// member settings should be seperate with member public infos into two classes
/*[13]*/	public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName) throws Exception;							//need to check email duplication. Will server do it?
/*[01,  ]*/	public ArrayList<Project> get_project_List(int mbID) throws Exception;
/*[  ]*/	public Project get_project_setting(int pjID) throws Exception;
/*[21]*/	public boolean create_new_project(int mbID, String pjName, String pjGoal, Date pjDeadline) throws Exception;					//return boolean or Project
/*[22]*/	public boolean update_project_setting(int pjID, int mbID, String pjName, String pjGoal, Date pjDeadline, int pjManagerID) throws Exception;	//return boolean or Project
/*[31]*/	public Member find_member_with_email(String userEmail) throws Exception;																	//should only pass member ID & Name
/*[32]*/	public boolean invite(int mbID, int pjID) throws Exception;
/*[34,35]*/	public boolean respond_to_invitation(int mbID, int pjID, boolean accept) throws Exception;
/*[41]*/	public ArrayList<MeetingMinutes> get_timeline(int pjID) throws Exception;																				//should not pass all MeetingMinutes content back at one request
/*[42,43]*/	public boolean create_new_MM(int pjID, MeetingMinutes mmContent, String mmObjective, Date mmMmeetingDate) throws Exception;                          //for front end
/*[44]*/	public boolean update_old_MM(int pjID, int mmID, MeetingMinutes mmContent, String mmObjective, Date mmMeetingDate) throws Exception;                 //for front end
/*[42]*/	public MeetingMinutes read_MM(int pjID, int mmID) throws Exception;
/*[51]*/	public ArrayList<ProjectMember> get_project_member_list(int pjID) throws Exception;
//[  ]		public ProjectMember get_member_role(int mbID, int pjID) throws Exception;											
//[  ]		public boolean remove_project_member(int mbID, int pjID) throws Exception;
//[  ]		public boolean set_member_role(int mbID, int pjID, String role, boolean isManager) throws Exception;
}
