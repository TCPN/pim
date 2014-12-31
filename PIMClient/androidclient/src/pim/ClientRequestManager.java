package pim;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;


public class ClientRequestManager implements /*API,*/ ClientAPI {
    final String validateString = "imvalidate";
    Socket socket = null;
	ObjectInputStream input;
	ObjectOutputStream output;
    String address;
    int port;

    public ClientRequestManager(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private void getConnection() throws Exception{
		if(this.socket != null && !this.socket.isClosed()) {
			this.socket.close();
		}
		this.socket = new Socket();
		this.socket.connect(new InetSocketAddress(address, port), 15000) ;
		this.output = new ObjectOutputStream(socket.getOutputStream());
        InputStream i = socket.getInputStream();
		this.input = new ObjectInputStream(i);
    }
  
	private Object sendRequest(Request request) throws Exception
	{
		System.out.println("ClientRequestManager:Start connect...");
		try
		{
			getConnection();
			System.out.println("Connected.");
			
			output.writeObject(request) ;
            socket.shutdownOutput();
			System.out.println("Request sent.");
			
			Object receivedObject = input.readObject() ;
			//System.out.println("Response received. Type:" + receivedObject.getClass().toString());
			
			socket.close();
            if(receivedObject!= null && receivedObject.getClass().equals(Exception.class))
                throw (Exception)receivedObject;

			return receivedObject;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
			throw e;
		}
	}

	////test and readPJ for test
	public String test(String str) throws Exception
	{
        Request request = new Request("Nothing","parameter");
        return (String) sendRequest(request) ;
	}

	boolean validateConnection(Socket socket) throws Exception	//not used yet
	{
		byte[] buffer = new byte[validateString.length()];
		socket.getInputStream().read(buffer, 0, validateString.length());
		return (new String(buffer).equals(validateString));
	}

    //implements ClientAPI

    @Override
    public Member logIn(String userEmail, String userPassword) throws Exception
    {
        Request request = new Request(
        		"logIn",
        		userEmail, 
        		PIMSecurityManager.md5Encoder(userPassword)
        		);
        return (Member) sendRequest(request) ;
    }

    @Override
    public Boolean forgetPassword(String userEmail) throws Exception
    {
    	Request request = new Request(
    			"forgetPassword", 
    			userEmail
    			) ;
    	return (Boolean) sendRequest(request) ;
    }
    
    @Override
    public Boolean createAccount(String userEmail, String userPassword, String userName) throws Exception 
    {
        Request request = new Request(
        		"createAccount",
        		userEmail, 
        		PIMSecurityManager.md5Encoder(userPassword),
        		userName
        		); 
        return (Boolean) sendRequest(request) ;
    }

    @Override
    public ArrayList<Project> getProjectList(Member member) throws Exception
    {
        Request request = new Request(
        		"getProjectList",
        		member.getMbID()
        		);
        return (ArrayList<Project>) sendRequest(request) ;
    }

    @Override
    public ArrayList<Project> getInvitingProjectList(Member member) throws Exception
    {
        Request request = new Request(
                "getInvitingProjectList",
                member.getMbID()
        );
        return (ArrayList<Project>) sendRequest(request) ;
    }

    @Override
    public ArrayList<MeetingMinutes> getMeetingMinutesList(Project project) throws Exception
    {
        Request request = new Request(
        		"getMeetingMinutesList",
        		project.getPjID()
        		);
        return (ArrayList<MeetingMinutes>) sendRequest(request) ;
    }

    @Override
    public Boolean respondInvitation(Member member, Project project, Boolean accept) throws Exception
    {
        Request request = new Request(
        		"respondInvitation", 
        		member.getMbID(), 
        		project.getPjID(), 
        		accept
        		);
        return (Boolean) sendRequest(request) ;
    }

    @Override
    public Boolean createProject(Member member, Project newProject, ArrayList<String> emailList) throws Exception
    {
        Request request = new Request(
        		"createProject", 
        		member.getMbID(), 
        		newProject.getPjName(),
        		newProject.getPjGoal(), 
        		newProject.getPjDeadline(), 
        		emailList
        		);
        return (Boolean) sendRequest(request) ;
    }

    @Override
    public ArrayList<ProjectMember> getMemberList(Project project) throws Exception
    {
        Request request = new Request(
        		"getMemberList",
        		project.getPjID()
        		);
        return (ArrayList<ProjectMember>) sendRequest(request) ;
    }
    
    @Override
    public ArrayList<ProjectMember> getInvitingMemberList(Project project) throws Exception
    {
    	Request request = new Request(
    			"getInvitingMemberList",
    			project.getPjID()
    			);
        return ( ArrayList<ProjectMember>) sendRequest(request) ;
    }

    @Override
    public Boolean createMeetingMinutes(Project project, MeetingMinutes newMeetingMinutes) throws Exception 
    {
        Request request = new Request(
        		"createMeetingMinutes", 
        		project.getPjID(), 
        		newMeetingMinutes.getContent()
        		);
        return (Boolean) sendRequest(request) ;
    }

    @Override
    public Boolean modifyProject(Member member, Project project) throws Exception 
    {
        Request request = new Request(
        		"modifyProject", 
        		project.getPjID(), 
        		member.getMbID(), 
        		project.getPjName(), 
        		project.getPjGoal(), 
        		project.getPjDeadline(), 
        		project.getPjManagerID()
        		);
        return (Boolean) sendRequest(request) ;
    }

    @Override
    public Boolean modifyMeetingMinutes(MeetingMinutes meetingMinutes) throws Exception 
    {
        Request request = new Request(
        		"modifyMeetingMinutes", 
        		meetingMinutes.getmmid(), 
        		meetingMinutes.getContent()
        		);
        return (Boolean) sendRequest(request) ;
    }

    // implement the APIs
/*
    @Override
    public Member login(String userEmail, String userPassword) {
        // just for test
    	
        Member mb= new Member(123456789, userEmail, "Adam");
        return mb;
    }

    @Override
    public boolean forget_password(String userEmail) throws Exception {
        return true;
    }

    @Override
    public boolean register(String userEmail, String userPassword, String userName) {
        return true;
    }

    @Override
    public Member get_member_setting(int mbID) {
        return new Member();
    }

    @Override
    public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName) {
        return true;
    }

    @Override
    public ArrayList<Project> get_project_List(int mbID) throws Exception{
        return new ArrayList<Project>();
    }

    @Override
    public Project get_project_setting(int pjID) throws Exception{
        return new Project(1,"a","b", 2, "aaa", new Date());
    }

    @Override
    public boolean create_new_project(int mbID, String pjName, String pjGoal, Date pjDeadline) {
        return true;
    }

    @Override
    public boolean update_project_setting(int pjID, int mbID, String pjName, String pjGoal, Date pjDeadline, int pjManagerID) {
        return true;
    }

    @Override
    public Member find_member_with_email(String userEmail) {
        return new Member();
    }

    @Override
    public boolean invite(int mbID, int pjID) {
        return true;
    }

    @Override
    public boolean respond_to_invitation(int mbID, int pjID, boolean accept) {
        return true;
    }

    @Override
    public ArrayList<MeetingMinutesAbstract> get_timeline(int pjID) {
        return new ArrayList<MeetingMinutesAbstract>();
    }

    @Override
    public boolean create_new_MM(int pjID, MeetingMinutesContent mmContent)  throws Exception{
        return true;
    }

    @Override
    public boolean update_old_MM(int mmID, MeetingMinutesContent mmContent) throws Exception{
        return true;
    }

    @Override
    public MeetingMinutes read_MM(int mmID) {
		return new MeetingMinutes(234,567,new Date(),
                new MeetingMinutesContent(new Date(),"Here","Me","You","Meeting",new ArrayList<Participant>(),"None","Empty",new ArrayList<ActionItem>()));
	}

    @Override
    public ArrayList<ProjectMember> get_project_member_list(int pjID) {
        return new ArrayList<ProjectMember>();
    }
*/
}
