package pim.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pim.MeetingMinutes;
import pim.MeetingMinutesContent;
import pim.Member;
import pim.Project;
import pim.Request;


public class ServerRequestManager extends Thread implements Serializable
{
	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	int id;
	Date connectTime;
	Date verifiedTime;
	DBConnector dbcn;
	
	public ServerRequestManager(Socket socket, int id, Date connectTime)
	{
		this.socket = socket;
		this.id = id;
		this.connectTime = connectTime;
		this.verifiedTime = new Date();
		try
		{
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			dbcn = new DBConnector("localhost:3306/pim", "root", "cliurcp");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	private Object handleRequest(Request request) throws Exception
	{
		/*
		  
		 
		String aMethod = "myMethod";

		Object iClass = pim.server.ServerRequestManager.newInstance();
		// get the method
		Method thisMethod = this.getDeclaredMethod(aMethod, params);
		// call the method
		thisMethod.invoke(iClass, paramsObj);*/
		String reqtype = request.getName() ;
		Object response = null ;
		if(reqtype.equals("login"))
		{
			//Unpack Parameters in Request, call DbConnector
			//DbConnector.createProject() ;
			
		}
		else if(reqtype.equals("test"))
		{
            System.out.println("pa mbID: " + request.findValue("mbID").toString());
            System.out.println("pa pjName: " + request.findValue("pjName").toString());
            System.out.println("pa pjGoal: " + request.findValue("pjGoal").toString());
            System.out.println("pa pjDeadline: " + request.findValue("pjDeadline").toString());
            Project pj = (Project)request.findValue("pjObject");
            System.out.println("pjObject: " + pj.getPjID() + " " + pj.getPjName() + " " + pj.getPjGoal() + " " + pj.getPjManager() + " " + pj.getPjDeadline());
			boolean success = pj.getPjManager().startsWith("Mr. ");
            return success;
		}
		else if(reqtype.equals("forget_password"))
		{
			
		}
		else if(reqtype.equals("register"))
		{
			
		}
		else if(reqtype.equals("get_member_setting"))
		{
			
		}
		else if(reqtype.equals("update_member_setting"))
		{
			
		}
		else if(reqtype.equals("get_project_List"))
		{
			
		}
		else if(reqtype.equals("get_project_setting"))
		{
			
		}
		else if(reqtype.equals("create_new_project"))
		{
			
		}
		else if(reqtype.equals("update_project_setting"))
		{
			
		}
		else if(reqtype.equals("find_member_with_email"))
		{
			
		}
		else if(reqtype.equals("invite"))
		{
			
		}
		else if(reqtype.equals("respond_to_invitation"))
		{
			
		}
		else if(reqtype.equals("get_timeline"))
		{
			
		}
		else if(reqtype.equals("create_new_MM"))
		{
			
		}
		else if(reqtype.equals("update_old_MM"))
		{
			
		}
		else if(reqtype.equals("read_MM"))
		{
			
		}
		else if(reqtype.equals("get_project_member_list"))
		{
			
		}
        else if(reqtype.equals("logIn"))
        {
            System.out.println("userEmail: " + request.findValue("userEmail").toString());
            System.out.println("userPassword: " + request.findValue("userPassword").toString());
            return new Member(789,request.findValue("userEmail").toString(), "name");
        }
        else if(reqtype.equals("createAccount"))
        {
            System.out.println("userEmail: " + request.findValue("userEmail").toString());
            System.out.println("userPassword: " + request.findValue("userPassword").toString());
            return true;
        }
        else if(reqtype.equals("getMemberProjectList"))
        {
            Member member = (Member)request.findValue("member");
            System.out.println("member: " + member.toString() +" "+  member.mbEmail +" "+ member.mbName +" "+ member.getMbID());
            ArrayList<Project> ret = new ArrayList<>();
            ret.add(new Project(1111, "pj1", "goal1", 1101, "mgr1", new Date(111111111)));
            ret.add(new Project(2222, "pj2", "goal2", 2202, "mgr2", new Date(222222222)));
            ret.add(new Project(3333, "pj3", "goal3", 3303, "mgr3", new Date(333333333)));
            return ret;
        }
        else if(reqtype.equals("getMeetingMinutesList"))
        {
            Project project = (Project)request.findValue("project");
            System.out.println("project: " + project.getPjID()
                    +" "+project.getPjName()+" "+project.getPjGoal()+" "+project.getPjManager()+" "+project.getPjDeadline());
            ArrayList<MeetingMinutes> ret = new ArrayList<>();
            ret.add(new MeetingMinutes(project.getPjID()+1, project.getPjID(), new Date(199999999), null));
            ret.add(new MeetingMinutes(project.getPjID()+2, project.getPjID(), new Date(299999999), null));
            ret.add(new MeetingMinutes(project.getPjID()+4, project.getPjID(), new Date(499999999), null));
            return ret;
        }
        else if(reqtype.equals("getMemberInvitationList"))
        {
            Member member = (Member)request.findValue("member");
            System.out.println("member: " + member.toString() +" "+  member.mbEmail +" "+ member.mbName +" "+ member.getMbID());
            ArrayList<Project> ret = new ArrayList<>();
            ret.add(new Project(4444, "hello1", "goal1", 1101, "mgr1", new Date(111111111)));
            ret.add(new Project(5555, "world2", "goal2", 2202, "mgr2", new Date(222222222)));
            ret.add(new Project(6666, "Please3", "goal3", 3303, "mgr3", new Date(333333333)));
            return ret;
        }
        else if(reqtype.equals("respondInvitation"))
        {
            Member member = (Member)request.findValue("member");
            System.out.println("member: "+member.mbEmail +" "+ member.mbName +" "+ member.getMbID());
            Project project = (Project)request.findValue("project");
            System.out.println("project: " + project.getPjID()
                    +" "+project.getPjName()+" "+project.getPjGoal()+" "+project.getPjManager()+" "+project.getPjDeadline());
            System.out.println("accept: " + request.findValue("accept").toString());
            return true;
        }
        else if(reqtype.equals("createProject"))
        {
            Project project = (Project)request.findValue("newProject");
            System.out.println("newProject: " + project.getPjID()
                    +" "+project.getPjName()+" "+project.getPjGoal()+" "+project.getPjManager()+" "+project.getPjDeadline());
            return true;
        }
        else if(reqtype.equals("createProjectMemberList"))
        {
            ArrayList<String> email = (ArrayList<String>)request.findValue("email");
            System.out.println("email: ");
            for(int i = 0; i < email.size(); i ++)
            {
                System.out.println(i+" "+email.get(i));
            }
            return true;
        }
        else if(reqtype.equals("createMeetingMinutes"))
        {
            MeetingMinutes minutes = (MeetingMinutes)request.findValue("newMeetingMinutes");
            System.out.println("newMeetingMinutes: " + "mmID:" + minutes.getmmid()
                    + "pjID:" + minutes.getpjid() +" "+ minutes.getLastModifyTime()
                    +" "+minutes.content.objective+" "+minutes.content.recorder+" "+minutes.content.actList.get(1).action);
            return true;
        }
        else if(reqtype.equals("modifyProject"))
        {
            Project project = (Project)request.findValue("project");
            System.out.println("project: " + project.getPjID()
                    +" "+project.getPjName()+" "+project.getPjGoal()+" "+project.getPjManager()+" "+project.getPjDeadline());
            return true;
        }
        else if(reqtype.equals("modifyMeetingMinutes"))
        {
            MeetingMinutes minutes = (MeetingMinutes)request.findValue("newMeetingMinutes");
            System.out.println("newMeetingMinutes: " + "mmID:" + minutes.getmmid()
                    + "pjID:" + minutes.getpjid() +" "+ minutes.getLastModifyTime()
                    +" "+minutes.content.objective+" "+minutes.content.recorder+" "+minutes.content.actList.get(1).action);
            return true;
        }
		//else if...for all DbConnector API
		else
		{
			//for error
		}
		return response ;
	}
	
	private Member logIn(String userEmail, String userPassword)
	{
		if(userPassword.equals(dbcn.getPassword(userEmail)))
		{
			int memberid = dbcn.getMemberID(userEmail, userPassword) ;
			Member member = dbcn.getMemberAsObject(memberid) ;
			return member ;
		}
		else return null ;//login failed
		
	}
	private int createAccount(String userEmail, String userPassword, String userName)
	{
		return dbcn.createMember(userEmail, userPassword, userName) ;
	}
	private ArrayList<Project>getMemberProjectList(Member member)
	{
		//resultset to object?
        return null;
	}
	
	private ArrayList<MeetingMinutesContent>getMeetingMinutesList(Project project)
	{
        ArrayList<MeetingMinutesContent> mmlist = new ArrayList<MeetingMinutesContent>();
        ArrayList<Integer> mmidlist = dbcn.getMMidList(project.getPjID()) ;
		for(int mmid:mmidlist)
		{
			mmlist.add(dbcn.getMMcontent(mmid)) ;
		}
		
		return mmlist ;
	}

	private ArrayList<Project>getMemberInvitationList(Member member)
	{
		//not done yet?
        return null;
	}

	private int respondInvitation(Member member, Project project)
	{
		//not done yet?
        return 0;
	}

	private int createProject(Project newProject)
	{
		String pjname = newProject.getPjName() ;
		String pjgoal = newProject.getPjGoal() ;
		String pjmanager = newProject.getPjManager() ;
		Date pjdeadline = newProject.getPjDeadline() ;
		int success = dbcn.createProject(pjname, pjgoal, pjmanager, pjdeadline) ;
		return success ;
	}
	private Boolean createProjectMemberList(List<String> emailList)
	{
		//Not sure yet
        return false;
	}
	private Boolean createMeetingMinutes(MeetingMinutes newMeetingMinutes)
	{
		MeetingMinutesContent mmcontent = newMeetingMinutes.getContent() ;
		int pjid = newMeetingMinutes.getpjid() ;
        dbcn.createMM(pjid, mmcontent) ;
		return false;
	}
	private int modifyProject(Project project)
	{
        int pjid = project.getPjID();
		String pjname = project.getPjName() ;
		String pjgoal = project.getPjGoal() ;
		String pjmanager = project.getPjManager() ;
		java.sql.Date pjdeadline = (java.sql.Date) project.getPjDeadline();
		int success = dbcn.updateProject(pjid, pjname, pjgoal, pjmanager, pjdeadline) ;
		return success ;
		
	}
	private Boolean modifyMeetingMinutes(MeetingMinutes newMeetingMinutes)
	{
		MeetingMinutesContent mmcontent = newMeetingMinutes.getContent() ;
		int pjid = newMeetingMinutes.getmmid() ;
        dbcn.updateMM(pjid, mmcontent) ;
        return  false;
	}
    public void run()
	{
		try
		{
            // 
			byte[] line = new byte[4096];
			String received = "";
			String returnObject = "return_of_";
			Request r = null;
			Request req = (Request) input.readObject() ;
			Object response = handleRequest(req) ;
			/*
            while (true)
			{
                int len = input.read(line, 0, 4096);
				//System.out.println(line);
                if (len <= 0) {
                    break;
                }
				received += String.valueOf(line, 0, len);
            }
			*/
			//System.out.println(received + "\n");
			System.out.println(new Date() +"\tMsg received");
			
			output.writeObject(response) ;
			//output.writeBytes(returnObject + received + "\n");
			System.out.println(new Date() +"\tclose id:" + id + "\n");
			this.socket.close();
        }
		catch (Exception e)
		{
			System.out.print("id:" + id + " ==> ");
			System.out.println(e);
			this.socket = null;
        }
    }
}