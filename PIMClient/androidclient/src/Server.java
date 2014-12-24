import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server
{
	
	final String validateString = "imvalidate";
    ServerSocket serverSocket = null ;
	int port = 0;
	int connectCount = 0;
	
    public static void main(String args[]) throws Exception
	{
		if(args.length > 0)
			(new Server(Integer.parseInt(args[0]))).run();	// take argument as port
		else
			(new Server(80)).run();							// HTTP port
    }
	
    public Server(int port) throws Exception
	{
		this.port = port;
        serverSocket = new ServerSocket(this.port) ;
        System.out.println("Start Server at " + this.port + " ...");
    }

    public void run()
	{
		try
		{

			while(true)
			{
				// 1, ���ݤ@�ӷs���s���ШD(Request).

				Socket socket = serverSocket.accept();
				Date connectTime = new Date() ;
				System.out.println(connectTime +"\tA connection from ["+ socket.getInetAddress().getHostAddress() +"] comes...");
				connectCount ++;
				/*
				if(validateConnection(socket) == false)
				{
					socket.close();
					continue;
				}
				*/

				// 2, �}�sThread�B�z�s���ШD.
				
				if(this.port == 80)
				{
					System.out.println("A http request handler on");
					(new HttpRequestHandler(socket)).start();
				}
				else
				{
					System.out.println("An object message handler on");
					(new ServerRequestManager(socket, connectCount, connectTime)).start();
				}
				System.out.println("New Thread " + connectCount + " is started to handle it...");

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


// End of Server.java