package pim.server;
//package pim;

//############################## START OF DbConnector CLASS #########################################################################

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import pim_data.MeetingMinutesContent;
import pim_data.Member;
import pim_data.Project;
import pim_data.ProjectMember;


public class DBConnector {

    ////ATTRIBUTES FOR CONNECTION ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    Connection conn = null;
    String jdbcURL;
    String user;
    String password;
    String useUnicode;
    String characterEncoding;

    ////CONSTRUCTOR //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DBConnector(String JdbcURL, String User, String Password /*, String UseUnicode, String CharacterEncoding*/) throws Exception {
        this.jdbcURL = JdbcURL;		// jdbcURL = localhost:3306/pim
        this.user = User;			// User = root
        this.password = Password;	// Password = root
        //this.useUnicode = UseUnicode;
        //this.characterEncoding = CharacterEncoding;
		this.getConnection();
    }
////METHODS FOR CONNECTION TO DB /////////////////////////////////////////////////////////////////////////////////////////////////////////	

	public boolean isConnected(){return (this.conn != null);}
	
	public void getConnection() throws ClassNotFoundException, SQLException {

        try	{

            String jdbcDriverName = "com.mysql.jdbc.Driver";
            String connectionString = "jdbc:mysql://" + this.jdbcURL + "?user=" + this.user + "&password=" + this.password + "&useUnicode=true&characterEncoding=utf-8";
            Class.forName(jdbcDriverName);
            this.conn = DriverManager.getConnection(connectionString);
            if(this.conn != null)
				System.out.println("connection created !");
			else
				System.out.println("connect failed !");
        } catch (ClassNotFoundException e) {
            System.out.println("[Warning] DB driver not found");
            System.out.println(e);
            throw e;
        } catch(SQLException e) {
            //e.printStackTrace();
            System.out.println("[Warning] Connect to DB CANNOT be built");
			System.out.println(e);
            throw e;
            //throw e;
        }
    }

    public void endConnection() /*throws SQLException */{

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

    public int createProject(String PJname, String PJgoal, String PJmanager, java.util.Date PJdeadline) /*throws SQLException */{
        System.out.println("creating project...");
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
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("failed, return " + -1);
            //throw e;
            return -1;
            //e.printStackTrace();
        }
        System.out.println("success, return " + last_inserted_pjID);
        return last_inserted_pjID;
    }

    public int updateProject(int PJid, String PJname, String PJgoal, Date PJdeadline) /*throws SQLException */{
        System.out.println("updating project...");

        try {
            String query = "UPDATE pjTable SET pjName = ?, pjGoal = ?, pjDeadline = ? WHERE pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, PJname);
            pStm.setString(2, PJgoal);
            pStm.setDate(3, PJdeadline);
            pStm.setInt(4, PJid);
            pStm.executeUpdate();

        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            //throw e;
            return -1;
        }
        System.out.println("success");
        return 1;
    }

    public Project getProject(int pjID) /*throws SQLException */{
        System.out.println("retrieving project data...");

        String pjName = null;
        String pjGoal = null;
        String pjManager = null;
        java.sql.Date pjDeadline = null;
        try{
            String query = "SELECT * FROM pjTable WHERE pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, pjID);
            ResultSet rs = pStm.executeQuery();
            if(rs.next()) {
                pjName = rs.getString("pjName");
                pjGoal = rs.getString("pjGoal");
                pjManager = rs.getString("pjManager");
                pjDeadline = rs.getDate("pjDeadline");
            }

            Project pj = new Project(pjID, pjName, pjGoal, getPjManagerID(pjID), pjManager, pjDeadline);

            return pj;
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }

    public String getPjManager(int PJid) /*throws SQLException */{
        System.out.println("get project manager name ...");

        String mgr = null;
        try {
            String query = "SELECT pjManager from pjTable where pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();
            if (rs.next()){
                mgr = rs.getString("pjManager");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return null;
        }
        if(mgr == null){
            System.out.println("failed");
            return null;
        }
        else{
            System.out.println("success");
            return mgr;
        }
    }

    public int getPjManagerID(int PJid) /*throws SQLException */{
        System.out.println("get project manager ID ...");

        int mgrID = -1;
        try {
            String query = "SELECT mbID from pjmbTable where (pjID = ? and pjmbIsManager = 1)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();
            if (rs.next()){
                mgrID = rs.getInt("mbID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        if(mgrID == -1) {
            System.out.println("failed");
            return -1;
        }
        else {
            System.out.println("success");
            return mgrID;
        }
    }

////-----------------------------------------------------------------// mbTable

    public int createMember(String MBemail, String MBpassword, String MBname) /*throws SQLException */{
        System.out.println("creating member ...");

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
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        System.out.println("success");
        return rowAffected;

    }

    public int updateMember(int MBid, String MBemail, String MBpassword, String MBname) /*throws SQLException */{
        System.out.println("updating member ...");

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
            System.out.println("failed");
            return -1;
        }
        System.out.println("success");
        return rowAffected;
    }

    public ResultSet getMemberAsResultSet(int MBid) /*throws SQLException */{
        System.out.println("retrieving member data ...");

        ResultSet rSet = null;

        try {
            String query = "SELECT * FROM mbTable WHERE mbID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MBid);
            rSet = pStm.executeQuery();

        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return null;
        }
        System.out.println("success");
        return rSet;
    }

    public Member getMemberAsObject(int MBid) /*throws SQLException  */{
        System.out.println("get member ...");
        ResultSet rSet = null;

        Member mb = null;
        try {
            String query = "SELECT * FROM mbTable WHERE mbID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MBid);
            ResultSet rs = pStm.executeQuery();
            if(rs.next()) {
                mb = new Member(rs.getInt("mbID"), rs.getString("mbEmail"), rs.getString("mbName"));
            }
            System.out.println("return mb");
            //return mb;


        }
        catch (SQLException e) {
            //e.printStackTrace();
			System.out.println(e);
            System.out.println("failed");
            return null;
        }
        if(mb == null){
            System.out.println("failed");
            return null;
        }
        else {
            System.out.println("success");
            return mb;
        }
    }

    public int getMemberIDByLogIn(String USERemail, String USERpassword) /*throws SQLException */{
        System.out.println("verifying password ...");

        int mbID = -1;
        try {
            String query = "SELECT mbID FROM mbTable WHERE (mbEmail = ? AND mbPassword = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, USERemail);
            pStm.setString(2, USERpassword);
            ResultSet rs = pStm.executeQuery();
            if(rs.next()) {
                mbID = rs.getInt("mbID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        if (mbID == -1){
            System.out.println("failed");
            return -1;
        } else {
            System.out.println("success");
            return mbID;
        }
    }

    public int getMemberIDbyEmail(String MBemail) /*throws SQLException */{
        System.out.println("get ID by email ...");

        int mbID = -1;
        try {
            String query = "SELECT mbID FROM mbTable WHERE (mbEmail = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBemail);
            ResultSet rs = pStm.executeQuery();
            if(rs.next()) {
                mbID = rs.getInt("mbID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        if (mbID == -1){
            System.out.println("failed");
            return -1;
        } else {
            System.out.println("success");
            return mbID;
        }
    }

    public int getMemberIDbyName(String MBname) /*throws SQLException */{

        int mbID = 0;
        try {
            String query = "SELECT mbID FROM mbTable WHERE (mbName = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBname);
            ResultSet rs = pStm.executeQuery();
            while(rs.next()) {
                mbID = rs.getInt("mbID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        if (mbID == 0){
            System.out.println("failed");
            return -1;
        } else {
            System.out.println("success");
            return mbID;
        }
    }

    public String getPassword(String MBemail) /*throws SQLException */{
        System.out.println("get password by email ...");

        String mbPassword = null;
        try {
            String query = "SELECT mbPassword FROM mbTable WHERE mbEmail = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, MBemail);
            ResultSet rs = pStm.executeQuery();
            if(rs.next()) {
                mbPassword = rs.getString("mbPassword");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return null;
        }
        if (mbPassword == null){
            System.out.println("failed");
            return null;
        } else {
            System.out.println("success");
            return mbPassword;
        }
    }

    public boolean matchEmail(String USERemail) /*throws SQLException */{
        System.out.println("verifying email duplication...");

        try {
            String query = "SELECT mbEmail FROM mbTable WHERE (mbEmail = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, USERemail);
            ResultSet rs = pStm.executeQuery();
            if(!rs.next()) {
                System.out.println("failed");
                return false;
            }
            if ( USERemail.equals(rs.getString("mbEmail")) ) {
                System.out.println("success");
                return true;
            }
            else {
                System.out.println("success");
                return false;
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return false;
        }
    }

////-----------------------------------------------------------------// pjmbTable

    public int createPJMB(int PJid, int MBid, String PJMBrole, int PJMBisActive, int PJMBisManager) /*throws SQLException */{
        System.out.println("creating project member...");

        try{
            String query = "INSERT INTO pjmbTable (pjID, mbID, pjmbRole, PJMBisActive, pjmbIsManager) VALUES (?,?,?,?,?)";
            PreparedStatement pStm = this.conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            pStm.setString(3, PJMBrole);
            pStm.setInt(4, PJMBisActive);
            pStm.setInt(5, PJMBisManager);
            pStm.executeUpdate();
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        System.out.println("success");
        return 1;
    }

    public int updatePJMB(int MBid, int PJid, String PJMBrole) /*throws SQLException */{
        System.out.println("updating project member...");

        int rowAffected = -1;
        try {
            String query = "UPDATE pjmbTable SET pjmbRole = ? WHERE pjID = ? and mbID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, PJMBrole);
            pStm.setInt(2, PJid);
            pStm.setInt(3, MBid);
            rowAffected = pStm.executeUpdate();
        }catch(Exception e) {
            System.out.println("failed, " + e);
            return -1;
        }
        System.out.println("success");
        return rowAffected;
    }

    public int updatePJMBActivity(int MBid, int PJid, int PJMBActivity) /*throws SQLException */{
        System.out.println("updating project member Activity...");

        int rowAffected = -1;
        try {
        String query = "UPDATE pjmbTable SET pjmbIsActive = ? WHERE pjID = ? and mbID = ?";
        PreparedStatement pStm = conn.prepareStatement(query);
        pStm.setInt(1, PJMBActivity);
        pStm.setInt(2, PJid);
        pStm.setInt(3, MBid);
        //System.out.println();
        rowAffected = pStm.executeUpdate();
        }catch(Exception e) {
            System.out.println("failed, " + e);
            return -1;
        }

        System.out.println("success");
        return rowAffected;
    }

    public ProjectMember getPJMB(int PJid, int MBid) /*throws SQLException */{
        System.out.println("get project member...");

        ProjectMember pjmb = null;
        try {
            String query = "SELECT pjmbRole, pjmbIsActive, pjmbIsManager FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            ResultSet rs = pStm.executeQuery();

            String pjmbRole = null;
            int isactive = 0, ismanager = 0;
            if (rs.next()) {
                pjmbRole = rs.getString("pjmbRole");
                isactive = rs.getInt("pjmbIsActive");
                ismanager = rs.getInt("pjmbIsManager");
            }
            if (pjmbRole == null) {
                System.out.println("failed");
                return null;
            }
            Member mb = this.getMemberAsObject(MBid);
            if (mb == null) {
                System.out.println("failed, meber not exist");
                return null;
            }
            pjmb = new ProjectMember(PJid, MBid, mb.getMbName(), mb.getMbEmail(), pjmbRole, ismanager == 1, isactive == 1);
        }catch(Exception e) {
            System.out.println("failed, " + e);
            return null;
        }

        System.out.println("success");
        return pjmb;
    }

    public int isManager(int PJid, int MBid) /*throws SQLException */{
        System.out.println("get whether project member is Manager...");

        try {
            String query = "SELECT pjmbIsManager FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            ResultSet rs = pStm.executeQuery();

            int isManager = -1;
            if (rs.next()) {
                isManager = rs.getInt("pjmbIsManager");
                System.out.println("success");
                return isManager;
            }
        }catch(Exception e) {
            System.out.println("failed, " + e);
            return -1;
        }
        System.out.println("failed, this PJMB not exist");
        return -1;
    }

    public int isActive(int PJid, int MBid) /*throws SQLException */{
        System.out.println("get whether project member is Active...");

        try {
            String query = "SELECT pjmbIsActive FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            ResultSet rs = pStm.executeQuery();

            int isActive = -1;
            if (rs.next()) {
                isActive = rs.getInt("pjmbIsActive");
                System.out.println("success");
                return isActive;
            }
        }catch(Exception e) {
            System.out.println("failed, " + e);
            return -1;
        }
        System.out.println("failed, this PJMB not exist");
        return -1;

    }

    public ArrayList<Integer> getPjmbIdList(int PJid) /*throws SQLException */{
        System.out.println("get all Members of project...");

        ArrayList<Integer> pjmbIdList = null;
        try {
            String query = "SELECT mbID FROM pjmbTable WHERE (pjID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();

            pjmbIdList = new ArrayList<Integer>();
            while (rs.next()) {
                pjmbIdList.add(rs.getInt("mbID"));
            }
        }catch(Exception e) {
            System.out.println("failed, " + e);
            return null;
        }
        System.out.println("success");
        return pjmbIdList;
    }

    public ArrayList<Integer> getPJidList(int MBid, int MBactivity) /*throws SQLException */{
        System.out.println("get " + (MBactivity==1?"joined":"inviteing") + " projects of member:" + MBid +" ...");

        ArrayList<Integer> pjidList = new ArrayList<Integer>();

        try {
            String query = "SELECT pjID FROM pjmbTable WHERE (mbID = ? and pjmbIsActive = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MBid);
            pStm.setInt(2, MBactivity);
            ResultSet rs = pStm.executeQuery();

            while(rs.next()) {
                int pjID = rs.getInt("pjID");
                //System.out.println("inside while loop. pjID = " + pjID);
                pjidList.add(pjID);
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return null;
        }
        System.out.println("success");
        return pjidList;
    }

    public int deletePJMB(int PJid, int MBid) /*throws SQLException */{
        System.out.println("deleting project member...");

        try{
            String query = "DELETE FROM pjmbTable where (pjID = ? and mbID = ?)";
            PreparedStatement pStm = this.conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            pStm.setInt(2, MBid);
            pStm.executeUpdate();
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        System.out.println("success");
        return 1;
    }

////------------------------------------------------

    public int updateManager(int PJid, int MBid_old, int MBid_new) /*throws SQLException */{
        System.out.println("updating Manager of project...");

        Member newManager = this.getMemberAsObject(MBid_new);
        if(newManager == null) {
            System.out.println("Invalid new manager ID");
            System.out.println("failed");
            return -1;
        }

        try {
            String query = "UPDATE pjTable SET pjManager = ? WHERE pjID = ?";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setString(1, newManager.getMbName());
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

        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        System.out.println("success");
        return 1;
    }

////-----------------------------------------------------------------// mmTable

    public int createMM(int PJid, Object mmContent) /*throws SQLException */{
        System.out.println("creating MM...");

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

    public boolean updateMM(int MMid, Object mmContent) /*throws SQLException */{
        System.out.println("updating MM...");

        boolean result = false;
        int rowAffected = 0;
        try {
            String query = "UPDATE mmTable SET mmContent = (?) WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setObject(1, mmContent);
            pStm.setInt(2, MMid);
            rowAffected = pStm.executeUpdate();

        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println(e);
            System.out.println("failed");
            return false;
        }
        if (rowAffected == 1){
            System.out.println("success");
            return true;
        }else{
            System.out.println("failed, affected:" + rowAffected);
            return false;
        }
    }

    public MeetingMinutesContent getMMcontent(int MMid) /*throws SQLException */{
        System.out.println("get MM...");

        MeetingMinutesContent mmc = null;
        try {
            String query = "SELECT mmContent FROM mmTable WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MMid);
            pStm.executeQuery();

            ResultSet rs = pStm.executeQuery();
            if(rs.next()){
                InputStream is = rs.getBlob("mmContent").getBinaryStream();
                ObjectInputStream ois = null;
                try{
                    ois = new ObjectInputStream(is);
                } catch (IOException e){
                    System.out.println("failed,"+ e);
                    return null;
                }
                try{
                    mmc = (MeetingMinutesContent)ois.readObject();
                }
                catch(IOException e) {
                    System.out.println("failed,"+ e);
                    return null;
                }
                catch(ClassNotFoundException e) {
                    System.out.println("failed,"+ e);
                    return null;
                }
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed,"+ e);
            return null;
        }
        if(mmc == null) {
            System.out.println("failed");
            return null;
        } else {
            System.out.println("success");
            return mmc;
        }
    }

    public int getPJIDofMM(int MMid) /*throws SQLException */{
        System.out.println("get belonging project of MM...");

        int pjID = -1;
        try {
            String query = "SELECT pjID FROM mmTable WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MMid);
            pStm.executeQuery();

            ResultSet rs = pStm.executeQuery();
            if(rs.next()){
                pjID = rs.getInt("pjID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed");
            return -1;
        }
        if(pjID == -1) {
            System.out.println("failed");
            return -1;
        }
        else {
            System.out.println("success");
            return pjID;
        }
    }

    public java.util.Date getMMLastModified(int MMid) /*throws SQLException */{
        System.out.println("get MM last modified time...");

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
            System.out.println("failed, " + e);
            return null;
        }
        if(date==null) {
            System.out.println("failed");
            return null;
        }else {
            System.out.println("success");
            return date;
        }
    }

    public boolean verifyMMcontent(int MMid) /*throws SQLException */{
        System.out.println("verifying MM belonging...");

        int getpjid = -1;
        try {
            String query = "SELECT pjID FROM mmTable WHERE (mmID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, MMid);
            pStm.executeQuery();

            ResultSet rs = pStm.executeQuery();
            if(rs.next()){
                getpjid = rs.getInt("pjID");
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed, " + e);
            return false;
        }
        System.out.println("success");
        if(getpjid>0) {
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<Integer> getMMidList(int PJid) /*throws SQLException */{
        System.out.println("get MM ID List...");

        ArrayList<Integer> MMidList = new ArrayList<Integer>();

        try {
            String query = "SELECT mmID FROM mmTable WHERE (pjID = ?)";
            PreparedStatement pStm = conn.prepareStatement(query);
            pStm.setInt(1, PJid);
            ResultSet rs = pStm.executeQuery();

            while(rs.next()) {
                MMidList.add(rs.getInt("mmID"));
            }
        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("failed, " + e);
            return null;
        }
        System.out.println("success");
        return MMidList;
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


