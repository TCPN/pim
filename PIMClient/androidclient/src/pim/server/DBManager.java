package pim.server;
import pim.*;
import pim_data.MeetingMinutes;
import pim_data.MeetingMinutesAbstract;
import pim_data.MeetingMinutesContent;
import pim_data.Member;
import pim_data.Project;
import pim_data.ProjectMember;

import java.io.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/*
	public MB login(String userEmail, String userPassword);
	public boolean forget_password(String userEmail) throws Exception;
	public boolean register(String userEmail, String userPassword, String userName);											// return boolean or the new memberID ?
	public MB get_member_setting(int mbID);																					// member settings should be seperate with member public infos into two classes
	public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName);							//need to check email duplication. Will server do it?
	public boolean create_new_project(int mbID, String pjName, String pjGoal, String pjDeadline, int pjManager);					//return boolean or PJ
	public boolean update_project_setting(int pjID, int mbID, String pjName, String pjGoal, String pjDeadline, int pjManagerID);	//return boolean or PJ
	public MBInfo find_member_with_email(String userEmail);																	//should only pass member ID & Name
	public boolean invite(int mbID, int pjID);
	public boolean respond_to_invitation(int mbID, int pjID, boolean accept);
	public ArrayList<MMInfo> get_timeline(int pjID);																				//should not pass all MM content back at one request
	public boolean create_new_MM(int pjID, MM mmContent, String mmObjective, Date mmMmeetingDate);
	public boolean update_old_MM(int pjID, int mmID, MM mmContent, String mmObjective, Date mmMeetingDate);
	public MM read_MM(int pjID, int mmID);
	public ArrayList<PJMB> get_project_member_list(int pjID);
*/

/** Set Time to Date
 * Calendar cal = Calendar.getInstance();
 * cal.set(Calendar.HOUR_OF_DAY,17);
 * cal.set(Calendar.MINUTE,30);
 * cal.set(Calendar.SECOND,0);
 * cal.set(Calendar.MILLISECOND,0);
 * 
 * Date d = cal.getTime();
 *
 */

public class DBManager implements DBAPI{

    //connector to db
    private  DBConnector db;

    public DBManager() throws Exception {
        this.db = new DBConnector("localhost:3306/pim", "root", "root");
    }

	public boolean isConnected(){return db.isConnected();}

	public Member login(String userEmail, String userPassword) /*throws Exception*/ {
		if (db.getMemberIDbyEmail(userEmail)==-1)
			return null;
		int mbID = db.getMemberIDByLogIn(userEmail, userPassword);
		if (mbID==-1)
		{
			//-----Login failed
			System.out.println("login failed");
			return null;
		}
		else
		{
			System.out.println("ID get: "+ mbID);
			Member member;
			member=db.getMemberAsObject(mbID);
			if(member == null)
				System.out.println("get as Object failed");
			return member;
		}
	}
	
	public  boolean forget_password(String userEmail) /*throws Exception*/ {
		int mbID = db.getMemberIDbyEmail(userEmail);
		if(mbID==-1)
			return false;
		else
		{
            System.out.println("Found match user. Send new password");
            String password = db.getPassword(userEmail);
            String new_password = password.substring(8);
			//new_password = md5(new_password);
			new_password = password.substring(8);
			//member.setMbPassword( md5(new_password) );
            Member member = db.getMemberAsObject(mbID);
            if(member == null){
                return false;
            }
			if(0 < db.updateMember(member.getMbID(), member.getMbEmail(), PIMSecurityManager.md5Encoder(new_password), member.getMbName()))
            {
                new SendMail(userEmail, "Your Password in PIM", "Hello, " + userEmail + " Your New Password for PIM App is " + new_password);
                return true;
            }
            else
            {
                return false;
            }
		}
	}
	
	public boolean register(String userEmail, String userPassword, String userName) /*throws Exception*/	{
		int mbID;
		mbID = db.getMemberIDbyEmail(userEmail);	//to be modify
		if(mbID!=-1)
		{
            System.out.print("failed, email exist");
			return false;	//member existed
		}
		else
		{
			if(-1 != db.createMember(userEmail, userPassword, userName))
                return true;
            else
			    return false;
		}
		
	}
	
	public Member get_member_setting(int mbID) /*throws Exception*/ {
		Member mb;
		mb = db.getMemberAsObject(mbID);
		if(mb==null)
			return null;
		mb = db.getMemberAsObject(mbID);
		return mb;
	}
		
