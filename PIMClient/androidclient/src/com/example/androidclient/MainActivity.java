package com.example.androidclient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi") public class MainActivity extends Activity 
{

	static EditText ed;
	static TextView tv1,tv2;
	static Button btn ;
	static String str1="0",str2="0";
	
	ClientRequestManager CRM = new ClientRequestManager("10.0.2.2", 5566) ;	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //tmp
        
        //先建立好與XML的元件連結
        final EditText ed = (EditText) findViewById(R.id.editText1);
        tv1 = (TextView) findViewById(R.id.show01);
        tv2 = (TextView) findViewById(R.id.show02);
        btn = (Button) findViewById(R.id.btn);
        
        //撰寫一個按鈕事件，當按下按鈕時，才啟動一些功能
        btn.setOnClickListener(new OnClickListener() 
        {
        	
        	
        	
        	int i ;
        	
        	//String s = "QQ" ;
        	
        	public void onClick(View arg0) 
        	{
        		String s = ed.getText().toString() ;
        		
        		try {
					CRM.doSomething(s) ;
				} 
        		catch(UnknownHostException e)
        		{
        			System.out.println("ClientRequestManager:Unknown Host");
        			
        		}
        		catch(SocketTimeoutException e)
        		{
        			System.out.println("ClientRequestManager:Request timeout");
        			
        		}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            //之後在讓它顯示在銀幕上，有時網路不夠穩時，傳輸會較慢，而導致傳誦與接收都尚未完成，便顯示到銀幕上了
        		System.out.println("Finish fetching...");
        		tv1.setText(str1);
        		tv2.setText(str2);
        		System.out.printf("str2: %s", str2);
        		System.out.println("Finish fetching...");
        	}
        });    
    }
    
    class thread extends Thread
    {
    	public void run() 
    	{
    		try
    		{
    			System.out.println("Waitting to connect......");
      //String server=ed.getText().toString();
    			String server="10.0.2.2" ;
    			int servPort=5566;
    			Socket socket=new Socket(server,servPort);
     
   				InputStream in=socket.getInputStream();
   				OutputStream out=socket.getOutputStream();
   				System.out.println("Connected!!");
   				
   				byte[] rebyte = new byte[18];
   	    		in.read(rebyte);
   	    		str2 ="Text:"+ new String(new String(rebyte));
   	    		String str = "2222";
   	    		str1 = "Text:" + str;
   	    		byte[] sendstr = new byte[21];
   	    		System.arraycopy(str.getBytes(), 0, sendstr, 0, str.length());
   	    		out.write(sendstr);
   	    		/*
   				byte[] rebyte = new byte[18];
    			in.read(rebyte);
    			str2 =new String(new String(rebyte));
    			String str = "android client string";
   				str1 = "(Client端)傳送的文字:"+str;
   				byte[] sendstr = new byte[21];
   				System.arraycopy(str.getBytes(), 0, sendstr, 0, str.length());
   				System.out.printf("%s", rebyte);
   				out.write(sendstr);*/
    		}
    		catch(Exception e)
    		{
    			System.out.println("Error: "+e.getMessage());
    		}
      }
    }
}