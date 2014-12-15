package com.example.androidclient;

import java.io.*;
import java.net.*;
import java.util.*;

import android.os.AsyncTask;
import android.os.StrictMode ; //tmp
/*
class MB{}
class MBinfo{}
class PJ{}
class PJMB{}
class MM{}
class MMinfo{}
*/
public class ClientRequestManager extends AsyncTask<Socket, Void, Void> implements API
//extends Async
{
	
	final String validateString = "imvalidate";
    Socket socket = null ;
	BufferedReader input;
	DataOutputStream output;
	String Address ;
	int Port ;
	InputStream in = null ;
	OutputStream out = null ;


    public ClientRequestManager(String address, int port) 
	{
    	
		this.Address = address ;
		this.Port = port ;
        
    }
  private void setupAll()
    {
    	if(this.socket==null) setupSocket() ;
    	else
    	{
    		System.out.println("Waiting...") ;
    		while(this.socket.isClosed()==false) ;
    		setupSocket() ;
    	}
    	
    }
  private void setupSocket()
  {
	  this.socket = new Socket() ;
	 
	  /*
	  try 
	  {
		  this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	  } 
	  catch (IOException e) 
	  {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  try 
	  {
		  this.output = new DataOutputStream(socket.getOutputStream());
	  } catch (IOException e) 
	  {
			// TODO Auto-generated catch block
		  e.printStackTrace();
	  }*/
		
	  
  }
    public void doSomething(String S) throws Exception //應該改成 private void write_and_read，然後implement每個API時，最後都call這個
	{
		System.out.println("doSomething ... ");
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));	// read from standard_input
		String line = "";
		System.out.println("ClientRequestManager:Start connect...");
		try
		{
			
			
			setupAll() ;
			socket.connect(new InetSocketAddress(Address, Port), 15000) ;
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new DataOutputStream(socket.getOutputStream());
			//this.in=socket.getInputStream();
			//this.out=socket.getOutputStream();
			line = "XDDDDDDDDD" ;
			output.writeBytes(S) ;
			String inputline = input.readLine() ;
			System.out.println(S);
			
			
			System.out.println("Connected!!");
			/*
			byte[] rebyte = new byte[18];
	    	in.read(rebyte);
	    	//str2 ="Text:"+ new String(new String(rebyte));
	    	//String str = "2222";
	    	//str1 = "Text:" + str;
	    	byte[] sendstr = new byte[21];
	    	System.arraycopy(S.getBytes(), 0, sendstr, 0, S.length());
	    	out.write(sendstr);
			
			do
			{
				System.out.println("type anything:");
				line = stdin.readLine();
				output.writeBytes(line + "\r\n");
				line = input.readLine();
				System.out.println(line);
				
				if(validateConnection(socket) == false)
				{
					socket.close();
					continue;
				}
				

			}while(line!="EXIT");*/
			
		}
		catch(UnknownHostException e)
		{
			System.out.println("ClientRequestManager:Unknown Host");
			throw e;
		}
		catch(SocketTimeoutException e)
		{
			
			System.out.println("ClientRequestManager:Request timeout");
			throw e;
		}
		finally
		{
			socket.close() ;
		}
		
        
	}
	
	boolean validateConnection(Socket socket) throws Exception	//not used yet
	{
		byte[] buffer = new byte[validateString.length()];
		socket.getInputStream().read(buffer, 0, validateString.length());
		return (new String(buffer) == validateString);
	}
	
	// implement the APIs
	
	public MB login(String userEmail, String userPassword)
	{
		//return new MB();
		return null ;
	}
	public boolean forget_password(String userEmail)
	{
		return true;
	}
	public boolean register(String userEmail, String userPassword, String userName)
	{
		return true;
	}
	public MB enter_member_setting(int mbID)
	{
		//return new MB();
		return null ;
	}
	public boolean update_member_setting(int mbID, String mbEmail, String mbPassword, String mbName)
	{
		return true;
	}
	public PJ create_new_project(int mbID, String pjName, String pjGoal, String pjDeadline, int pjManager)
	{
		//return new PJ();
		return null ;
	}
	public PJ update_project_setting(int pjID, int mbID, String pjName, String pjGoal, String pjDeadline, int pjManagerID)
	{
		//return new PJ();
		return null ;
	}
	public MBinfo find_member_with_email(String userEmail)
	{
		return new MBinfo();
	}
	public boolean invite(int mbID, int pjID)
	{
		return true;
	}
	public boolean respond_to_invitation(int mbID, int pjID, boolean accept)
	{
		return true;
	}
	public ArrayList<MMinfo> enter_timeline(int pjID)
	{
		return new ArrayList<MMinfo>();
	}
	public Message create_new_mm(int pjID, String mmContent, String mmObjective, Date mmMmeetingDate)
	{
		return API.Message.SUCCESS;
	}
	public Message update_old_mm(int pjID, int mmID, String mmContent, String mmObjective, Date mmMeetingDate)
	{
		return API.Message.SUCCESS;
	}
	public MM read_exist_MM(int pjID, int mmID) throws Exception
	{
		System.out.println("ClientRequestManager:Start connect...");
		try
		{
			socket.connect(new InetSocketAddress(Address, Port), 5000) ;
		}
		catch(UnknownHostException e)
		{
			System.out.println("ClientRequestManager:Unknown Host");
			throw e;
		}
		catch(SocketTimeoutException e)
		{
			System.out.println("ClientRequestManager:Request timeout");
			throw e;
		}
        System.out.println("ClientRequestManager:connected ...");
		return new MM();
	}
	public ArrayList<PJMB> select_project_member(int pjID)
	{
		return new ArrayList<PJMB>();
	}

	@Override
	public MB get_member_setting(int mbID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MMinfo> get_timeline(int pjID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean create_new_MM(int pjID, String mmContent,
			String mmObjective, Date mmMmeetingDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update_old_MM(int pjID, int mmID, String mmContent,
			String mmObjective, Date mmMeetingDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MM read_MM(int pjID, int mmID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PJMB> get_project_member(int pjID) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected Void doInBackground(Socket... arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}


// End of Server.java
