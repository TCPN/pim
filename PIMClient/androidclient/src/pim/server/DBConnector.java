package pim.server;
//package pim;

//############################## START OF DbConnector CLASS #########################################################################

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import pim.MeetingMinutes;
import pim.MeetingMinutesContent;
import pim.Member;
import pim.Project;
import pim.ProjectMember;


public class DbConnector {

    ////ATTRIBUTES FOR CONNECTION ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    Connection conn = null;
    String jdbcURL;
    String user;
    String password;
    String useUnicode;
    String characterEncoding;

    ////ATTRIBUTES FOR QUERY STATUS //////////////////////////////////////////////////////////////////////////////////////////////////////////
    int rowAffected;
    ////ATTRIBUTES FOR RETURN VALUE //////////////////////////////////////////////////////////////////////////////////////////////////////////
    boolean status;						// [02] Button Name: [Forget Password] --> DB return: TRUE ("email sent")
    // [11] Button Name: [Create New Member] --> DB return: TRUE ("member created")
    // [13] Button Name: [Update Member Setting] --> DB return: TRUE ("member updated")
    // [31]	Button Name: [Send Project Invitation] --> DB return: TRUE ("project invitation sent")
    // [33]	Button Name: [Decline Invitation] --> db return: TRUE ("invitation declined")
    //-------------------------------------------------------------------------------------------------------------------------------------
    Project pj;								// [21]	Button Name: [Create New Project] --> DB return: PROJECT OBJECT (new)
    // [23]	Button Name: [Update Project Setting] --> DB return: PROJECT OBJECT (updated)
    // [32]	Button Name: [Enter Invited Project Detail] --> DB return: PROJECT OBJECT
    //-------------------------------------------------------------------------------------------------------------------------------------
    MeetingMinutes mm;								// [42]	Button Name: [Enter MM View] --> DB return: MM OBJECT
    // [43]	Button Name: [Create New MM from New] --> DB return: MM OBJECT (new from blank)
    // [44]	Button Name: [Create New MM from Old] --> DB return: MM OBJECT (new from old)
    // [45]	Button Name: [Update Old MM from Old] --> DB return: MM OBJECT (updated old)
    //-------------------------------------------------------------------------------------------------------------------------------------
    Member mb;								// [12]	Button Name: [Enter Member Setting] --> DB return: MEMBER OBJECT
    //-------------------------------------------------------------------------------------------------------------------------------------
    ArrayList<ArrayList<Project>> pjCombinedList;		// [01]	Button Name: [Submit Email & Password] --> DB return: MIXED PROJECT OBJECT LIST
    //-------------------------------------------------------------------------------------------------------------------------------------
    ArrayList<MeetingMinutes> mmList;					// [34]	Button Name: [Accept Invitation] --> DB return: MM BOJECT LIST
    // [41]	Button Name: [Enter Timeline View] --> DB return: MM OBJECT LIST
    //-------------------------------------------------------------------------------------------------------------------------------------
    ArrayList<ProjectMember> pjmbList;				// [51]	Button Name: [Select Project Member] --> DB return: PJMB OBJECT LIST (PJMB = Project Member)
    //-------------------------------------------------------------------------------------------------------------------------------------

    ////CONSTRUCTOR //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DbConnector(String JdbcURL, String User, String Password /*, String UseUnicode, String CharacterEncoding*/) {
        this.jdbcURL = JdbcURL;		// jdbcURL = localhost:3306/pim
        this.user = User;			// User = root
        this.password = Password;	// Password = cliurcp
        //this.useUnicode = UseUnicode;
        //this.characterEncoding = CharacterEncoding;
    }
////METHODS FOR CONNECTION TO DB /////////////////////////////////////////////////////////////////////////////////////////////////////////	

    public void getConnection() /*throws ClassNotFoundException, SQLException*/ {

        try	{

            String jdbcDriverName = "com.mysql.jdbc.Driver";
            String connectionString = "jdbc:mysql://" + this.jdbcURL + "?user=" + this.user + "&password=" + this.password + "&useUnicode=true&characterEncoding=utf-8";
            Class.forName(jdbcDriverName);
            this.conn = DriverManager.getConnection(connectionString);
            //System.out.println("conn created !");
        }
        catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            System.out.println("[Warning] Connect to DB CANNOT be built");
        }
    }

    public void endConnection() /*throws SQLException*/ {

        try {
            this.conn.close();
            this.conn = null;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("[Warning] CANNOT close connection to DB correctly");
        }
    }

