package pim.server;
import pim.*;
import pim.MeetingMinutes;
import pim.MeetingMinutesAbstract;
import pim.MeetingMinutesContent;
import pim.Member;
import pim.Project;
import pim.ProjectMember;

import java.io.*;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	private  DBConnector db = new DBConnector("localhost:3306/pim", "root", "root");
	
	public boolean isConnected(){return db.isConnected();}
	
	private Project getPJ(int pjid)	{
		ResultSet rs = db.getProjectAsResultSet(pjid);
		if(rs==null)
			return null;
		int pjID = 0;
		String pjName = null;
		String pjGoal = null;
		String pjManager = null;
		java.sql.Date pjDeadline = null;
		try {
			while(rs.next()) {
				pjID = rs.getInt("pjID");
				pjName = rs.getString("pjName");
				pjGoal = rs.getString("pjGoal");
				pjManager = rs.getString("pjManager");
				pjDeadline = rs.getDate("pjDeadline");
			}
			
			Project pj = new Project(pjID, pjName, pjGoal, db.getPjManagerID(pjID), pjManager, pjDeadline);

			return pj;
		} catch (SQLException e) {
			//e.printStackTrace();
            System.out.println(e);
			return null;
		}
	}
	
	public Member login(String userEmail, String userPassword)	{
		if (db.getMemberIDbyEmail(userEmail)==-1)
			return null;
		int mbID = db.getMemberID(userEmail, userPassword);
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
	
	public  boolean forget_password(String userEmail){
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
			if(0 > db.updateMember(member.getMbID(), member.mbEmail, PIMSecurityManager.md5Encoder(password.substring(8)), member.mbName))
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
	
	public boolean register(String userEmail, String userPassword, String userName)	{
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
                return false;
            else
			    return true;
		}
		
	}
	
	public Member get_member_setting(int mbID){
		Member mb;
		mb = db.getMemberAsObject(mbID);
		if(mb==null)
			return null;
		mb = db.getMemberAsObject(mbID);
		return mb;
	}
		
	public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName){
		Member mb;
		mb = db.getMemberAsObject(mbID);
		if(mb==null)
			return false;
		db.updateMember(mbID, mbEmail, mbPassword, mbName);
		return true;
	}
		
	public ArrayList<Project> get_project_List(int mbID) {
		ArrayList<Project> pjList = new ArrayList<Project>();
		List<Integer> pjidList = db.getPJidList(mbID, 1); 
		if(pjidList==null)
			return null;
		for(int i=0;i<pjidList.size();i++){
			pjList.add(getPJ(pjidList.get(i)));
		}
		return pjList;
	}

	public ArrayList<Project> get_invitation_project_List(int mbID) {
		ArrayList<Project> pjList = new ArrayList<Project>();
		List<Integer> pjidList = db.getPJidList(mbID, 0); 
		if(pjidList==null)
			return null;
		for(int i=0;i<pjidList.size();i++){
			pjList.add(getPJ(pjidList.get(i)));
		}
		return pjList;
	}

	public Project get_project_setting(int pjID) {
		return getPJ(pjID);
	}

    public boolean create_new_project(int mbID, String pjName, String pjGoal, java.util.Date pjDeadline){
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

        int pjID = db.createProject(pjName, pjGoal, mb.mbName, sqlDeadline);
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

    public boolean create_new_project(int mbID, String pjName, String pjGoal, java.util.Date pjDeadline, ArrayList<String> emaillist){
        Member mb;
        mb = db.getMemberAsObject(mbID);
        if(mb==null)
            return false;
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(pjDeadline);
        java.sql.Date sqlDeadline = new java.sql.Date(deadline.getTimeInMillis());

        int pjID = db.createProject(pjName, pjGoal, mb.mbName, sqlDeadline);
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

    @Override
    public boolean update_project_setting(int pjID, int mbID, String pjName, String pjGoal, Date pjDeadline, int newPjManagerID) throws Exception {
		Member mb;
		mb = db.getMemberAsObject(mbID);
		if(mb==null)
			return false;
		
		Member newmb;
		newmb = db.getMemberAsObject(newPjManagerID);
		if(newmb==null)
			return false;
		
		ResultSet pj;
		pj = db.getProjectAsResultSet(pjID);
		if(pj==null)
			return false;
		
		Calendar deadline = Calendar.getInstance();
		deadline.setTime(pjDeadline);
		java.sql.Date sqlDeadline = new java.sql.Date(deadline.getTimeInMillis());
		
		if(mbID!=newPjManagerID)	//need to change the manager
			db.updateManager(pjID, mbID, newPjManagerID, newmb.mbName);
		
		int update = db.updateProject(pjID, pjName, pjGoal, newmb.mbName, sqlDeadline);
		if(update==1)
			return true;
		else
			return false;
	}
		
	public Member find_member_with_email(String userEmail){
		
		int mbID = db.getMemberIDbyEmail(userEmail);
		if(mbID==-1)
			return null;
		Member mb = db.getMemberAsObject(mbID);
		return mb;
	}
	
	public boolean invite(int mbID, int pjID){
		//member not existed
		Member existing = db.getMemberAsObject(mbID);
		if(existing == null)
			return false;
		
		//member existed in pjmbtable
		
		try{
			if(db.getPJMB(pjID, mbID) != null)
			return false;
		} catch(SQLException e){}

		int result = db.createPJMB(pjID, mbID, "", 0, 0);
		if (result == -1)
			return false;
		else
			return true;
	}
		
	public boolean respond_to_invitation(int mbID, int pjID, boolean accept){
		int existed=-1;
		try{
			existed = db.isActive(pjID, mbID);
			if(existed == 1)		//already active
				return false;
		}catch (SQLException e){	//not existed
			return false;
		}
		
		
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
			try{
				int result = db.updatePJMBActivity(mbID, pjID, 1);
				return true;
			} catch(SQLException e){
				return false;
			}
		}
		
	}
	
    public ArrayList<MeetingMinutesAbstract> get_timeline(int pjID){
		List<Integer> mmidList;
		ArrayList<MeetingMinutesAbstract> MMAlist = new ArrayList<MeetingMinutesAbstract>();
		mmidList = db.getMMidList(pjID);
		if (mmidList==null)
			return null;
		
		for (int i = 0; i <= mmidList.size()-1; i++){
            MeetingMinutes mm = read_MM(mmidList.get(i));
			
		    MeetingMinutesAbstract tempmma = new MeetingMinutesAbstract(pjID, mmidList.get(i), db.getMMLastModified(mmidList.get(i)), mm.content.meetingTime, mm.content.objective);

		    MMAlist.add(tempmma);
		}
		return MMAlist;
	}
		
	public boolean create_new_MM(int pjID, MeetingMinutesContent mmContent){
		int result = db.createMM(pjID, (Object)mmContent);
		if(result < 1)
			return false;
		
		return true;
	}
		
	public boolean update_old_MM(int mmID, MeetingMinutesContent mmContent){
		if(db.verifyMMcontent(mmID)==false)
			return false;
		boolean result = db.updateMM(mmID, (Object)mmContent);
		
		return result;
	}
		
	public MeetingMinutes read_MM(int mmID){
		Object o = db.getMMcontent(mmID);
		MeetingMinutesContent mmc = (MeetingMinutesContent)o;
        MeetingMinutes mm = new MeetingMinutes(mmID, db.getPJIDofMM(mmID), db.getMMLastModified(mmID), mmc);
        return mm;
	}

    public ArrayList<ProjectMember> get_active_project_member_list(int pjID){
		ResultSet pj;
		pj = db.getProjectAsResultSet(pjID);
		if(pj==null)
			return null;
		
		List<Integer> pjmbIdList = new ArrayList<Integer>();
		try{
			pjmbIdList = db.getPjmbIdList(pjID);
		}catch(SQLException e){
			//e.printStackTrace();
			return null;
		}
		
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();
		
		for (int i = 0; i <= pjmbIdList.size()-1; i++){
            int mbID = pjmbIdList.get(i);
			
			ProjectMember pjmb = null;
			try{
                if(db.isActive(pjID, mbID) == 1) {
                    pjmb = db.getPJMB(pjID, mbID);
                    if (pjmb != null) {
                        list.add(pjmb);
                    }
                }
            }catch(SQLException e){
                //e.printStackTrace();
                return null;
            }
		}
		return list;
	}
	
	public ArrayList<ProjectMember> get_inactive_project_member_list(int pjID){
        ResultSet pj;
        pj = db.getProjectAsResultSet(pjID);
        if(pj==null)
            return null;

        List<Integer> pjmbIdList = new ArrayList<Integer>();
        try{
            pjmbIdList = db.getPjmbIdList(pjID);
        }catch(SQLException e){
            //e.printStackTrace();
            return null;
        }

        ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

        for (int i = 0; i <= pjmbIdList.size()-1; i++){
            int mbID = pjmbIdList.get(i);

            ProjectMember pjmb = null;
            try{
                if(db.isActive(pjID, mbID) == 0) {
                    pjmb = db.getPJMB(pjID, mbID);
                    if (pjmb != null) {
                        list.add(pjmb);
                    }
                }
            }catch(SQLException e){
                //e.printStackTrace();
                return null;
            }
        }
        return list;
	}
	
	//adtional method for invitation list
	public ArrayList<Project> getInvitationList(int mbID){
        ArrayList<Integer> pjIDlist = null;
        ArrayList<Project> pjlist = null;
        try {
            pjIDlist = db.getPJidList(mbID, 0);
            pjlist = new ArrayList<Project>();
            for (Integer pjID : pjIDlist) {
                if (-1 != db.isActive(pjID, mbID)) {
                    pjlist.add(getPJ(pjID));
                }

            }
        }catch (SQLException e) {
        }
		return pjlist ;
		
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
							System.out.println("Member: id=" + mb.getMbID() +" name=" + mb.mbName);
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

