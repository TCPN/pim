package pim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import pim_data.ActionItem;
import pim_data.MeetingMinutes;
import pim_data.MeetingMinutesContent;
import pim_data.Member;
import pim_data.Project;
import pim_data.ProjectMember;

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
                    //ret = crm.test(ed.getText().toString());
					//ret = crm.forget_password(input);
                    //ret = crm.update_old_MM(0,0,null,"",null);
                    Project pj = null ;
                    Member mb = null;
                    boolean b = false;
                    ArrayList<Project> pl = null, ipl = null;
                    ArrayList<ProjectMember> pml = null, ipml = null;
                    ArrayList<MeetingMinutes> mml = null;

                    String elog = "";
                            //Account related
                    //crm.test("YA");
                    int i = 77;
                    mb = crm.logIn("boyenen@gmail.com", ed.getText().toString());
//                    b = crm.modifyMemberPassword(mb, "hithere");
//                    b = crm.modifyMemberName(mb, "you you you");
//                    mb = crm.logIn("boyenen@gmail.com",  "hithere");
//                    b = crm.modifyMemberPassword(mb, ed.getText().toString());
//                    b = crm.forgetPassword("boyenen@gmail.com");
                    if(mb != null)
                        throw new Exception("Login succeed");
                    else if(mb == null)
                        throw new Exception("Login failed.");