////-----------------------------------------------------------------// pjTable

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int createProject(String PJname, String PJgoal, String PJmanager, java.util.Date PJdeadline) /*throws SQLException*/ {
        int tempNum = 0;
        int last_inserted_pjID = -1;
        //int pjID = -1;
        try {
            String query = "INSERT INTO pjTable (pjName, pjGoal, pjManager, pjDeadline) VALUES (?, ?, ?, ?)";
            PreparedStatement pStm1 = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pStm1.setString(1, PJname);
            pStm1.setString(2, PJgoal);
            pStm1.setString(3, PJmanager);
            pStm1.setDate(4, (java.sql.Date)PJdeadline);
            pStm1.executeUpdate();

            ResultSet rs = pStm1.getGeneratedKeys();
            if(rs.next())
            {
                last_inserted_pjID = rs.getInt(1);
            }
			/*
			query = "SELECT MYSQL_INSERT_ID() FROM pjTable";
			PreparedStatement pStm2 = this.conn.prepareStatement(query);
			pjID = pStm2.executeUpdate();
			System.out.println("pjID = " + pjID);
			*/
            tempNum += 1;

        } catch (SQLException e) {
            return -1;
            //e.printStackTrace();
        }
        if (tempNum == 1){
            return last_inserted_pjID;
        } else {
            return -1;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int updateProject(int PJid, String PJname, String PJgoal, String PJmanager, java.sql.Date PJdeadline) /*throws SQLException*/ {

        int tempNum = 0;
        try {
            String query = "UPDATE pjTable SET pjName = ?, pjGoal = ?, pjManager = ?, pjDeadline = ? WHERE pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, PJname);
            pStm.setString(2, PJgoal);
            pStm.setString(3, PJmanager);
            pStm.setDate(4, PJdeadline);
            pStm.setInt(5, PJid);
            pStm.executeUpdate();

            tempNum += 1;
            System.out.println("renew " + tempNum + " rows in pjTable");
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (tempNum == 1) {
            return 1;
        }
        else {
            return -1;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public ResultSet getProjectAsResultSet(int PJid) /*throws SQLException*/ {

        int tempNum = 0;
        ResultSet rSet = null;
        try{
            String query = "SELECT * FROM pjTable WHERE pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();
            rSet = rs;
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        if (tempNum == 1){
            return rSet;
        } else {
            return null;
        }
    }


    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public ArrayList<Integer> getPJidList(int MBid) /*throws SQLException*/ {

        ArrayList<Integer> pjidList = new ArrayList<Integer>();
        int tempNum = 0;

        try {
            String query = "SELECT pjID FROM pjmbTable WHERE (mbID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MBid);
            ResultSet rs = pStm.executeQuery();

            while(rs.next()) {
                int pjID = rs.getInt("pjID");
                //System.out.println("inside while loop. pjID = " + pjID);
                pjidList.add(pjID);
            }
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        if (tempNum == 1){
            return pjidList;
        } else {
            return null;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public String getPjManager(int PJid) {

        int tempNum = 0;
        String mgr = null;
        try {
            String query = "SELECT pjManager from pjTable where pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();
            while (rs.next()){
                mgr = rs.getString("pjManager");
            }
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        if (tempNum == 1){
            return mgr;
        } else {
            return null;
        }
    }

////-----------------------------------------------------------------// mbTable

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int createMember(String MBemail, String MBpassword, String MBname) /*throws SQLException*/ {

        int tempNum = 0;
        int mbID = -13;
        int rowAffected = 0;
        try {
            String query = "INSERT INTO mbTable (mbEmail, mbPassword, mbName) VALUES (?,?,?)";
            PreparedStatement pStm1 = this.conn.prepareStatement(query);
            pStm1.setString(1, MBemail);
            pStm1.setString(2, MBpassword);
            pStm1.setString(3, MBname);
            //System.out.println("@pStm - " + pStm1);
            rowAffected = pStm1.executeUpdate();
			/*
			query = "SELECT mbID FROM mbTable WHERE (mbEmail = ? AND mbPassword = ?)";
			PreparedStatement pStm2 = this.conn.prepareStatement(query);
			pStm2.setString(1,  MBemail);
			pStm2.setString(2, MBpassword);
			System.out.println("@pStm - " + pStm2);
			mbID = pStm2.executeUpdate();
			*/
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (tempNum == 1){
            return rowAffected;
        }
        else {
            return -1;
        }

    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int updateMember(int MBid, String MBemail, String MBpassword, String MBname) /*throws SQLException*/ {

        int rowAffected = 0;

        try {
            String query = "UPDATE mbTable SET mbEmail = ?, mbPassword = ?, mbName = ? WHERE mbID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBemail);
            pStm.setString(2, MBpassword);
            pStm.setString(3, MBname);
            pStm.setInt(4, MBid);
            rowAffected = pStm.executeUpdate();

        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        return rowAffected;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public ResultSet getMemberAsResultSet(int MBid) /*throws SQLException*/ {

        int tempNum = 0;
        ResultSet rSet = null;

        try {
            String query = "SELECT * FROM mbTable WHERE mbID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MBid);
            ResultSet rs = pStm.executeQuery();
            rSet = rs;

            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        if (tempNum == 1){
            //System.out.println("@return mb");
            //return mb;
            return rSet;
        } else {
            //System.out.println("@return null");
            return null;
        }
    }


    public Member getMemberAsObject(int MBid) //throws SQLException
    {
        int tempNum = 0;
        ResultSet rSet = null;

        try {
            String query = "SELECT * FROM mbTable WHERE mbID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MBid);
            ResultSet rs = pStm.executeQuery();
            int mbID = 0;
            String mbEmail = "";
            String mbPassword = "";
            String mbName = "";
            Timestamp mbLastModified = null;
            while(rs.next()) {
                mbID = rs.getInt("mbID");
                mbEmail = rs.getString("mbEmail");
                mbPassword = rs.getString("mbPassword");
                mbName = rs.getString("mbName");
                mbLastModified = rs.getTimestamp("mbLastModified");

            }
            //System.out.println("before");
            //this.mb = new Member(mbID, mbEmail, mbPassword, mbName, mbLastModified);
            mbID = mb.getMbID();
            //System.out.println(mbID);
            //System.out.println("after");
            //--mbID = mb.getMbID();
            //--mbEmail = mb.getMbEmail();
            //--mbPassword = mb.getMbPassword();
            //--mbName = mb.getMbName();
            //--mbLastModified = mb.getMbLastModified();
            //--System.out.println("@MB.mbID = " + mbID);
            //--System.out.println("@MB.mbEmail = " + mbEmail);
            //--System.out.println("@MB.mbPassword = " + mbPassword);
            //--System.out.println("@MB.mbName = " + mbName);
            //--System.out.println("@MB.mbLastModified = " + mbLastModified);

            tempNum += 1;

        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        if (tempNum == 1){
            System.out.println("@return mb");
            //return mb;
            return mb;
        } else {
            System.out.println("@return null");
            return null;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int getMemberID(String USERemail, String USERpassword) /*throws SQLException*/ {

        int mbID = 0;
        int tempNum = 0;
        try {
            String query = "SELECT mbID FROM mbTable WHERE (mbEmail = ? AND mbPassword = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, USERemail);
            pStm.setString(2, USERpassword);
            ResultSet rs = pStm.executeQuery();
            while(rs.next()) {
                mbID = rs.getInt("mbID");
            }
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (mbID == 0){
            return -1;
        } else {
            return mbID;
        }
    }


    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int getMemberIDbyEmail(String MBemail) /*throws SQLException*/ {

        int mbID = 0;
        int tempNum = 0;
        try {
            String query = "SELECT mbID FROM mbTable WHERE (mbEmail = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBemail);
            ResultSet rs = pStm.executeQuery();
            while(rs.next()) {
                mbID = rs.getInt("mbID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (mbID == 0){
            return -1;
        } else {
            return mbID;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int getMemberIDbyName(String MBname) /*throws SQLException*/ {

        int mbID = 0;
        int tempNum = 0;
        try {
            String query = "SELECT mbID FROM mbTable WHERE (mbName = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBname);
            ResultSet rs = pStm.executeQuery();
            while(rs.next()) {
                mbID = rs.getInt("mbID");
            }
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (tempNum == 1){
            return mbID;
        } else {
            return -1;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public String getPassword(String MBemail) /*throws SQLException*/ {

        String mbPassword = "";
        int tempNum = 0;
        try {
            String query = "SELECT mbPassword FROM mbTable WHERE mbEmail = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBemail);
            ResultSet rs = pStm.executeQuery();
            while(rs.next()) {
                mbPassword = rs.getString("mbPassword");
            }
            tempNum += 1;
        }
        catch (SQLException e) {
            return null;
            //e.printStackTrace();
        }
        if (tempNum == 1){
            return mbPassword;
        } else {
            return null;
        }
    }


    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public boolean matchEmail(String USERemail) {

        int tempNum = 0;
        try {
            String query = "SELECT mbEmail FROM mbTable WHERE (mbEmail = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, USERemail);
            ResultSet rs = pStm.executeQuery();
            if ( USERemail.equals(rs.getString("mbEmail")) ) {
                tempNum += 1;
            }
        }
        catch (SQLException e) {
            return false;
            //e.printStackTrace();
        }
        if (tempNum == 1){
            return true;
        } else {
            return false;
        }
    }

////-----------------------------------------------------------------// pjmbTable

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public int createPJMB(int PJid, int MBid, String PJMBrole, int PJMBisActive, int PJMBisManager) /*throws SQLException*/ {

        int tempNum = 0;
        try{
            String query = "INSERT INTO pjmbTable (pjID, mbID, pjmbRole, PJMBisActive, pjmbIsManager) VALUES (?,?,?,?,?)";
            PreparedStatement pStm = this.conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            pStm.setString(3, PJMBrole);
            pStm.setInt(4, PJMBisActive);
            pStm.setInt(5, PJMBisManager);
            pStm.executeUpdate();
            tempNum += 1;
        }
        catch (SQLException e) {
            return -1;
            //e.printStackTrace();
        }
        if (tempNum == 1){
            return 1;
        } else {
            return -1;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int updatePJMB(int MBid, int PJid, String PJMBrole) throws SQLException {
        String query = "UPDATE pjmbTable SET pjmbRole = ? WHERE pjID = ? and mbID = ?";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setString(1, PJMBrole);
        pStm.setInt(2, PJid);
        pStm.setInt(3, MBid);
        int rowAffected = pStm.executeUpdate();

        return rowAffected;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int updatePJMBActivity(int MBid, int PJid, int PJMBActivity) throws SQLException {
        String query = "UPDATE pjmbTable SET pjmbIsActive = ? WHERE pjID = ? and mbID = ?";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, PJMBActivity);
        pStm.setInt(2, PJid);
        pStm.setInt(3, MBid);
        //System.out.println();
        int rowAffected = pStm.executeUpdate();

        return rowAffected;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public String getPJMB(int PJid, int MBid) throws SQLException {
        String query = "SELECT pjmbRole FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, PJid);
        pStm.setInt(2, MBid);
        ResultSet rs = pStm.executeQuery();

        String pjmbRole = "";
        //int pjmbActivity = 0;
        while(rs.next()) {
            pjmbRole = rs.getString("pjmbRole");
            //pjmbActivity = rs.getInt("pjmbIsActive");
        }

        return pjmbRole;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int isManager(int PJid, int MBid) throws SQLException {
        String query = "SELECT pjmbRole FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, PJid);
        pStm.setInt(2, MBid);
        ResultSet rs = pStm.executeQuery();

        int isManager = -1;
        isManager = rs.getInt("pjmbIsManager");
        return isManager;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public int isActive(int PJid, int MBid) throws SQLException {
        String query = "SELECT pjmbIsActive FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, PJid);
        pStm.setInt(2, MBid);
        ResultSet rs = pStm.executeQuery();

        int isActive = -1;
        while(rs.next()) {
            isActive = rs.getInt("pjmbIsActive");
        }

        return isActive;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public ArrayList<Integer> getPjmbIdList(int PJid) throws SQLException {
        String query = "SELECT mbID FROM pjmbTable WHERE (pjID = ?)";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, PJid);
        ResultSet rs = pStm.executeQuery();

        ArrayList<Integer> pjmbIdList = new ArrayList<Integer>();
        while(rs.next()) {
            pjmbIdList.add(rs.getInt("mbID"));
        }
        return pjmbIdList;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public ArrayList<Integer> getPjIdList(int MBid) throws SQLException {
        String query = "SELECT pjID FROM pjmbTable WHERE (mbID = ?)";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, MBid);
        ResultSet rs = pStm.executeQuery();

        ArrayList<Integer> pjIdList = new ArrayList<Integer>();
        while(rs.next()) {
            pjIdList.add(rs.getInt("pjID"));
        }
        return pjIdList;
    }

    
    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int deletePJMB(int PJid, int MBid) /*throws SQLException*/ {

        int tempNum = 0;
        try{
            String query = "DELETE FROM pjmbTable where (pjID = ? and mmID = ?)";
            PreparedStatement pStm = this.conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            pStm.executeUpdate();
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (tempNum == 1){
            return 1;
        } else {
            return -1;
        }
    }

////------------------------------------------------

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public int updateManager(int PJid, int MBid_old, int MBid_new, String PJmanager_new) /*throws SQLException*/ {

        int tempNum = 0;
        try {
            String query = "UPDATE pjTable SET pjManager = ? WHERE pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, PJmanager_new);
            pStm.setInt(2,  PJid);

            int rowAffected = pStm.executeUpdate();

            query = "UPDATE pjmbTable SET pjmbIsManager = ? WHERE pjID = ? and mbID = ?";
            pStm = conn.prepareStatement(query);
            pStm.setInt(1, 1);
            pStm.setInt(2, PJid);
            pStm.setInt(3, MBid_new);
            rowAffected += pStm.executeUpdate();

            query = "UPDATE pjmbTable SET pjmbIsManager = ? WHERE pjID = ? and mbID = ?";
            pStm = conn.prepareStatement(query);
            pStm.setInt(1, 0);
            pStm.setInt(2, PJid);
            pStm.setInt(3, MBid_old);
            pStm.executeUpdate();

            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (tempNum == 1){
            return 1;
        } else {
            return -1;
        }
    }



////-----------------------------------------------------------------// mbTable

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public int createMM(int PJid, Object mmContent) {

        int last_inserted_mmID = -1;
        int tempNum = 0;
        try {
            String query = "INSERT INTO mmTable (pjID, mmContent) VALUE (?,?)";
            PreparedStatement pStm = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pStm.setInt(1, PJid);
            pStm.setObject(2, mmContent);
            pStm.executeUpdate();

            ResultSet rs = pStm.getGeneratedKeys();

            if(rs.next()) { last_inserted_mmID = rs.getInt(1); }
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }

        if (tempNum == 1){ return last_inserted_mmID; }
        else { return -1; }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public boolean updateMM(int MMid, Object mmContent) {

        boolean result = false;
        int tempNum = 0;
        try {
            String query = "UPDATE mmTable SET mmContent = (?) WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setObject(1, mmContent);
            pStm.setInt(2, MMid);
            tempNum = pStm.executeUpdate();

            if (tempNum == 1) { result = true; }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
        return result;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public MeetingMinutesContent getMMcontent(int MMid) {
        MeetingMinutesContent mmblob = null;
        try {
            String query = "SELECT mmContent FROM mmTable WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MMid);
            pStm.executeQuery();

            ResultSet rs = pStm.executeQuery();
            while(rs.next()){
                InputStream is = rs.getBlob("mmContent").getBinaryStream();
                ObjectInputStream ois = null;
                try{
                    ois = new ObjectInputStream(is);
                } catch (IOException ex)
                {return null;} finally
                {

                    try{
                        mmblob = (MeetingMinutesContent)ois.readObject();
                    } catch(IOException ex)
                    {return null;}catch(ClassNotFoundException ex){return null;}
                }
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        return mmblob;
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public java.util.Date getMMLastModified(int MMid) {
        java.util.Date date=null;
        try {
            String query = "SELECT mmLastModified FROM mmTable WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MMid);
            pStm.executeQuery();

            ResultSet rs = pStm.executeQuery();
            while(rs.next()){
                date = rs.getDate("mmLastModified");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        if(date==null)
            return null;

        return date;
    }


    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    public boolean verifyMMcontent(int MMid) {
        int getpjid=-1;
        try {
            String query = "SELECT pjID FROM mmTable WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MMid);
            pStm.executeQuery();

            ResultSet rs = pStm.executeQuery();
            while(rs.next()){
                getpjid = rs.getInt("pjID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
        if(getpjid>0)
            return true;
        else
            return false;
    }


    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    public ArrayList<Integer> getMMidList(int PJid) {

        int tempNum = 0;
        ArrayList<Integer> MMidList = new ArrayList<Integer>();

        try {
            String query = "SELECT mmID FROM mmTable WHERE (pjID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();

            while(rs.next()) { MMidList.add(rs.getInt("mmID")); }
            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }

        if (tempNum == 1) { return MMidList; }
        else { return null; }
    }




//--------------UTILITIES	

    public boolean matchManager(String mgrOld, String mgrNew){
        String OLD = mgrOld;
        String NEW = mgrNew;
        boolean matchResult = OLD.equals(NEW);
        return matchResult;
    }

    // GET LAST INSERT ID
    public int getLastInsertID(String tableName) {

        int pjID = -1;
        int tempNum = 0;
        try {
            String query = "SELECT MYSQL_INSERT_ID() FROM " + tableName;
            PreparedStatement pStm2 = this.conn.prepareStatement(query);
            ResultSet rs = pStm2.executeQuery();

            tempNum += 1;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
        if (tempNum == 1){
            return pjID;
        } else {
            return -1;
        }

    }


    public java.sql.Date convertUitlDateToSqlDate(java.util.Date UtilDate) {

        java.util.Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = new java.util.Date(); // your util date
        cal.setTime(utilDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime()); // your sql date
        //System.out.println("utilDate:" + utilDate);
        //System.out.println("sqlDate:" + sqlDate);
        return sqlDate;
    }

}


