
import java.io.*;
import java.net.*;
import java.util.*;


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

            // 1, 讀取HTTP Header字串

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

            // 2, 解出請求的資源路徑(或包含?query字串)
			
            // 3, 處理請求的資源. (測試: 只處理Homepage的請求)

			
			String homepage = "I love you";
			output.writeBytes(
				 "HTTP/1.0 200 OK\r\nContent-Length: " +
				 homepage.length() +
				 "\r\n\r\n" +
				 homepage);
			
			
            // TODO: 處理其它請求.

			this.socket.close();
        }
		catch (Exception e)
		{
			System.out.println(e);
        }
    }
}