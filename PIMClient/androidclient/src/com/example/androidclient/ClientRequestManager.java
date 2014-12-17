package com.example.androidclient;

import java.io.*;
import java.net.*;
import java.util.*;


public class ClientRequestManager implements API {
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
			
			Object receivedobject = input.readObject() ;
			System.out.println("Response received.");
			
			socket.close();
			return receivedobject;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
			throw e;
		}
	}

	////createPJ and readPJ for test
	boolean createPJ(Project pj) throws Exception
	{
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter<Integer>("mbID", 5566));
        parameters.add(new Parameter<String>("pjName", "My Project"));
        parameters.add(new Parameter<String>("pjGoal", "Reach it"));
        parameters.add(new Parameter<Date>("pjDeadline", new Date()));
        Request createPJRequest = new Request("abc", parameters);
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
    public ArrayList<MeetingMinutes> get_timeline(int pjID) {
        return new ArrayList<MeetingMinutes>();
    }

    @Override
    public boolean create_new_MM(int pjID, MeetingMinutes mmContent, String mmObjective, Date mmMmeetingDate)  throws Exception{
        return true;
    }

    @Override
    public boolean update_old_MM(int pjID, int mmID, MeetingMinutes mmContent, String mmObjective, Date mmMeetingDate) throws Exception{
        MeetingMinutes mm = new MeetingMinutes(new Date(1420072200000L), "Here", "me", "you", "Forget Everthing", new ArrayList<Participant>(), "Amanda", "Sue", null);
        return true;
    }

    @Override
    public MeetingMinutes read_MM(int pjID, int mmID) {
		return new MeetingMinutes(new Date(1420072200000L), "Here", "me", "you", "Forget Everthing", new ArrayList<Participant>(), "Amanda", "Sue", null);
	}

    @Override
    public ArrayList<ProjectMember> get_project_member_list(int pjID) {
        return new ArrayList<ProjectMember>();
    }
}
