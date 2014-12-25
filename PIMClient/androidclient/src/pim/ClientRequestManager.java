package pim;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;


public class ClientRequestManager implements API, ClientAPI {
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
			System.out.println("Response received. Type:" + receivedObject.getClass().toString());
			
			socket.close();
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
	boolean test(Project pj) throws Exception
	{
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Integer>("mbID", 5566));
        parameters.add(new Parameter<String>("pjName", SecurityManager.md5Encoder("My Project")));
        parameters.add(new Parameter<String>("pjGoal", "Reach it"));
        parameters.add(new Parameter<Date>("pjDeadline", new Date()));
        parameters.add(new Parameter<Project>("pjObject", pj));
        Request createPJRequest = new Request("test", parameters);
		boolean success = (Boolean) sendRequest(createPJRequest) ;
		return success ;
	}

	Project readPJ(int pjID) throws Exception
	{
		//Request readPJRequest = new CreateRequest("READ_PJ", pjID) ;
		//Project retpj = (Project) sendRequest(readPJRequest) ;
		//return retpj ;
		return null ;
	}


	boolean validateConnection(Socket socket) throws Exception	//not used yet
	{
		byte[] buffer = new byte[validateString.length()];
		socket.getInputStream().read(buffer, 0, validateString.length());
		return (new String(buffer).equals(validateString));
	}

    //implements ClientAPI

    @Override
    public Member logIn(String userEmail, String userPassword) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<String>("userEmail", userEmail));
        parameters.add(new Parameter<String>("userPassword", SecurityManager.md5Encoder(userPassword)));
        Request request = new Request("logIn", parameters);
        return (Member) sendRequest(request) ;
    }

    @Override
    public boolean createAccount(String userEmail, String userPassword) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<String>("userEmail", userEmail));
        parameters.add(new Parameter<String>("userPassword", SecurityManager.md5Encoder(userPassword)));
        Request request = new Request("createAccount", parameters);
        return (boolean) sendRequest(request) ;
    }

    @Override
    public ArrayList<Project> getMemberProjectList(Member member) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Member>("member", member));
        Request request = new Request("getMemberProjectList", parameters);
        return (ArrayList<Project>) sendRequest(request) ;
    }

    @Override
    public ArrayList<MeetingMinutes> getMeetingMinutesList(Project project) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Project>("project", project));
        Request request = new Request("getMeetingMinutesList", parameters);
        return (ArrayList<MeetingMinutes>) sendRequest(request) ;
    }

    @Override
    public ArrayList<Project> getMemberInvitationList(Member member) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Member>("member", member));
        Request request = new Request("getMemberInvitationList", parameters);
        return (ArrayList<Project>) sendRequest(request) ;
    }

    @Override
    public boolean respondInvitation(Member member, Project project, Boolean accept) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Member>("member", member));
        parameters.add(new Parameter<Project>("project", project));
        parameters.add(new Parameter<Boolean>("accept", accept));
        Request request = new Request("respondInvitation", parameters);
        return (boolean) sendRequest(request) ;
    }

    @Override
    public boolean createProject(Project newProject) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Project>("newProject", newProject));
        Request request = new Request("createProject", parameters);
        return (boolean) sendRequest(request) ;
    }

    @Override
    public boolean createProjectMemberList(ArrayList<String> email) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<ArrayList<String>>("email", email));
        Request request = new Request("createProjectMemberList", parameters);
        return (boolean) sendRequest(request) ;
    }

    @Override
    public boolean createMeetingMinutes(MeetingMinutes newMeetingMinutes) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<MeetingMinutes>("newMeetingMinutes", newMeetingMinutes));
        Request request = new Request("createMeetingMinutes", parameters);
        return (boolean) sendRequest(request) ;
    }

    @Override
    public boolean modifyProject(Project project) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Project>("project", project));
        Request request = new Request("modifyProject", parameters);
        return (boolean) sendRequest(request) ;
    }

    @Override
    public boolean modifyMeetingMinutes(MeetingMinutes minutes) throws Exception {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<MeetingMinutes>("minutes", minutes));
        Request request = new Request("modifyMeetingMinutes", parameters);
        return (boolean) sendRequest(request) ;
    }

    // implement the APIs

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

}