i++;                    try{mb = crm.logIn("boyenen@gmail.com",  "password");}catch(Exception e){elog = elog + ";69:" + e;}
i++;                    if(BuildConfig.DEBUG &&!( mb == null)) throw new Exception(""+ i);
i++;                    b = crm.createAccount("boyenen@gmail.com", "password", "USER");
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.createAccount("b99b01062@ntu.edu.tw", "Forgotten", "Student");
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.createAccount("this is an email address", "1234567890!@#$%^&*()", "~`_-+=|\\{[}]:;\"'<,>.?/");
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.forgetPassword("b99b01062@ntu.edu.tw");
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    try{mb = crm.logIn("b99b01062@ntu.edu.tw", "Forgotten");}catch(Exception e){elog = elog + ";74:" + e;}
i++;                    if(BuildConfig.DEBUG &&!(  mb == null )) throw new Exception(""+ i);
i++;                    mb = crm.logIn("boyenen@gmail.com",  "password");
i++;                    if(BuildConfig.DEBUG &&!(  mb != null )) throw new Exception(""+ i);
i++;
i++;                    //Read related
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 0 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 0 )) throw new Exception(""+ i);
i++;                    ArrayList<String> emaillist= new ArrayList<String>() ;
i++;                    emaillist.add("b99b01062@ntu.edu.tw");
i++;                    emaillist.add("this is an email address");
i++;                    b = crm.createProject(mb, new Project(9876, "TestProject", "Goal!!", mb.getMbID(), "NoNameMember", new Date()), emaillist);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.createProject(mb, new Project(0, "TestProject", "Goal!!", mb.getMbID(), "USER", new Date()), null);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    emaillist.add("boyenen@gmail.com");
i++;                    b = crm.createProject(mb, new Project(0, "Project3", "3Goal!!", mb.getMbID(), "newUSER", new Date()), emaillist);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    emaillist.remove(0);
i++;                    emaillist.add("this is an email address");
i++;                    try{b = crm.createProject(mb, new Project(0, "newProjectbad", "newbadGoal!!", 1234, "newUSER", new Date()), emaillist);}catch(Exception e){elog = elog + ";92:" + e;}
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.createProject(mb, new Project(0, "newProject", "newGoal!!", mb.getMbID(), "newUSER", new Date()), emaillist);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.createProject(mb, new Project(0, "WrongProject", "FaultyGoal!!", mb.getMbID(), "CrackedUser", new Date()), emaillist);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 6 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 0 )) throw new Exception(""+ i);
i++;                    pml = crm.getMemberList(pl.get(0));
i++;                    if(BuildConfig.DEBUG &&!(  pml != null && pml.size() == 1 )) throw new Exception(""+ i);
i++;                    ipml = crm.getInvitingMemberList(pl.get(0));
i++;                    if(BuildConfig.DEBUG &&!(  ipml != null && ipml.size() == 2 )) throw new Exception(""+ i);
i++;                    pml = crm.getMemberList(pl.get(1));
i++;                    if(BuildConfig.DEBUG &&!(  pml != null && pml.size() == 1 )) throw new Exception(""+ i);
i++;                    ipml = crm.getInvitingMemberList(pl.get(1));
i++;                    if(BuildConfig.DEBUG &&!(  ipml != null && ipml.size() == 0 )) throw new Exception(""+ i);
i++;                    pml = crm.getMemberList(pl.get(2));
i++;                    if(BuildConfig.DEBUG &&!(  pml != null && pml.size() == 1 )) throw new Exception(""+ i);
i++;                    ipml = crm.getInvitingMemberList(pl.get(2));
i++;                    if(BuildConfig.DEBUG &&!(  ipml != null && ipml.size() == 2 )) throw new Exception(""+ i);
i++;
i++;                    mb = crm.logIn("this is an email address",  "1234567890!@#$%^&*()");
i++;                    if(BuildConfig.DEBUG &&!(  mb != null )) throw new Exception(""+ i);
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 0 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 5 )) throw new Exception(""+ i);
i++;                    b = crm.respondInvitation(mb, ipl.get(0), true);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.respondInvitation(mb, ipl.get(1), false);
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 1 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 3 )) throw new Exception(""+ i);
i++;
i++;                    mb = crm.logIn("boyenen@gmail.com",  "password");
i++;                    if(BuildConfig.DEBUG &&!(  mb != null )) throw new Exception(""+ i);
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 6 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 0 )) throw new Exception(""+ i);
i++;                    pml = crm.getMemberList(pl.get(0));
i++;                    if(BuildConfig.DEBUG &&!(  pml != null && pml.size() == 2 )) throw new Exception(""+ i);
i++;                    ipml = crm.getInvitingMemberList(pl.get(0));
i++;                    if(BuildConfig.DEBUG &&!(  ipml != null && ipml.size() == 1 )) throw new Exception(""+ i);
i++;                    pml = crm.getMemberList(pl.get(1));
i++;                    if(BuildConfig.DEBUG &&!(  pml != null && pml.size() == 1 )) throw new Exception(""+ i);
i++;                    ipml = crm.getInvitingMemberList(pl.get(1));
i++;                    if(BuildConfig.DEBUG &&!(  ipml != null && ipml.size() == 0 )) throw new Exception(""+ i);
i++;                    pml = crm.getMemberList(pl.get(2));
i++;                    if(BuildConfig.DEBUG &&!(  pml != null && pml.size() == 1 )) throw new Exception(""+ i);
i++;                    ipml = crm.getInvitingMemberList(pl.get(2));
i++;                    if(BuildConfig.DEBUG &&!(  ipml != null && ipml.size() == 1 )) throw new Exception(""+ i);
i++;
i++;                    pj = pl.get(0);
i++;                    mml = crm.getMeetingMinutesList(pj);
i++;                    if(BuildConfig.DEBUG &&!(  mml != null && mml.size() == 0 )) throw new Exception(""+ i);
i++;                    b = crm.createMeetingMinutes(pj, new MeetingMinutes(0, pj.getPjID(), new Date(),new MeetingMinutesContent(new Date(), "Here", "You", "Me", "OBJ", null, "A", "I", null)));
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    b = crm.createMeetingMinutes(pj, new MeetingMinutes(0, pj.getPjID(), new Date(),new MeetingMinutesContent(new Date(), "there", "Youtoo", "MeTwo", "OBJ2",new ArrayList<String>(), "A22", "22I", new ArrayList<ActionItem>())));
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;                    ArrayList<String> pcl = new ArrayList<String>();
i++;                    pcl.add("hi my name is Joe");
i++;                    pcl.add("I ain't a man");
i++;                    pcl.add("I am a boy");
i++;                    ArrayList<ActionItem> acl = new ArrayList<ActionItem>();
i++;                    acl.add(new ActionItem(1, "hi1","my name4", new java.sql.Date(1400000000), "OnGoing",""));
i++;                    acl.add(new ActionItem(5, "hi2","my name5", new java.sql.Date(1500000000), "Suspend",""));
i++;                    acl.add(new ActionItem(10, "hi2","my name6", new java.sql.Date(1600000000), "Colsed",""));
i++;                    b = crm.createMeetingMinutes(pj, new MeetingMinutes(0, pj.getPjID(), new Date(),new MeetingMinutesContent(new Date(), "there", "Youtoo", "MeTwo", "OBJ2", pcl, "A22", "22I", acl)));
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;
i++;                    mb = crm.logIn("this is an email address",  "1234567890!@#$%^&*()");
i++;                    if(BuildConfig.DEBUG &&!(  mb != null )) throw new Exception(""+ i);
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 1 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 3 )) throw new Exception(""+ i);
i++;                    pj = pl.get(0);
i++;                    pj.getPjDeadline().setTime(1300000000);
i++;                    pj = new Project(pj.getPjID(), pj.getPjName()+"yayaya", pj.getPjGoal(), pj.getPjManagerID()+1111,pj.getPjManager(),pj.getPjDeadline());
i++;                    b = crm.modifyProject(mb, pj);
i++;                    if(BuildConfig.DEBUG &&!(  !b )) throw new Exception(""+ i);
i++;
i++;                    mml = crm.getMeetingMinutesList(pj);
i++;                    if(BuildConfig.DEBUG &&!(  mml != null && mml.size() == 3 )) throw new Exception(""+ i);
i++;                    mml.get(0).setObjective(mml.get(0).getObjective() + "\nwhat's the problem?");
i++;                    b = crm.modifyMeetingMinutes(mml.get(0));
i++;                    if(BuildConfig.DEBUG &&!(  b )) throw new Exception(""+ i);
i++;
i++;                    mb = crm.logIn("boyenen@gmail.com",  "password");
i++;                    if(BuildConfig.DEBUG &&!(  mb != null )) throw new Exception(""+ i);
i++;                    pl = crm.getProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  pl != null && pl.size() == 6 )) throw new Exception(""+ i);
i++;                    ipl = crm.getInvitingProjectList(mb);
i++;                    if(BuildConfig.DEBUG &&!(  ipl != null && ipl.size() == 0 )) throw new Exception(""+ i);
i++;                    mml = crm.getMeetingMinutesList(pj);
i++;                    if(BuildConfig.DEBUG &&!(  mml != null && mml.size() == 3 )) throw new Exception(""+ i);

                           ret = "Totally done!";
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
