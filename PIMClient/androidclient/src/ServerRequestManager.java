
import java.io.*;
import java.net.*;
import java.util.*;
import pim.*;


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

		Object iClass = ServerRequestManager.newInstance();
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