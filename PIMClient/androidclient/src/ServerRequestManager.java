
import java.io.*;
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
	
	private ArrayList<Parameter>handleRequest(Request request) throws Exception
	{
		/*
		 * 
		 * */
		String reqtype = request.getName() ;
		ArrayList<Parameter> response = null ;
		if(reqtype.equals("createProject"))
		{
			//Unpack Parameters in Request, call dbconnector
			//Dbconnector.createProject() ;
			
		}
		//else if...for all DBConnector API
		else
		{
			//for error
		}
		return response ;
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
			ArrayList<Parameter> response = handleRequest(req) ;
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