package com.example.androidclient;


import java.util.*;

//class MB{}
class MBinfo{}
//class PJ{}
//class PJMB{}
//class MM{}
class MMinfo{}

public interface API
{
	public MB login(String userEmail, String userPassword);
	public boolean forget_password(String userEmail);
	public boolean register(String userEmail, String userPassword, String userName);											// return boolean or the new memberID ?
	public MB get_member_setting(int mbID);																					// member settings should be seperate with member public infos into two classes
	public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName);							//need to check email duplication. Will server do it?
	public PJ create_new_project(int mbID, String pjName, String pjGoal, String pjDeadline, int pjManager);					//return boolean or PJ
	public PJ update_project_setting(int pjID, int mbID, String pjName, String pjGoal, String pjDeadline, int pjManagerID);	//return boolean or PJ
	public MBinfo find_member_with_email(String userEmail);																	//should only pass member ID & Name
	public boolean invite(int mbID, int pjID);
	public boolean respond_to_invitation(int mbID, int pjID, boolean accept);
	public List<MMinfo> get_timeline(int pjID);																				//should not pass all MM content back at one request
	public boolean create_new_MM(int pjID, String mmContent, String mmObjective, Date mmMmeetingDate);
	public boolean update_old_MM(int pjID, int mmID, String mmContent, String mmObjective, Date mmMeetingDate);
	public MM read_MM(int pjID, int mmID);
	public List<PJMB> get_project_member(int pjID);

	public enum Message
	{
		SUCCESS,
		REQUEST_TIMEOUT,		//
		INVALID_VA,				//parameter wrong
		ILLEGAL_ACCESS,			//
		ERROR					//others
	}

}