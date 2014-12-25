package pim;

/**
 * last update time: 2014/12/17 03:37 by Daniel
 * MeetingMinutes as parameter directly. only serialize it when passing the socket and storing it at server.
 * create_new_project take parameter "deadline" as class Date object
 * remove class MMinfo & MBinfo, because we can fill the not need fields(mbPassword, mmContents) with empty values(null,0)
 * all functions throws Exception
 * 
 * I think "return boolean" is not useful for telling failure reason, so we can change them into void
 */

import java.util.ArrayList;
import java.util.Date;

import pim.MeetingMinutes;
import pim.MeetingMinutesAbstract;
import pim.MeetingMinutesContent;
import pim.Member;
import pim.Project;
import pim.ProjectMember;

public interface API {
    /**[01] will "No Matched" be an Exception ????????? */
    public Member login(String userEmail, String userPassword) throws Exception;

    /**[02] */
    public boolean forget_password(String userEmail) throws Exception;

    /**[11] return boolean or the new memberID ????????? */
    public boolean register(String userEmail, String userPassword, String userName) throws Exception;

    /**[12] member settings should get ID, Name, Email.  Which side will verify the password, server or client ?????????? */
    public Member get_member_setting(int mbID) throws Exception;

    /**[13] server will check email duplication. */
    public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName) throws Exception;

    /**[01,  ]*/
    public ArrayList<Project> get_project_List(int mbID) throws Exception;

    /**[  ]*/
    public Project get_project_setting(int pjID) throws Exception;

    /**[21] parameter int pjManager deleted */
    public boolean create_new_project(int mbID, String pjName, String pjGoal, Date pjDeadline) throws Exception;

    /**[22] return boolean or Project ???????????*/
    public boolean update_project_setting(int pjID, int mbID, String pjName, String pjGoal, Date pjDeadline, int newPjManagerID) throws Exception;

    /**[31] only get member ID, Name, Email */
    public Member find_member_with_email(String userEmail) throws Exception;

    /**[32] */
    public boolean invite(int mbID, int pjID) throws Exception;

    /**[34,35] */
    public boolean respond_to_invitation(int mbID, int pjID, boolean accept) throws Exception;

    /**[41] should not pass all MeetingMinutes content back at one request */
    public ArrayList<MeetingMinutesAbstract> get_timeline(int pjID) throws Exception;

    /**[42,43] */
    public boolean create_new_MM(int pjID, MeetingMinutesContent mmContent) throws Exception;

    /**[44]*/
    public boolean update_old_MM(int mmID, MeetingMinutesContent mmContent) throws Exception;

    /**[42]*/
    public MeetingMinutes read_MM(int mmID) throws Exception;

    /**[51]*/
    public ArrayList<ProjectMember> get_project_member_list(int pjID) throws Exception;

    /**[  ]*/
//    public ProjectMember get_member_role(int mbID, int pjID) throws Exception;

    /**[  ]*/
//    public boolean remove_project_member(int mbID, int pjID) throws Exception;

    /**[  ]*/
//    public boolean set_member_role(int mbID, int pjID, String role, boolean isManager) throws Exception;
}