	public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName) /*throws Exception*/ {
		Member mb;
		mb = db.getMemberAsObject(mbID);
		if(mb==null)
			return false;
		db.updateMember(mbID, mbEmail, mbPassword, mbName);
		return true;
	}
		
	public ArrayList<Project> get_project_List(int mbID) /*throws Exception*/ {
		ArrayList<Project> pjList = new ArrayList<Project>();
		List<Integer> pjidList = db.getPJidList(mbID, 1); 
		if(pjidList==null)
			return null;
		for(int i=0;i<pjidList.size();i++){
			pjList.add(db.getProject(pjidList.get(i)));
		}
		return pjList;
	}

	public ArrayList<Project> get_invitation_project_List(int mbID) /*throws Exception*/ {
		ArrayList<Project> pjList = new ArrayList<Project>();
		List<Integer> pjidList = db.getPJidList(mbID, 0); 
		if(pjidList==null)
			return null;
		for(int i=0;i<pjidList.size();i++){
			pjList.add(db.getProject(pjidList.get(i)));
		}
		return pjList;
	}

	public Project get_project_setting(int pjID) /*throws Exception*/ {
		return db.getProject(pjID);
	}

    public boolean create_new_project(int mbID, String pjName, String pjGoal, java.util.Date pjDeadline) /*throws Exception*/ {
        Member mb;
        mb = db.getMemberAsObject(mbID);
        if(mb==null)
            return false;
		/*Calendar utilDeadline = new GregorianCalendar(2015 ,9 , 26); // yyyy, M, dd
		java.sql.Date sqlDeadline = new java.sql.Date(utilDeadline.getTimeInMillis());*/
        // don't use Date Constructor (as follow):
        // Date deadline = new Date(2015,9,26);
        // Date Constructor has been deprecate
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(pjDeadline);
        java.sql.Date sqlDeadline = new java.sql.Date(deadline.getTimeInMillis());

        int pjID = db.createProject(pjName, pjGoal, mb.getMbName(), sqlDeadline);
        //int pjID = db.createProject(pjName, pjGoal, pjManager, sqlDeadline);

        if (pjID != -1) {
            // PP1b.	new a pjmbTable (pjID, mbID, pjmbRole, pjmbIsManager)
            //				return 1
            if(-1 != db.createPJMB(pjID, mbID, "Project Manager", 0, 1))
                return false;
            else
                return true;

        }
        else
            return false;
        //PJ newPJ = getPJ(pjID);
    }

    public boolean create_new_project(int mbID, String pjName, String pjGoal, java.util.Date pjDeadline, ArrayList<String> emaillist) /*throws Exception*/ {
        Member mb;
        mb = db.getMemberAsObject(mbID);
        if(mb==null)
            return false;
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(pjDeadline);
        java.sql.Date sqlDeadline = new java.sql.Date(deadline.getTimeInMillis());

        int pjID = db.createProject(pjName, pjGoal, mb.getMbName(), sqlDeadline);
        //int pjID = db.createProject(pjName, pjGoal, pjManager, sqlDeadline);

        if (pjID != -1) {
            // PP1b.	new a pjmbTable (pjID, mbID, pjmbRole, pjmbIsManager)
            //				return 1
            if(-1 == db.createPJMB(pjID, mbID, "Project Manager", 1, 1))
            {
                System.out.println("FAILED TO ADD INITIAL PROJECT MANAGER !!! (pjID:" + pjID + ")");
                return false;
            }
            else {
                if(emaillist == null)
                {
                    System.out.println("invite email list is not given");
                    return true;
                }
                System.out.println("invite email list size: " + emaillist.size());
                for(String email:emaillist)
                {
                    int invitedmbID = db.getMemberIDbyEmail(email) ;
                    if(invitedmbID != -1)
                        invite(invitedmbID, pjID) ;
                }
                return true;
            }

        }
        else
            return false;
        //PJ newPJ = getPJ(pjID);
    }

    public boolean update_project_setting(int pjID, int mbID, String pjName, String pjGoal, Date pjDeadline, int newPjManagerID) /*throws Exception*/ {
		if(db.isManager(pjID, mbID) != 1) {
            System.out.println("not authorized");
            return false;
        }
		
        Project  pj = db.getProject(pjID);
		if(pj == null) {
            System.out.println("Invalid project ID");
            return false;
        }

		java.sql.Date sqlDeadline = (java.sql.Date)pjDeadline;
		
		if(mbID != newPjManagerID)	//need to change the manager
			db.updateManager(pjID, mbID, newPjManagerID);
		
		int rowAffected = db.updateProject(pjID, pjName, pjGoal, sqlDeadline);
		if(rowAffected == 1)
			return true;
		else
			return false;
	}
		
	public Member find_member_with_email(String userEmail) /*throws Exception*/ {
		
		int mbID = db.getMemberIDbyEmail(userEmail);
		if(mbID==-1)
			return null;
		Member mb = db.getMemberAsObject(mbID);
		return mb;
	}
	
	public boolean invite(int mbID, int pjID) /*throws Exception*/ {
		//member not existed
		Member existing = db.getMemberAsObject(mbID);
		if(existing == null)
			return false;
		
		//member existed in pjmbtable

        if(db.getPJMB(pjID, mbID) != null)
        return false;

		int result = db.createPJMB(pjID, mbID, "", 0, 0);
		if (result == -1)
			return false;
		else
			return true;
	}
		
	public boolean respond_to_invitation(int mbID, int pjID, boolean accept) /*throws Exception*/ {
		int existed=-1;
        existed = db.isActive(pjID, mbID);
        if(existed == 1)		//already active
            return false;
		
		
		if(accept == false)	//reject
		{
			int result = db.deletePJMB(pjID, mbID);
			if(result == -1)
				return false;
			else
				return true;
		}
		else				//accept
		{
            int result = db.updatePJMBActivity(mbID, pjID, 1);
            return true;
		}
		
	}
	
    public ArrayList<MeetingMinutesAbstract> get_timeline(int pjID) /*throws Exception*/ {
		List<Integer> mmidList;
		ArrayList<MeetingMinutesAbstract> MMAlist = new ArrayList<MeetingMinutesAbstract>();
		mmidList = db.getMMidList(pjID);
		if (mmidList==null)
			return null;
		
		for (int i = 0; i <= mmidList.size()-1; i++){
            MeetingMinutes mm = read_MM(mmidList.get(i));
			
		    MeetingMinutesAbstract tempmma = new MeetingMinutesAbstract(pjID, mmidList.get(i), db.getMMLastModified(mmidList.get(i)), mm.getMeetingTime(), mm.getObjective());

		    MMAlist.add(tempmma);
		}
		return MMAlist;
	}
		
	public boolean create_new_MM(int pjID, MeetingMinutesContent mmContent) /*throws Exception*/ {
		int result = db.createMM(pjID, (Object)mmContent);
		if(result < 1)
			return false;
		
		return true;
	}
		
	public boolean update_old_MM(int mmID, MeetingMinutesContent mmContent) /*throws Exception*/ {
		boolean result = db.updateMM(mmID, mmContent);
		return result;
	}
		
	public MeetingMinutes read_MM(int mmID) /*throws Exception*/ {
		Object o = db.getMMcontent(mmID);
		MeetingMinutesContent mmc = (MeetingMinutesContent)o;
        MeetingMinutes mm = new MeetingMinutes(db.getPJIDofMM(mmID), mmID, db.getMMLastModified(mmID), mmc);
        return mm;
	}

    public ArrayList<ProjectMember> get_active_project_member_list(int pjID) /*throws Exception*/ {
		Project pj = db.getProject(pjID);
		if(pj==null)
			return null;
		
		List<Integer> pjmbIdList = new ArrayList<Integer>();
        pjmbIdList = db.getPjmbIdList(pjID);
		
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();
		
		for (int i = 0; i <= pjmbIdList.size()-1; i++){
            int mbID = pjmbIdList.get(i);
			
			ProjectMember pjmb = null;
            if(db.isActive(pjID, mbID) == 1) {
                pjmb = db.getPJMB(pjID, mbID);
                if (pjmb != null) {
                    list.add(pjmb);
                }
            }
		}
		return list;
	}
	
	public ArrayList<ProjectMember> get_inactive_project_member_list(int pjID) /*throws Exception*/ {
        Project pj = db.getProject(pjID);
        if(pj==null)
            return null;

        List<Integer> pjmbIdList = new ArrayList<Integer>();
        pjmbIdList = db.getPjmbIdList(pjID);

        ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

        for (int i = 0; i <= pjmbIdList.size()-1; i++){
            int mbID = pjmbIdList.get(i);

            ProjectMember pjmb = null;
            if(db.isActive(pjID, mbID) == 0) {
                pjmb = db.getPJMB(pjID, mbID);
                if (pjmb != null) {
                    list.add(pjmb);
                }
            }
        }
        return list;
	}

	public static void main(String args[]){
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String cin = "";
		try{
			DBManager dbcn = new DBManager();
			if(!dbcn.isConnected())
			{
				System.out.println("Connect failed. Quit.");
				return;
			}
			do{
				try{
					cin = r.readLine();
					if(cin == null)
						break;
						String[] inputs = cin.split(" ");
						if(inputs[0].toLowerCase().equals("login"))
						{
							Member mb = dbcn.login(inputs[1], inputs[2]);
							System.out.println("Member: id=" + mb.getMbID() +" name=" + mb.getMbName());
						}
						else if(inputs[0].toLowerCase().equals("register"))
						{
							boolean b = dbcn.register(inputs[1], inputs[2], inputs[3]);
							System.out.println("Return: " + b );
						}
						else
						{
							System.out.println("Undefined command.");
						}
				} catch(Exception e) {
					System.out.println(e);
				}
			}while(true);
		}catch(Exception e){
				System.out.println(e);
		}
	}
}

