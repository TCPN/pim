package pim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

// this Activity is for test
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
		
        crm = new ClientRequestManager("54.148.152.17", 80);
        //crm = new ClientRequestManager("220.134.20.34", 9999);
		
        //撰寫一個按鈕事件，當按下按鈕時，才啟動一些功能
        btn.setOnClickListener(new View.OnClickListener() 
        {
            String ret = "";
        	public void onClick(View arg0) 
        	{
        		try { // for test
					//ret = crm.test(pj);
                    //ret = crm.respondInvitation(mb, pj, true);
                    ret = crm.test(ed.getText().toString());
					//ret = crm.forget_password(input);
                    //ret = crm.update_old_MM(0,0,null,"",null);
                    Project pj = null ;
                    Member mb = null;
                    boolean b = false;
                    ArrayList<Project> pl = null, ipl = null;
                    ArrayList<ProjectMember> pml = null, ipml = null;
                    ArrayList<MeetingMinutes> mml = null;

                            //Account related
                    mb = crm.logIn("boyenen@gmail.com",  "password");
                    b = crm.createAccount("boyenen@gmail.com", "password", "USER");
                    b = crm.createAccount("b99b01062@ntu.edu.tw", "Forgotten", "Student");
                    b = crm.createAccount("this is an email address", "1234567890!@#$%^&*()", "~`_-+=|\\{[}]:;\"'<,>.?/");
                    b = crm.forgetPassword("b99b01062@ntu.edu.tw");
                    mb = crm.logIn("b99b01062@ntu.edu.tw", "Forgotten");
                    mb = crm.logIn("boyenen@gmail.com",  "password");

                    if(mb == null)
                        return;

                    //Read related
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);
                    ArrayList<String> emaillist= new ArrayList<String>() ;
                    emaillist.add("b99b01062@ntu.edu.tw");
                    emaillist.add("this is an email address");
                    b = crm.createProject(mb, new Project(9876, "TestProject", "Goal!!", mb.getMbID(), "NoNameMember", new Date()), emaillist);
                    b = crm.createProject(mb, new Project(0, "TestProject", "Goal!!", mb.getMbID(), "USER", new Date()), null);
                    emaillist.add("boyenen@gmail.com");
                    b = crm.createProject(mb, new Project(0, "Project3", "3Goal!!", mb.getMbID(), "newUSER", new Date()), emaillist);
                    emaillist.remove(0);
                    emaillist.add("this is an email address");
                    b = crm.createProject(mb, new Project(0, "newProject", "newGoal!!", 1234, "newUSER", new Date()), emaillist);
                    b = crm.createProject(mb, new Project(0, "newProject", "newGoal!!", mb.getMbID(), "newUSER", new Date()), emaillist);
                    b = crm.createProject(mb, new Project(0, "WrongProject", "FaultyGoal!!", mb.getMbID(), "CrackedUser", new Date()), emaillist);
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);
                    pml = crm.getMemberList(pl.get(0));
                    ipml = crm.getMemberList(pl.get(0));
                    pml = crm.getMemberList(pl.get(1));
                    ipml = crm.getMemberList(pl.get(1));

                    mb = crm.logIn("this is an email address",  "1234567890!@#$%^&*()");
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);
                    b = crm.respondInvitation(mb, ipl.get(0), true);
                    b = crm.respondInvitation(mb, ipl.get(1), false);
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);

                    mb = crm.logIn("boyenen@gmail.com",  "password");
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);
                    pml = crm.getMemberList(pl.get(0));
                    ipml = crm.getMemberList(pl.get(0));
                    pml = crm.getMemberList(pl.get(1));
                    ipml = crm.getMemberList(pl.get(1));

                    pj = pl.get(0);
                    mml = crm.getMeetingMinutesList(pj);
                    b = crm.createMeetingMinutes(pj, new MeetingMinutes(0, pj.getPjID(), new Date(),
                            new MeetingMinutesContent(new Date(), "Here", "You", "Me", "OBJ", null, "A", "I", null)));
                    b = crm.createMeetingMinutes(pj, new MeetingMinutes(0, pj.getPjID(), new Date(),
                            new MeetingMinutesContent(new Date(), "there", "Youtoo", "MeTwo", "OBJ2",
                                    new ArrayList<Participant>(), "A22", "22I", new ArrayList<ActionItem>())));
                    ArrayList<Participant> pcl = new ArrayList<Participant>();
                    pcl.add(new Participant("hi","my name", "is Joe", true));
                    pcl.add(new Participant("I","ain't", "a man", false));
                    pcl.add(new Participant("I","am", "a boy", true));
                    ArrayList<ActionItem> acl = new ArrayList<ActionItem>();
                    acl.add(new ActionItem("hi1","my name4", new java.sql.Date(1400000000), "OnGoing",""));
                    acl.add(new ActionItem("hi2","my name5", new java.sql.Date(1500000000), "Suspend",""));
                    acl.add(new ActionItem("hi2","my name6", new java.sql.Date(1600000000), "Colsed",""));
                    b = crm.createMeetingMinutes(pj, new MeetingMinutes(0, pj.getPjID(), new Date(),
                            new MeetingMinutesContent(new Date(), "there", "Youtoo", "MeTwo", "OBJ2", pcl, "A22", "22I", acl)));

                    // modify project and meeting minutes
                    // if you're going to use this, please new a new instance of Project,
                    // ths new manager ID and Name can get from ProjectMember List
                    // Backend needs member to verified


                    mb = crm.logIn("this is an email address",  "1234567890!@#$%^&*()");
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);
                    pj = pl.get(0);
                    pj.pjDeadline.setTime(1300000000);
                    pj = new Project(pj.getPjID(), pj.pjName+"yayaya", pj.pjGoal, pj.pjManagerID+1111,pj.pjManagerName,pj.getPjDeadline());
                    b = crm.modifyProject(mb, pj);

                    mml.get(0).content.objective = mml.get(0).content.objective + "\nwhat's the problem?";
                    b = crm.modifyMeetingMinutes(mml.get(0));

                    mb = crm.logIn("boyenen@gmail.com",  "password");
                    pl = crm.getProjectList(mb);
                    ipl = crm.getInvitingProjectList(mb);
                    mml = crm.getMeetingMinutesList(pj);


                } catch (Exception e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
					ret = e.toString();
                }

				//之後在讓它顯示在銀幕上，有時網路不夠穩時，傳輸會較慢，而導致傳誦與接收都尚未完成，便顯示到銀幕上了
        		System.out.println("Finish fetching...");
                tv1.setText(ret);
                System.out.printf("returned String: %s", ret);
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
