package pim.server;
import pim.* ;
import pim.SecurityManager;

import java.sql.Timestamp;
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
	private  DBConnector db;
	
	private  Project getPJ(int pjid)	{
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
			return null;
		}
	}
	
	public  Member login(String userEmail, String userPassword)	{
		if (db.getMemberIDbyEmail(userEmail)==-1)
			return null;
		int mbID = db.getMemberID(userEmail, userPassword);
		if (mbID==-1)
		{
			//-----Login failed
			return null;
		}
		else
		{
			Member member;
			member=db.getMemberAsObject(mbID);
			return member;
		}
	}
	
	public  boolean forget_password(String userEmail){
		int mbID = db.getMemberIDbyEmail(userEmail);
		if(mbID==-1)
			return false;
		else
		{
            String password = db.getPassword(userEmail);
            String new_password = password.substring(8);
			//new_password = md5(new_password);
			new_password = SecurityManager.md5Encoder(password.substring(8));
			//member.setMbPassword( md5(new_password) );
            Member member = db.getMemberAsObject(mbID);
			db.updateMember(member.getMbID(), member.mbEmail, new_password, member.mbName);
			
			new SendMail(userEmail, "Your Password in PIM", "Hello, " + userEmail + " Your New Password for PIM App is " + new_password);
			return true;
		}
	}
	
	public boolean register(String userEmail, String userPassword, String userName)	{
		int mbID;
		mbID = db.getMemberIDbyEmail(userEmail);	//to be modify
		if(mbID!=-1)
		{
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
        // 不要使用 Date Constructor (如下):
        // Date deadline = new Date(2015,9,26);
        // Date Constructor 已經被 deprecate 了。
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(pjDeadline);
        java.sql.Date sqlDeadline = new java.sql.Date(deadline.getTimeInMillis());

        int pjID = db.createProject(pjName, pjGoal, mb.mbName, sqlDeadline);
        //int pjID = db.createProject(pjName, pjGoal, pjManager, sqlDeadline);

        if (pjID != -1) {
            // PP1b.	[同步新增] 一筆 pjmbTable 資料 (pjID, mbID, pjmbRole, pjmbIsManager)
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
		/*Calendar utilDeadline = new GregorianCalendar(2015 ,9 , 26); // yyyy, M, dd
		java.sql.Date sqlDeadline = new java.sql.Date(utilDeadline.getTimeInMillis());*/
        // 不要使用 Date Constructor (如下):
        // Date deadline = new Date(2015,9,26);
        // Date Constructor 已經被 deprecate 了。
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(pjDeadline);
        java.sql.Date sqlDeadline = new java.sql.Date(deadline.getTimeInMillis());

        int pjID = db.createProject(pjName, pjGoal, mb.mbName, sqlDeadline);
        //int pjID = db.createProject(pjName, pjGoal, pjManager, sqlDeadline);

        if (pjID != -1) {
            // PP1b.	[同步新增] 一筆 pjmbTable 資料 (pjID, mbID, pjmbRole, pjmbIsManager)
            //				return 1
            if(-1 != db.createPJMB(pjID, mbID, "Project Manager", 0, 1))
                return false;
            else {
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
			int existed = db.isActive(pjID, mbID);
			return false;
		} catch(SQLException e){}
		
		String pjmbRole = "";
		int pjmbIsActive = 0;
		int pjmbIsManager = 0;
		int result = db.createPJMB(pjID, mbID, pjmbRole, pjmbIsActive, pjmbIsManager);
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
			Member member;
			member = db.getMemberAsObject(mbID);
			
			String pjmbRole="";
			int pjmbIsActive=0;
			try{
				pjmbRole = db.getPJMB(pjID, mbID);
				pjmbIsActive = db.isActive(pjID, mbID);
                if(pjmbIsActive==1) {
                    ProjectMember temp = new ProjectMember(pjID, mbID, member.mbName, member.mbEmail, pjmbRole, (db.isManager(pjID, mbID) == 1), true);
                    list.add(temp);
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
			Member member;
			member = db.getMemberAsObject(mbID);
			
			String pjmbRole="";
			int pjmbIsActive=0;
			try{
				pjmbRole = db.getPJMB(pjID, mbID);
			}catch(SQLException e){
				//e.printStackTrace();
				return null;
			}
			try{
				pjmbIsActive = db.isActive(pjID, mbID);
			}catch(SQLException e){
				//e.printStackTrace();
				return null;
			}
			if(pjmbIsActive==0) {
                ProjectMember temp = new ProjectMember(pjID, mbID, member.mbName, member.mbEmail, pjmbRole, false, false);
                list.add(temp);
            }
		}
		return list;
	}
	
	//adtional method for invitation list
	public ArrayList<Project> getInvitationList(int mbID)
	{
        ArrayList<Integer> pjIDlist = null;
        ArrayList<Project> pjlist = null;
        try {
            pjIDlist = db.getPjIdList(mbID);
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

	
	// public static void main(String[] args) {
		
		// System.out.println("it all begins here.");
		
		// // -----------Connect to DB----------
		// //DbConnector db = new DbConnector("localhost:3306/pim", "root", "root");
		// db = new DbConnector("localhost:3306/pim", "root", "root");
		// db.getConnection();
		// // -----------DB conntected----------
		
		
		
		
		
	    
	    
		
		
		
		
		
		
		
// /////////////////////////////////////////////////////////////////////////////////////
// ////	Test By MZ
// /////////////////////////////////////////////////////////////////////////////////////
		
		// // Test Login ---------- success
		// /*
		// MB a;
		// a = login("email@61.com", "pw61");
		
		// if(a==null)
			// System.out.println("LOGIN FAILED");
		// else
			// System.out.println("memberID: " + a.getMbID());
		// */
		

		// //---------- Test Forget Password ---------- success
	    // /*
	    // String tempEmail = "kindamark@gmail.com";
	    // forget_password(tempEmail);
		// */
		
		
		// // Test Create Member ---------- success
		// /*
		// int tempID;
		// tempID = register("kindamark@gmail.com", "aaa", "userName");
		// if(tempID == -1)
			// System.out.println("Member Existed");
		// else
			// System.out.println("New member id is " + tempID);
		// */
		
		// //---------- Get Member Setting ---------- success
		// /*
		// MB mba;
		// mba = get_member_setting(109);
		// System.out.println("Email: " + mba.mbEmail);
		// System.out.println("MBID: " + mba.getMbID());
		// System.out.println("LastModified: " + mba.getMbLastModified());
		// System.out.println("Name: " + mba.mbName);
		// System.out.println("Password: " + mba.getMbPassword());
		// */
		
		
		
		// //---------- Update Member Setting ---------- success
		// /*
		// int tempmbID=109;
		// MB mba;
		// mba = get_member_setting(tempmbID);
		// System.out.println("Email: " + mba.mbEmail);
		// System.out.println("MBID: " + mba.getMbID());
		// System.out.println("LastModified: " + mba.getMbLastModified());
		// System.out.println("Name: " + mba.mbName);
		// System.out.println("Password: " + mba.getMbPassword());
		
		// update_member_setting(tempmbID, "newkindamark@aaa.com", "BBB", "MZ");
		
		// mba = get_member_setting(tempmbID);
		// System.out.println("After updating");
		// System.out.println("Email: " + mba.mbEmail);
		// System.out.println("MBID: " + mba.getMbID());
		// System.out.println("LastModified: " + mba.getMbLastModified());
		// System.out.println("Name: " + mba.mbName);
		// System.out.println("Password: " + mba.getMbPassword());
		// */
		
		
		// //---------- Get Project List---------- success
		// /*int tempmbid=65;
		// ArrayList<PJ> pjlist = get_project_List(tempmbid);
		// for(int i=0;i<pjlist.size();i++)
			// System.out.println(pjlist.get(i).getPjGoal());
		// */

		// //---------- Get Project Setting---------- success
		// /*int temppjID=610;
	    // PJ project = get_project_setting(temppjID);
	    // System.out.println(project.getPjGoal());*/
		
	    
		// //---------- Create New Project ---------- success
		// /*
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY,17);
		// cal.set(Calendar.MINUTE,30);
		// cal.set(Calendar.SECOND,0);
		// cal.set(Calendar.MILLISECOND,0);
		// java.util.Date temppjDeadline = cal.getTime();
		
		// int tempmbID=109;
		// String temppjName="King";
		// String temppjGoal="Create a king soft";
		
		// int temppjID =create_new_project(tempmbID, temppjName, temppjGoal, temppjDeadline);
		
		// if(temppjID==-1)
			// System.out.println("Create Project Failed");
		// else
			// System.out.println("Project ID: " + temppjID);
		// */
		
		
		// //---------- Update Project ---------- success
		// /*
		// int temppjID=610;
		// int tempmbID=109;
		// String temppjName="Kings";
		// String temppjGoal="Create Kings Soft";
		
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY,15);
		// cal.set(Calendar.MINUTE,30);
		// cal.set(Calendar.SECOND,0);
		// cal.set(Calendar.MILLISECOND,0);
		// java.util.Date temppjDeadline = cal.getTime();
		// int newPjManagerID=61;
		
		// boolean result = update_project_setting(temppjID, tempmbID, temppjName, temppjGoal, temppjDeadline, newPjManagerID);
		
		// if(result==true)
			// System.out.println("Update Success");
		// else
			// System.out.println("Update Failed");
		// */
		
		// //---------- Find Member with Email ---------- success
		// /*
		// Member MZ;
		// MZ = find_member_with_email("kindamark@aaaa.com");
		// System.out.println("Email: " + MZ.mbEmail);
		// System.out.println("MbID: " + MZ.getMbID());
		// System.out.println("MbName: " + MZ.mbName);
		// */
		
		
		
		// //---------- Invite User  ---------- success
		// /*
		// int tempmbID = 69;
		// int temppjID = 608;
		// boolean result = invite(tempmbID, temppjID);
		// if( result == true)
			// System.out.println("Invite MB Correctly");
		// else
			// System.out.println("Invite MB Failed");
		// */
		
		
		// //---------- respond to invitation  ---------- 
		// /*
		// int tempmbID = 69;
		// int temppjID = 608;
		// boolean accept = true;
		// boolean result = respond_to_invitation(tempmbID, temppjID, accept);
		// if (result == false)
			// System.out.println("respond failed");
		// else
			// System.out.println("respond correctly");
		// */
		
		

		// //---------- Get timeline  ---------- success
		// /*int temppjID=610;
		// ArrayList<MMAbstract> list;
		// list = get_timeline(temppjID);
		
		// for(int i=0;i<list.size();i++)
		// {
			// System.out.println(list.get(i).getObjective());
		// }*/
			
		
		
		// //---------- create new MM ---------- success
		// /*Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY,17);
		// cal.set(Calendar.MINUTE,30);
		// cal.set(Calendar.SECOND,0);
		// cal.set(Calendar.MILLISECOND,0);
		// java.util.Date tempdateTime = cal.getTime();
		
		// Calendar tempDeadline = new GregorianCalendar(2015 ,9 , 26); // yyyy, M, dd
		// java.sql.Date tempsqlDeadline = new java.sql.Date(utilDeadline.getTimeInMillis());
		
		// PAR par1 = new PAR("MZ", "MZ@mmm.com", "X", "Yes");
		// PAR par2 = new PAR("MZS", "MZS@mmm.com", "XXX", "No");
		
		
		// ACT act1 = new ACT(1 , "Build MM", "MZ", tempsqlDeadline, "Ongoing", "Remark1");
		// ACT act2 = new ACT(2 , "Build MM again", "MZS", tempsqlDeadline, "Done", "Remark2");
		
		
		// String location = "CSIEBuilding";
		// String facilitator = "Me";
		// String recorder = "MeAgain";
		// String objective = "To write an MM, and Test if the timeline usable";
		// ArrayList<PAR> parList = new ArrayList<PAR>();
		// String agenda = "Try to build create MM";
		// String issue = "MM building";
		// ArrayList<ACT> actList = new ArrayList<ACT>();
		// int temppjID=610;
		// int tempmmID=111;
		
		
		// parList.add(par1);
		// parList.add(par2);
		// actList.add(act1);
		// actList.add(act2);
		
		// Timestamp timestamp = new Timestamp(tempdateTime.getTime());
		
		// MM tempmm= new MM(tempdateTime, location, facilitator, recorder, objective, parList, agenda, issue, actList, temppjID, tempmmID, timestamp);
		
		
		// MMContent mmc = new MMContent(tempdateTime, location, facilitator, recorder, objective, parList, agenda, issue, actList);

		// create_new_MM(temppjID, mmc);*/
		
		
		
		
		// //---------- Update MM  ---------- success
		// /*Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY,17);
		// cal.set(Calendar.MINUTE,30);
		// cal.set(Calendar.SECOND,0);
		// cal.set(Calendar.MILLISECOND,0);
		// java.util.Date tempdateTime = cal.getTime();
		
		// Calendar tempDeadline = new GregorianCalendar(2015 ,9 , 26); // yyyy, M, dd
		// java.sql.Date tempsqlDeadline = new java.sql.Date(utilDeadline.getTimeInMillis());
		
		// PAR par1 = new PAR("MZ", "MZ@mmm.com", "X", "Yes");
		// PAR par2 = new PAR("MZS", "MZS@mmm.com", "XXX", "No");
		
		
		// ACT act1 = new ACT(1 , "Build MM", "MZ", tempsqlDeadline, "Ongoing", "Remark1");
		// ACT act2 = new ACT(2 , "Build MM again", "MZS", tempsqlDeadline, "Done", "Remark2");
		
		
		// String location = "CSIEBuilding B1";
		// String facilitator = "MeMZ!!!";
		// String recorder = "MeAgain";
		// String objective = "To write an MM";
		// ArrayList<PAR> parList = new ArrayList<PAR>();
		// String agenda = "Try to build create MM";
		// String issue = "MM building";
		// ArrayList<ACT> actList = new ArrayList<ACT>();
		// int temppjID=610;
		// int tempmmID=5;
		
		// parList.add(par1);
		// parList.add(par2);
		// actList.add(act1);
		// actList.add(act2);
		
		// MMContent mmc3 = new MMContent(tempdateTime, location, facilitator, recorder, objective, parList, agenda, issue, actList);
		// boolean result = update_old_MM(tempmmID, mmc3);
		
		// MMContent mmc2 = read_MM(tempmmID);
		// if(mmc2==null)
			// System.out.println("Failed");
			
		// System.out.println(mmc2.meetingTime);
		// System.out.println(mmc2.location);
		// System.out.println(mmc2.facilitator);
		// System.out.println(mmc2);
		// */
		
		
		// //---------- Get MM  ---------- success
		// /*int tempmmID=5;
		// MMContent mmc2 = read_MM(tempmmID);
		// if(mmc2==null)
			// System.out.println("Failed");
			
		// System.out.println(mmc2.meetingTime);
		// System.out.println(mmc2.location);
		// System.out.println(mmc2);*/
		
		
		
		
		
		// //---------- Get Project MB List ---------- success
		// /*
		// int temppjID=606;
		// ArrayList<PJMB> list;
		// list = get_project_member_list(temppjID);
		// if(list==null)
			// System.out.println("Project not found");
		// else
		// {
			// for (int i = 0; i <= list.size()-1; i++){
				// System.out.println("Member " + i + ":");
				// System.out.println("pjID: " + list.get(i).getPjID());
				// System.out.println("mbID: " + list.get(i).getMbID());
				// System.out.println("mbName: " + list.get(i).mbName);
				// System.out.println("Email: " + list.get(i).mbEmail);
				// System.out.println("pjmbRole: " + list.get(i).getPjmbRole());
			// }
		// }
		// */
		
		
		
	// /////////////////////////////////////////////////////////////////////////////////////
	// ////Test By MZ over
	// /////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		
		
		
		
		
		
		
		// /*
		// // 這三個變數是為了快速變更使用者 identity 所設置的。 for test purpose
		// String tempEmail = "88@88.com";
		// String tempPassword = "5488";
		// String tempName = "爸爸";
		
		// // items from mbTable
		// int mbID = -3;
		// String mbEmail = "";
		// String mbPassword = "";
		// String mbName = "";
		// Timestamp mbLastModified = null;
		
		// // items from pjTable
		// int pjID = 105;
		// String pjName = "劉喬安";
		// String pjGoal = "下海下海愛";
		// String pjManager = "爸爸";
			// //以下這兩行中的 utilDeadline 與 pjDeadline 是專門為了將 java.util.Date 轉型為 java.sql.Date 用的
			// Calendar utilDeadline = new GregorianCalendar(2018, 3, 19); // yyyy, M, dd
			// java.sql.Date pjDeadline = new java.sql.Date(utilDeadline.getTimeInMillis());
		// //Date pjDeadline = null;
		// Timestamp pjLastModified = null;
		// // items for matching old and new manager during project update
		// String oldManager = "";
		// String newManager = "";
		// int oldManagerID = -1;
		// int newManagerID = -1;
		
		// // items from pjmbTable
		// String pjmbRole = "";
		// int pjmbIsManager = 0;
		
		// // items from mmTable
		// int mmID = -1;
		// Blob mmContent = null;
		// Timestamp mmLastModified = null;
		// */
		
// /////////////////////////////////////////////////////////////////////////////////////
// //// TEST-Part[1]:	先測 Member Functions (因為 Project Functions 需要與 PJMB 同步，因此晚點再做)
// //// 				這部分所有的 test 的編號都以 "M" 開頭 (例如: M1, M2)
// ////				Test 編號與名稱後面 (或上方) 有註解： "測試OK" 代表該 function call 已確定可以 work。
// /////////////////////////////////////////////////////////////////////////////////////
// /*		
		// // M1. Create 一個  Member			//測試OK
		// //		return mbID
		// mbID = db.createMember(tempEmail, tempPassword, tempName);
		// System.out.println("@db.createMember() -> " + mbID);
// */		
// //------------------------------------------------------------------------------------------		
// /*
		// // M2 Login						//測試OK 
		// mbID = db.getMemberID(tempEmail, tempPassword);
		// System.out.println("@db.getMemberID() -> " + mbID);
// */
// //------------------------------------------------------------------------------------------
// /*	
		// // M3. Get Member Password		//測試OK 
		// String pw = db.getPassword("666006@yahoo.com");
		// System.out.println("@db.getPassword() -> " + pw);
// */		
// //------------------------------------------------------------------------------------------
		// // M4-a. Get Member As Object		// 這是我們今天在開會時討論的 function call， (原本的問題是無法回傳物件)
											// // 此問題現在應該算解決了。所以我最後還是會以物件形式將資料回傳給你，不需要用 ResultSet)
											// // 因此 M4-b 應該就不需要了

		// /*MB mb = db.getMemberAsObject(mbID);
		// mbID = mb.getMbID();
		// System.out.println(mbID);*/
// //------------------------------------------------------------------------------------------	
// /*
		// // M4-b. Get Member As ResultSet		//測試OK 
		// ResultSet rs2 = db.getMemberAsResultSet(mbID);
// //		int mbID = 0;
// //		String mbEmail = "";
// //		String mbPassword = "";
// //		String mbName = "";
// //		Timestamp mbLastModified = null;
		// try {
			// while(rs2.next()) {
				// mbID = rs2.getInt("mbID");
				// mbEmail = rs2.getString("mbEmail");
				// mbPassword = rs2.getString("mbPassword");
				// mbName = rs2.getString("mbName");
				// mbLastModified = rs2.getTimestamp("mbLastModified");
			// }
		// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		// }
		// MB mb = new MB(mbID, mbEmail, mbPassword, mbName, mbLastModified);
				
// //		以下10行是測試 MB 內容專用，實際程式碼中不需要:
		// mbID = mb.getMbID();
		// mbEmail = mb.mbEmail;
		// mbPassword = mb.getMbPassword();
		// mbName = mb.mbName;
		// mbLastModified = mb.getMbLastModified();
		// System.out.println("mbID = " + mbID);
		// System.out.println("mbEmail = " + mbEmail);
		// System.out.println("mbPassword = " + mbPassword);
		// System.out.println("mbName = " + mbName);
		// System.out.println("mbLastModified = " + mbLastModified);
// */
// //------------------------------------------------------------------------------------------		
// /*
		// // M5. Update Member		//測試OK 
		// int updateResult = db.updateMember(mbID, "tammy@lovelace.com", "foxy", "熱被");
		// System.out.println("已更新 " + updateResult + " 筆會員資料。");
// */
// //------------------------------------------------------------------------------------------
// /*
		// // M6. Get Member Record Again After Update
		// rs2 = db.getMemberAsResultSet(mbID);
// //		mbID = 0;
// //		mbEmail = "";
// //		mbPassword = "";
// //		mbName = "";
// //		mbLastModified = null;
		// try {
			// while(rs2.next()) {
				// mbID = rs2.getInt("mbID");
				// mbEmail = rs2.getString("mbEmail");
				// mbPassword = rs2.getString("mbPassword");
				// mbName = rs2.getString("mbName");
				// mbLastModified = rs2.getTimestamp("mbLastModified");
			// }
		// } catch (SQLException e) {
			// e.printStackTrace();
		// }
		// mb = new MB(mbID, mbEmail, mbPassword, mbName, mbLastModified);
		
		// mbID = mb.getMbID();
		// mbEmail = mb.mbEmail;
		// mbPassword = mb.getMbPassword();
		// mbName = mb.mbName;
		// mbLastModified = mb.getMbLastModified();
		// System.out.println("mbID = " + mbID);
		// System.out.println("mbEmail = " + mbEmail);
		// System.out.println("mbPassword = " + mbPassword);
		// System.out.println("mbName = " + mbName);
		// System.out.println("mbLastModified = " + mbLastModified);
// */		
// //------------------------------------------------------------------------------------------
		
// /////////////////////////////////////////////////////////////////////////////////////
// ////TEST PART [2]:	測試 Project Member & PJMB Functions (PP)
// ////這部分所有的 test 的編號都以 "PP" 開頭 (例如: PP0, PP1a, PP2, ...)
// ////Test 編號與名稱後面 (或上方)有註解： 	"已測試成功"	 代表該 function call 已確定可以 work。
// /////////////////////////////////////////////////////////////////////////////////////

// //------------------------------------------------------------------------------------------		
// /*		
		// // PP0		已測試成功
		// // PP0.		Login: [取得] 某人的 project list。
		// //				return List<pjID>
			
		// List<Integer> pjidList = db.getPJidList(mbID);
		// for (int i = 0; i <= pjidList.size()-1; i++){
			// System.out.println(pjidList.get(i));
		// }
// */
// //------------------------------------------------------------------------------------------
// /*
		// // PP1a		已測試成功
		// // PP1a.	[新增] 一筆 pjTable 資料 (pjName, pjGoal, pjManager, pjDeadline, pjLastModified)
		// //				return pjID
		// //GregorianCalendar deadline = new GregorianCalendar(2015, 9, 26);
		// Calendar utilDeadline = new GregorianCalendar(2015, 9, 26); // yyyy, M, dd
		// java.sql.Date sqlDeadline = new java.sql.Date(utilDeadline.getTimeInMillis());
					// // 不要使用 Date Constructor (如下):	
					// // Date deadline = new Date(2015,9,26);
					// // Date Constructor 已經被 deprecate 了。	
		
		// pjID = db.createProject("上帝有約", "Invited by God", tempName, sqlDeadline);
		
		// if (pjID != -1) {
			// // PP1b.	[同步新增] 一筆 pjmbTable 資料 (pjID, mbID, pjmbRole, pjmbIsManager)
			// //				return 1
			// int result = db.createPJMB(pjID, mbID, "Project Manager", 1);
			// System.out.println("pjmbTable [同步作業] '成功' 新增 " + result + " 一個 專案。  pjID = " + pjID);
		// }
// */
// //------------------------------------------------------------------------------------------		
// /*
		// // PP2 	已測試成功
		// // PP2.	Project Setting: [取得] 一筆 pjTable 資料 (pjID)
		// //			return ResultSet
		// //		然後根據 ResultSet 建立 PJ 物件
		// ResultSet rs = db.getProjectAsResultSet(617);
		// //ResultSet rs = db.getProjectAsResultSet(pjID);
		// int tempNum = 0;
		// try {
			// while(rs.next()) {
				// pjID = rs.getInt("pjID");
				// pjName = rs.getString("pjName");
				// pjGoal = rs.getString("pjGoal");
				// pjManager = rs.getString("pjManager");
				// pjDeadline = rs.getDate("pjDeadline");
				// pjLastModified = rs.getTimestamp("pjLastModified");
			// }
			
			// PJ pj = new PJ(pjID, pjName, pjGoal, pjManager, pjDeadline, pjLastModified);
			
			// pjID = pj.getPjID();
			// pjName = pj.getPjName();
			// pjManager = pj.getPjManager();
			// pjDeadline = pj.getPjDeadline();
			// pjLastModified = pj.getPjLastModified();
			// System.out.println("pjID = " + pjID);
			// System.out.println("pjName = " + pjName);
			// System.out.println("pjGoal = " + pjGoal);
			// System.out.println("pjManager = " + pjManager);
			// System.out.println("pjLastModified = " + pjLastModified);
		
			// tempNum += 1;
			
		// } catch (SQLException e) {
			// e.printStackTrace();
		// }
		
		// if (tempNum == 1) {
			// System.out.println("已成功回傳 PJ 物件的 ResultSet 並成功建立 PJ 物件。");
		// }
		// else {
			// System.out.println("未成功回傳 PJ 物件的 ResultSet 並成功建立 PJ 物件。");
		// }
// */

// //------------------------------------------------------------------------------------------

// //// 從這裡開始: 處理 PP3a：		// 這一段還沒 debug 完
// /*			
		// // PP3a.	[更新] 一筆 pjTable 資料 (pjID, pjName, pjGoal, pjManager, pjDeadline, pjLastModified) 
		// // 				return 1
		// ////// Step 1. 先把 update 之前的 pjManager 的名字存起來。
		// oldManager = pjManager;
		// oldManagerID = mbID;
		// pjManager = "舅舅";
		
		// // update Project
		// int result = db.updateProject(pjID, pjName, pjGoal, pjManager, pjDeadline);		
		// // (假設 pjManager 會換人)
		// // 檢查 oldManager 與 新的 manager 是否為同一個人
		// newManager = db.getPjManager(pjID);
		// boolean matchResult = db.matchManager(oldManager, newManager);
		// if (matchResult == false) {
			// // PP3b.	IF (pjManager 有修改)：
			// //				[同步更新] 一筆 pjTable 資料 (pjID, mbID, pjmbRole, pjmbIsManfager)
			// //					return 1
			// newManagerID = db.getMemberIDbyName(newManager);
			// db.updateManager(pjID, oldManagerID, newManagerID, newManager);
		// }
// */
		

		// // 結束 DB 連線
		// db.endConnection();
		
	// }
	
}

