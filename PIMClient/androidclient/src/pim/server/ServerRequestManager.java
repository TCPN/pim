package pim.server;
import pim.* ;

import java.io.*;
import java.net.Socket;
import java.util.*;

import pim_data.MeetingMinutes;
import pim_data.MeetingMinutesAbstract;
import pim_data.MeetingMinutesContent;


public class ServerRequestManager extends Thread implements Serializable
{
	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	int id;
	Date connectTime;
	Date verifiedTime;
	DBManager dbcn;
	
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
			dbcn = new DBManager();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	private Object handleRequest(Request request)
	{
		/*
		String aMethod = "myMethod";
		Object iClass = pim.server.ServerRequestManager.newInstance();
		// get the method
		Method thisMethod = this.getDeclaredMethod(aMethod, params);
		// call the method
		thisMethod.invoke(iClass, paramsObj);*/
		try{
			String reqName = request.getName() ;
			Object params[] = request.getParameterList() ;
			if(reqName.equals("test"))
			{/*
				System.out.println("pa mbID: " + request.findValue("mbID").toString());
				System.out.println("pa pjName: " + request.findValue("pjName").toString());
				System.out.println("pa pjGoal: " + request.findValue("pjGoal").toString());
				System.out.println("pa pjDeadline: " + request.findValue("pjDeadline").toString());
				Project pj = (Project)request.findValue("pjObject");
				System.out.println("pjObject: " + pj.getPjID() + " " + pj.getPjName() + " " + pj.getPjGoal() + " " + pj.getPjManager() + " " + pj.getPjDeadline());
				boolean success = pj.getPjManager().startsWith("Mr. ");
				return success;*/
                return "this is Test Request";
			}
			else if(reqName.equals("logIn"))
			{

				return dbcn.login(
						(String)params[0], 
						(String)params[1]
								) ;
				
			}
			else if(reqName.equals("forgetPassword")) 
			{
				return dbcn.forget_password(
						(String)params[0]
								) ;
			}
			else if(reqName.equals("createAccount"))
			{
				return dbcn.register(
						(String)params[0], 
						(String)params[1], 
						(String)params[2]
								) ;
			}
			else if(reqName.equals("getProjectList"))
			{
				return dbcn.get_project_List(
						(Integer)params[0]
								);
			}
			else if(reqName.equals("getInvitingProjectList"))
			{
				return dbcn.get_invitation_project_List(
						(Integer)params[0]
							);
				
			}
			else if(reqName.equals("getMeetingMinutesList"))
			{
				ArrayList<MeetingMinutesAbstract> mmablist = dbcn.get_timeline(
						(Integer)params[0]
								) ;
				ArrayList<MeetingMinutes> mmlist = new ArrayList<MeetingMinutes>() ;
				for(MeetingMinutesAbstract mmab : mmablist)
				{
					mmlist.add(new MeetingMinutes(mmab)) ;
				}

				return mmlist ;
			}
			else if(reqName.equals("respondInvitation"))
			{
				return dbcn.respond_to_invitation(
						(Integer)params[0], 
						(Integer)params[1], 
						(Boolean)params[2]
								) ;
			}
			else if(reqName.equals("createProject"))
			{
				return dbcn.create_new_project(
						(Integer)params[0], 
						(String)params[1], 
						(String)params[2], 
						(Date)params[3],
                        (ArrayList<String>)params[4]
								) ;
			}
			else if(reqName.equals("getMemberList"))
			{
				return dbcn.get_active_project_member_list(
						(Integer)params[0]
								) ;
			}
			else if(reqName.equals("getInvitingMemberList"))
			{
				return dbcn.get_inactive_project_member_list(
						(Integer)params[0]
								) ;
			}
			else if(reqName.equals("createMeetingMinutes"))
			{
				return dbcn.create_new_MM(
						(Integer)params[0],
						(MeetingMinutesContent)params[1]
								);
			}
			else if(reqName.equals("modifyProject"))
			{
				return dbcn.update_project_setting(
						(Integer)params[0], 
						(Integer)params[1], 
						(String)params[2], 
						(String)params[3], 
						(Date)params[4], 
						(Integer)params[5]
							) ;
			}
			else if(reqName.equals("modifyMeetingMinutes"))
			{
				return dbcn.update_old_MM(
						(Integer)params[0], 
						(MeetingMinutesContent)params[1]
								) ;
			}
			//else if...for all DbConnector API
			else
			{
				//for error
				return new Exception("Request Name No Match.");
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
			return new Exception("Caught Error when Server Handle Request: "+ e.toString(), e);
		}
	}
/*
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
		int pjid = newMeetingMinutes.getPJId() ;
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
		int pjid = newMeetingMinutes.getMMId() ;
        dbcn.updateMM(pjid, mmcontent) ;
        return  false;
	}
	*/
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
            System.out.println(req);
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
			System.out.print("SRM(id:" + id + ") ==> ");
			System.out.println(e);
			this.socket = null;
        }
    }
}