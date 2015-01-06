package pim.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server
{
	
	final String validateString = "imvalidate";
    ServerSocket serverSocket = null ;
	String protocol;
	int port = 0;
	int connectCount = 0;
	
    public static void main(String args[]) throws Exception
	{
		if(args.length >= 2)
			(new Server(Integer.parseInt(args[0]), args[1])).run();	// take argument as port
		if(args.length >= 1)
			(new Server(Integer.parseInt(args[0]), "object")).run();	// take argument as port
		else
			(new Server(80, "object")).run();							// HTTP port
    }
	
    public Server(int port, String protocol) throws Exception
	{
		this.protocol = protocol;
		this.port = port;
        serverSocket = new ServerSocket(this.port) ;
        System.out.println("Start " + this.protocol + " pim.server.Server at " + this.port + " ...");
    }

    public void run()
	{
		try
		{
			while(true)
			{
                System.out.println("waiting connect...");
				Socket socket = serverSocket.accept();
				Date connectTime = new Date() ;
				System.out.println(connectTime +"\tA connection from ["+ socket.getInetAddress().getHostAddress() +"] comes (connect: "+
                        (++connectCount)+
                        ")...");
				if(this.protocol.equals("http"))
				{
                    System.out.println("Start a http request handler...");
					(new HttpRequestHandler(socket)).start();
				}
				else
				{
                    System.out.println("Build an object message handler (connect: "+(connectCount)+")...");
                    ServerRequestManager srm = new ServerRequestManager(socket, connectCount, connectTime);
                    System.out.println("Handler built (connect: "+(connectCount)+")");
					srm.start();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	boolean validateConnection(Socket socket) throws Exception	//not used yet
	{
		byte[] buffer = new byte[validateString.length()];
		socket.getInputStream().read(buffer, 0, validateString.length());
		return (new String(buffer) == validateString);
	}

}


// End of pim.server.Server.java