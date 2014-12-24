package pim;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.*;
import android.widget.*;


@SuppressLint("NewApi") public class MainActivity extends Activity 
{

	EditText ed;
	TextView tv1;
	Button btn ;
	String str1="0", str2="0";
	
	ClientRequestManager crm = null;
	//ClientRequestManager crm = new ClientRequestManager("220.134.20.34", 9999) ;	
	//ClientRequestManager crm = new ClientRequestManager("10.0.2.2", 5566) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } //tmp
        
        //先建立好與XML的元件連結
        ed = (EditText) findViewById(R.id.editText);
        tv1 = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);
		
        crm = new ClientRequestManager("220.134.20.34", 9999);		
        final Project pj = new Project(3370, "eclipsesucks", "write", 1, "doge", null) ;
		
        //撰寫一個按鈕事件，當按下按鈕時，才啟動一些功能
        btn.setOnClickListener(new View.OnClickListener() 
        {
            boolean ret = true;
        	public void onClick(View arg0) 
        	{
        		String input = ed.getText().toString();        		
        		try { // for test
					boolean pj_ret = crm.createPJ(pj);
					//ret = crm.forget_password(input);
                    //ret = crm.update_old_MM(0,0,null,"",null);					
				} catch (Exception e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
					ret = false;
                }

				//之後在讓它顯示在銀幕上，有時網路不夠穩時，傳輸會較慢，而導致傳誦與接收都尚未完成，便顯示到銀幕上了
        		System.out.println("Finish fetching...");
                tv1.setText(ret ? "true" : "false");
                System.out.printf("str2: %s", ret ? "true" : "false");
        		System.out.println("Finish fetching...");
        	}
        });    
    }
    /*
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
   				out.write(sendstr);/*
    		}
    		catch(Exception e)
    		{
    			System.out.println("Error: "+e.getMessage());
    		}
      }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }	
}
