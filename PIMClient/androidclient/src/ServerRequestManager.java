
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import com.example.androidclient.*;


public class ServerRequestManager extends Thread implements Serializable
{
	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	int id;
	
	public ServerRequestManager(Socket socket, int id)
	{
		this.socket = socket;
		this.id = id;
		try
		{
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
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
		if(reqtype.equals("logIn"))
		{
			//Unpack Parameters in Request, call DbConnector
			//DbConnector.createProject() ;
			
		}
		else if(reqtype.equals("createAccount"))
		{
			
		}
		else if(reqtype.equals("getMemberProjectList"))
		{
			
		}
		else if(reqtype.equals("getMemberProjectList"))
		{
			
		}
		else if(reqtype.equals("logIn"))
		{
			
		}
		else if(reqtype.equals("logIn"))
		{
			
		}
		else if(reqtype.equals("logIn"))
		{
			
		}
		else if(reqtype.equals("logIn"))
		{
			
		}
		else if(reqtype.equals("logIn"))
		{
			
		}
		else if(reqtype.equals("logIn"))
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
		if(userPassword.equals(DBConnsetor.getPassword(userEmail)))
		{
			int memberid = DbConnector.getMemberID(userEmail, userPassword) ;
			Member member = DBConncetor.getMemberAsObject(int memberid) ;
			return member ;
		}
		else return null ;//login failed
		
	}
	private int createAccount(String userEmail, String userPassword, String userName)
	{
		return DbConnector.createMember(userEmail, userPassword, userName) ;
	}
	private List<Project>getMemberProjectList(Member member)
	{
		//resultset to object?
	}
	
	private List<MeetingMinutes>getMeetingMinutesList(Project project)
	{	
		List<MeetingMinutes> mmlist ;
		List<Integer> mmidlist = DbConnector.getMMidList(project.getPjID()) ;
		for(int mmid:mmidlist)
		{
			mmlist.add(DbConnector.getMMcontent(mmid)) ;
		}
		
		return mmlist ;
	}

	private List<Project>getMemberInvitationList(Member member)
	{
		//not done yet?
	}

	private int respondInvitation(Member member, Project project)
	{
		//not done yet?
	}

	private int createProject(Project newProject)
	{
		String pjname = newProject.getPjName() ;
		String pjgoal = newProject.getPjGoal() ;
		String pjmanager = newProject.getPjManager() ;
		Date pjdeadline = newProject.getPjDeadline() ;
		int success = DbConnector.createProject(pjname, pjgoal, pjmanager, pjdeadline) ;
		return success ;
	}
	private Boolean createProjectMemberList(List<String>)
	{
		//Not sure yet
	}
	private Boolean createMeetingMinutes(MeetingMinutes newMeetingMinutes)
	{
		MeetingMinutesContent mmcontent = newMeetingMinutes.getContent() ;
		int pjid = newMeetingMinutes.getpjid() ;
		DbConnector.createMM(pjid, mmcontent) ;
		
	}
	private int modifyProject(Project project)
	{
		String pjname = project.getPjName() ;
		String pjgoal = project.getPjGoal() ;
		String pjmanager = project.getPjManager() ;
		Date pjdeadline = project.getPjDeadline() ;
		int success = DbConnector.updateProject(pjname, pjgoal, pjmanager, pjdeadline) ;
		return success ;
		
	}
	private Boolean modifyMeetingMinutes(MeetingMinutes newMeetingMinutes)
	{
		MeetingMinutesContent mmcontent = newMeetingMinutes.getContent() ;
		int pjid = newMeetingMinutes.getmmid() ;
		DbConnector.updateMM(pjid, mmcontent) ;
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
			//Object obj = input.readObject();
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
			System.out.println("received");
			
			//output.writeObject(2015);
			output.writeObject(response) ;
			//output.writeBytes(returnObject + received + "\n");
			System.out.println("close id:" + id + "\n");
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