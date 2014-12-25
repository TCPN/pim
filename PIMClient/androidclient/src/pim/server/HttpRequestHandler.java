package pim.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class HttpRequestHandler extends Thread
{
	Socket socket;
	BufferedReader input;
	DataOutputStream output;
	
	public HttpRequestHandler(Socket socket)
	{
		this.socket = socket;
		try
		{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new DataOutputStream(socket.getOutputStream());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
    public void run()
	{

        try
		{

            // 1,  read HTTP request Header

            String request = "";
			String line = "";
            while (true)
			{

                line = input.readLine();
				System.out.println(line);
                if (null == line) {
                    break;
                }

                request += line + "\r\n";
                if (0 == line.length()) {
                    break;
                }
            }

            System.out.println("reply with http header");
			
			String homepage = "I love you";
			output.writeBytes(
				 "HTTP/1.0 200 OK\r\nContent-Length: " +
				 homepage.length() +
				 "\r\n\r\n" +
				 homepage);

			this.socket.close();
        }
		catch (Exception e)
		{
			System.out.println(e);
        }
    }
}