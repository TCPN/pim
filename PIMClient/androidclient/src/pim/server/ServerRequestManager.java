package pim.server;
import pim.* ;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

import pim_data.MeetingMinutes;
import pim_data.MeetingMinutesAbstract;
import pim_data.MeetingMinutesContent;
import pim_data.Member;


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
	}
	
	private Object handleRequest(Request request)
	{
		try{
			String reqName = request.getName() ;
			Object params[] = request.getParameterList() ;
			if(reqName.equals("test"))
			{
//				System.out.println("pa mbID: " + request.findValue("mbID").toString());
//				System.out.println("pa pjName: " + request.findValue("pjName").toString());
//				System.out.println("pa pjGoal: " + request.findValue("pjGoal").toString());
//				System.out.println("pa pjDeadline: " + request.findValue("pjDeadline").toString());
//				Project pj = (Project)request.findValue("pjObject");
//				System.out.println("pjObject: " + pj.getPjID() + " " + pj.getPjName() + " " + pj.getPjGoal() + " " + pj.getPjManager() + " " + pj.getPjDeadline());
//				boolean success = pj.getPjManager().startsWith("Mr. ");
//				return success;
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
                    //jump over this
					//mmlist.add(new MeetingMinutes(mmab)) ;
                    mmlist.add(dbcn.read_MM(mmab.getMMId())) ;
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
            else if(reqName.equals("modifyMemberPassword"))
            {
                return dbcn.modifyMemberPassword(
                        (int)params[0],
                        (String)params[1]
                ) ;
            }
            else if(reqName.equals("modifyMemberName"))
            {
                return dbcn.modifyMemberName(
                        (int)params[0],
                        (String)params[1]
                ) ;
            }
			//else if...for all DbConnector API
			else
			{
				//for error
				return new Exception("Request Name modifyMemberName Match.");
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
			return new Exception("Caught Error when Server Handle Request: "+ e.toString(), e);
		}
	}

    public void run()
	{
        System.out.println(new Date() + "\t" + "An object message handler for connect: "+id+" start...");
        try {
            try {
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("build Streams for connect:" + id + " failed, " + e);
            }
            try {
                dbcn = new DBManager();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("build connection to database for connect:" + id + " failed, " + e);
                // TODO : write error to client
            }

            try
            {
                Request req = (Request) input.readObject() ;
                System.out.println(new Date() +"\t" + "Request received.");
                System.out.println(req);
                Object response = handleRequest(req) ;
                output.writeObject(response) ;
                System.out.println(new Date() +"\t" + "Response sent.");
            }
            catch(Exception e)
            {
                System.out.print("handle request of connect:" + id + " failed, " + e);
                // TODO : write error to client
            }
        } catch(Exception e){

        } finally {
            close();
        }
        this.socket = null;
        System.out.println(new Date() +"\t" + "Thread for connect:" + id + " end.");
    }

    private void close() {
        try{
            System.out.println("closing connect:" + id + "...");
            this.socket.close();
        }catch (Exception e){
            System.out.print("close connect:" + id + " failed, " + e);
        }
    }
}