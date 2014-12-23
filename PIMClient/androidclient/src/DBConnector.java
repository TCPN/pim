
//package pim;

//############################## START OF DbConnector CLASS #########################################################################
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
//import java.sql.Date;		// 這裡要用 java.util.Date;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;	// for Date		// 這個現在不用了;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;		// for Serialization
import java.io.ObjectOutputStream;	// for Serialization
import java.io.ObjectInputStream;	// for Serialization
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.InputStream;
import java.io.IOException;


public class DbConnector {

////ATTRIBUTES FOR CONNECTION ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 0. 設定連線變數
	Connection conn = null;
	String jdbcURL;
	String user;
	String password;
	String useUnicode;
	String characterEncoding;
	
////ATTRIBUTES FOR QUERY STATUS //////////////////////////////////////////////////////////////////////////////////////////////////////////
	int rowAffected;
										// 以下這些上次討論 UI Flow 時用的，現在不知還有沒有用，因為 Barry 會用自己的方式設計 UI。
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
	List<List<Project>> pjCombinedList;		// [01]	Button Name: [Submit Email & Password] --> DB return: MIXED PROJECT OBJECT LIST
	//-------------------------------------------------------------------------------------------------------------------------------------
	List<MeetingMinutes> mmList;					// [34]	Button Name: [Accept Invitation] --> DB return: MM BOJECT LIST
										// [41]	Button Name: [Enter Timeline View] --> DB return: MM OBJECT LIST
	//-------------------------------------------------------------------------------------------------------------------------------------
	List<ProjectMember> pjmbList;				// [51]	Button Name: [Select Project Member] --> DB return: PJMB OBJECT LIST (PJMB = Project Member)
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
	
	// METHOD: 開啟［資料庫］連結
	public void getConnection() /*throws ClassNotFoundException, SQLException*/ {
		
		try	{
			
			// 1. 指定 JDBC 驅動類別名稱
			String jdbcDriverName = "com.mysql.jdbc.Driver";
			// 2. 指定本機 MySQL 的 JDBC URL (StudyInMotion 是我在mySQL中要建置的db名稱)
			String connectionString = "jdbc:mysql://" + this.jdbcURL + "?user=" + this.user + "&password=" + this.password + "&useUnicode=true&characterEncoding=utf-8"; 
			// 3. 載入 JDBC 驅動類別
			Class.forName(jdbcDriverName);			
			// 4. 取得一個 Connection 連結
			this.conn = DriverManager.getConnection(connectionString);
			//System.out.println("conn created !");
		}
		catch (ClassNotFoundException | SQLException e) {
			//e.printStackTrace();
			System.out.println("[Warning] Connect to DB CANNOT be built");
		}
	}
	
	// METHOD: 關閉［資料庫］連結
	public void endConnection() /*throws SQLException*/ {
	
		try {	
			// 5. 關閉 Connection 連結
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
	// METHOD: 在 pjTable 中 [新增] 一整列資料(row)
	///////////////////////////////////////////////////////
	public int createProject(String PJname, String PJgoal, String PJmanager, Date PJdeadline) /*throws SQLException*/ {						// 增 project
		
		int tempNum = 0;
		int last_inserted_pjID = -1;
		//int pjID = -1;
		try {
			// 新增 pj 一筆資料
			String query = "INSERT INTO pjTable (pjName, pjGoal, pjManager, pjDeadline) VALUES (?, ?, ?, ?)";
			PreparedStatement pStm1 = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pStm1.setString(1, PJname);
			pStm1.setString(2, PJgoal);
			pStm1.setString(3, PJmanager);
			pStm1.setDate(4, (java.sql.Date)PJdeadline);
			pStm1.executeUpdate();
			
			// 取得該筆資料的 pjID
			ResultSet rs = pStm1.getGeneratedKeys();
			if(rs.next())
            {
                last_inserted_pjID = rs.getInt(1);
            }
			/*
			// 取得該筆資料的 pjID
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
	// METHOD: 在 pjTable 中 [更新] 一整列資料(row)
	///////////////////////////////////////////////////////
	public int updateProject(int PJid, String PJname, String PJgoal, String PJmanager, java.sql.Date PJdeadline) /*throws SQLException*/ {			// 改 project
		
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
			System.out.println("已成功更新 " + tempNum + " 列資料於 pjTable 中");
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
	// METHOD: 在 pjTable 中 [取得] 一整列資料(row)
	///////////////////////////////////////////////////////
	public ResultSet getProjectAsResultSet(int PJid) /*throws SQLException*/ {												// 查 project
		
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
//		暫時先以 ResultSet 形式回傳，先不作成 PJ object
//		int pjID = 0;
//		String pjName = "";
//		String pjGoal = "";
//		String pjManager = "";
//		Date pjDeadline = null;
//		Timestamp pjLastModified = null;
//		while(rs.next()) {
//			pjID = rs.getInt("pjID");
//			pjName = rs.getString("pjName");
//			pjGoal = rs.getString("pjGoal");
//			pjManager = rs.getString("pjManager");
//			pjDeadline = rs.getDate("pjDeadline");
//			pjLastModified = rs.getTimestamp("pjLastModified");
//		}
//		PJ pj = new PJ(pjID, pjName, pjGoal, pjManager, pjDeadline, pjLastModified);
//		System.out.println("已成功從pjTable取得資料、建立PJ物件並回傳PJ物件。");
//		return pj;
	
	
	///////////////////////////////////////////////////////
	// METHOD: 在 pjTable 中 [取得] pjID
	///////////////////////////////////////////////////////	
	
	public List<Integer> getPJidList(int MBid) /*throws SQLException*/ {
		
		//建立一個用來存放 pjID 的 List
		List<Integer> pjidList = new ArrayList<Integer>();
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
	// METHOD: 在 pjTable 中 [取得] pjManager
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
	// METHOD: 在 mbTable 中 [新增] 一列資料(row)
	///////////////////////////////////////////////////////	
	public int createMember(String MBemail, String MBpassword, String MBname) /*throws SQLException*/ {						// 增 member
		
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
	// METHOD: 在 mbTable 中 [更新] 某一列資料(row)
	///////////////////////////////////////////////////////
	public int updateMember(int MBid, String MBemail, String MBpassword, String MBname) /*throws SQLException*/ {			// 改 member
		
		int rowAffected = 0;
		
		try {	
			String query = "UPDATE mbTable SET mbEmail = ?, mbPassword = ?, mbName = ? WHERE mbID = ?";
			PreparedStatement pStm = conn.prepareStatement(query);
			pStm.setString(1, MBemail);
			pStm.setString(2, MBpassword);
			pStm.setString(3, MBname);
			pStm.setInt(4, MBid);
			rowAffected = pStm.executeUpdate();
			
			//System.out.println("@DbConn裡: 已成功更新 " + rowAffected + " 列資料於 mbTable 中");
		}
		catch (SQLException e) {
			//e.printStackTrace();
			return -1;
		}
		return rowAffected;
	}		
			
	///////////////////////////////////////////////////////
	// METHOD: 在 mbTable 中 [取得] 某一列資料(row)
	///////////////////////////////////////////////////////
	public ResultSet getMemberAsResultSet(int MBid) /*throws SQLException*/ {												// 查 project
		
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
			this.mb = new Member(mbID, mbEmail, mbPassword, mbName, mbLastModified);
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
			
			//System.out.println("已成功從mbTable取得資料、建立MB物件並回傳MB物件。");
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
	// METHOD: 在 mbTable 中 [取得] mbID @ Login
	///////////////////////////////////////////////////////
	public int getMemberID(String USERemail, String USERpassword) /*throws SQLException*/ {			// 改 member
	
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
			//System.out.println("已成功更新 " + rowAffected + " 列資料於 pjTable 中");
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
	// METHOD: 在 mbTable 中 [取得] mbID by Email
	///////////////////////////////////////////////////////
	public int getMemberIDbyEmail(String MBemail) /*throws SQLException*/ {			// 改 member
		
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
	// METHOD: 在 mbTable 中 [取得] mbID by mbName
	///////////////////////////////////////////////////////
	public int getMemberIDbyName(String MBname) /*throws SQLException*/ {			// 改 member
	
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
			//System.out.println("已成功更新 " + rowAffected + " 列資料於 pjTable 中");
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
	// METHOD: 在 mbTable 中 [取得] mbPassword
	///////////////////////////////////////////////////////
	public String getPassword(String MBemail) /*throws SQLException*/ {			// 改 member
		
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
			//System.out.println("已成功更新 " + rowAffected + " 列資料於 pjTable 中");
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
	// METHOD: 在 pjTable 中 [比對userEmail 和 mbEmail 是否重複]
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
	// METHOD: 在pjmbTable 中 [新增] 一列資料(row)
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
	// METHOD: 在pjmbTable 中 [更新] pjmbRole
	///////////////////////////////////////////////////////
	public int updatePJMB(int MBid, int PJid, String PJMBrole) throws SQLException {			// 改 pjbm
		String query = "UPDATE pjmbTable SET pjmbRole = ? WHERE pjID = ? and mbID = ?";
		PreparedStatement pStm = conn.prepareStatement(query);
		pStm.setString(1, PJMBrole);
		pStm.setInt(2, PJid);
		pStm.setInt(3, MBid);
		int rowAffected = pStm.executeUpdate();
		
		//System.out.println("已成功更新 " + rowAffected + " 列資料於 pjmbTable 中");
		return rowAffected;
	}		
	
	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [更新] pjmbActivity
	///////////////////////////////////////////////////////
	public int updatePJMBActivity(int MBid, int PJid, int PJMBActivity) throws SQLException {			// 改 pjbm
		String query = "UPDATE pjmbTable SET pjmbIsActive = ? WHERE pjID = ? and mbID = ?";
		PreparedStatement pStm = conn.prepareStatement(query);
		pStm.setInt(1, PJMBActivity);
		pStm.setInt(2, PJid);
		pStm.setInt(3, MBid);
		//System.out.println();
		int rowAffected = pStm.executeUpdate();
		
		//System.out.println("已成功更新 " + rowAffected + " 列資料於 pjmbTable 中");
		return rowAffected;
	}		

	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [取得] pjmbRole
	///////////////////////////////////////////////////////
	public String getPJMB(int PJid, int MBid) throws SQLException {												// 查 project
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
		
		//System.out.println("已成功從mbTable取得資料、建立MB物件並回傳MB物件。");
		return pjmbRole;
	}	

	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [取得] pjmbIsManager
	///////////////////////////////////////////////////////
	public int isManager(int PJid, int MBid) throws SQLException {												// 查 project
		String query = "SELECT pjmbRole FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
		PreparedStatement pStm = conn.prepareStatement(query);
		pStm.setInt(1, PJid);
		pStm.setInt(2, MBid);
		ResultSet rs = pStm.executeQuery();
		
		int isManager = -1;		// 故意不用 0 或 1，以便與ＤＢ回傳值作區隔。
		while(rs.next()) {
			isManager = rs.getInt("pjmbIsManager");
		}
		
		//System.out.println("已成功從mbTable取得資料、建立MB物件並回傳MB物件。");
		return isManager;
	}
	
	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [取得] pjmbIsActive
	///////////////////////////////////////////////////////
	
	public int isActive(int PJid, int MBid) throws SQLException {												// 查 project
		String query = "SELECT pjmbIsActive FROM pjmbTable WHERE (pjID = ? AND mbID = ?)";
		PreparedStatement pStm = conn.prepareStatement(query);
		pStm.setInt(1, PJid);
		pStm.setInt(2, MBid);
		ResultSet rs = pStm.executeQuery();
		
		int isActive = -1;		// 故意不用 0 或 1，以便與ＤＢ回傳值作區隔。
		while(rs.next()) {
			isActive = rs.getInt("pjmbIsActive");
		}
		
		//System.out.println("已成功從mbTable取得資料、建立MB物件並回傳MB物件。");
		return isActive;
	}

	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [取得] pjmbIdList
	///////////////////////////////////////////////////////
	public List<Integer> getPjmbIdList(int PJid) throws SQLException {												// 查 project
		String query = "SELECT mbID FROM pjmbTable WHERE (pjID = ?)";
		PreparedStatement pStm = conn.prepareStatement(query);
		pStm.setInt(1, PJid);
		ResultSet rs = pStm.executeQuery();
		
		List<Integer> pjmbIdList = new ArrayList<Integer>(); 
		while(rs.next()) {
			pjmbIdList.add(rs.getInt("mbID"));
		}
		//System.out.println("已成功從mbTable取得資料、建立MB物件並回傳MB物件。");
		return pjmbIdList;
	}
	
	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [取得] project id list
	///////////////////////////////////////////////////////
	public List<Integer> getPjIdList(int MBid) throws SQLException {												// 查 project
		String query = "SELECT pjID FROM pjmbTable WHERE (mbID = ?)";
		PreparedStatement pStm = conn.prepareStatement(query);
		pStm.setInt(1, MBid);
		ResultSet rs = pStm.executeQuery();
		
		List<Integer> pjIdList = new ArrayList<Integer>(); 
		while(rs.next()) {
			pjIdList.add(rs.getInt("pjID"));
		}
		//System.out.println("已成功從mbTable取得資料、建立MB物件並回傳MB物件。");
		return pjIdList;
	}

	
	///////////////////////////////////////////////////////
	// METHOD: 在pjmbTable 中 [delete] project_member
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

////------------------------------------------------// pjTable & pjmbTable (同步作業)

	///////////////////////////////////////////////////////
	// METHOD: 在 pjTable & pjmbTable 中 [同步變更] project manager 身分
	///////////////////////////////////////////////////////
	public int updateManager(int PJid, int MBid_old, int MBid_new, String PJmanager_new) /*throws SQLException*/ {			// 改 pjbm
		
		int tempNum = 0;
		try {
			// 1. 先改 pjTable　中 pjManager 值
			String query = "UPDATE pjTable SET pjManager = ? WHERE pjID = ?";
			PreparedStatement pStm = conn.prepareStatement(query);
			pStm.setString(1, PJmanager_new);
			pStm.setInt(2,  PJid);
			
			int rowAffected = pStm.executeUpdate();
			//System.out.println("已成功更新 " + rowAffected + " 列資料於 pjTable 中");		
			
			// 2. 在 pjmbTable 中將 new manager　的 pjmbIsManager 的值設為 1  
			query = "UPDATE pjmbTable SET pjmbIsManager = ? WHERE pjID = ? and mbID = ?";
			pStm = conn.prepareStatement(query);
			pStm.setInt(1, 1);
			pStm.setInt(2, PJid);
			pStm.setInt(3, MBid_new);
			rowAffected += pStm.executeUpdate();
			
			// 3. 在 pjmbTable 中將 old manager　的 pjmbIsManager 的值設為 0  
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
	// METHOD: 在 mmTable 中 [新增] 一列資料(row)
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
		
		if (tempNum == 1){ return last_inserted_mmID; }		// 成功回傳新增的 mmID
		else { return -1; }									// 否則回傳 -1
	}
	
	///////////////////////////////////////////////////////
	// METHOD: 在 mmTable 中 [更新] 一列資料(row)
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
			
			if (tempNum == 1) { result = true; }	// 成功 update 1  筆資料，tempNum 才會變成 1
													// 如果回傳值大於 1，表示 DB 中有 double entry 的問題。
		}
		catch (SQLException e) {
			//e.printStackTrace();
			return false;
		}
		return result;		// 成功回傳 true，否則回傳 false
	}
	
	///////////////////////////////////////////////////////
	// METHOD: 在 mmTable 中 [取得] MMcontent
	///////////////////////////////////////////////////////	
	public Object getMMcontent(int MMid) {
		Object mmblob = null;
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
						mmblob = ois.readObject();
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
	// METHOD: 在 mmTable 中 [取得] mmLastModified
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
	// METHOD: 在 mmTable 中  verify MMcontent exists
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
	// METHOD: 在 mmTable 中 [取得] MM ID list
	///////////////////////////////////////////////////////	
	
	public List<Integer> getMMidList(int PJid) {
		
		int tempNum = 0;
		List<Integer> MMidList = new ArrayList<Integer>();
		
		try {
			String query = "SELECT mmID FROM mmTable WHERE (pjID = ?)";
			PreparedStatement pStm = conn.prepareStatement(query);
			pStm.setInt(1, PJid);
			ResultSet rs = pStm.executeQuery();
			
			while(rs.next()) { MMidList.add(rs.getInt("mmID")); }		
			tempNum += 1;							// 如果成功取得並加入List中， tempNum 變成 1
		}
		catch (SQLException e) {
			//e.printStackTrace();
			return null;
		}
		
		if (tempNum == 1) { return MMidList; }		// 如果成功，回傳 mm id list
		else { return null; }						// 如果失敗，回傳 null
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
		// 取得該筆資料的 pjID
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
	
	
	// METHOD 尚未完成
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
	
	

//	這是用 SimpleDateFormat 將 String 轉 Date 的寫法
//	//SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//	SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
//	String dateString = "31-08-1982 10:20:56";
//	try {
//		Date deadline = sdf.parse(dateString);
//	} catch (ParseException e1) {
//		e1.printStackTrace();
//	}
//	//System.out.println(deadline); //Tue Aug 31 10:20:56 SGT 1982
	
	
//	這是一段 Calendar 轉 Date 的寫法：		
//	Calendar cal = Calendar.getInstance();
//	cal.set(Calendar.YEAR, 2000);
//	cal.set(Calendar.MONTH, 0);
//	cal.set(Calendar.DAY_OF_MONTH, 1);
//	cal.set(Calendar.HOUR_OF_DAY, 1);
//	cal.set(Calendar.MINUTE, 1);
//	cal.set(Calendar.SECOND, 0);
//	cal.set(Calendar.MILLISECOND, 0);
//	stmt.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
	
	
//	這是另一種 Calendar 轉 Date 的寫法：		
//	Calendar C = new GregorianCalendar(1993,9,21);
//	Date DD = C.getTime();
//	System.out.println(DD);	
	
////---------------FOR PASSING OBJECTS--------------

	
	 
	
////------------------------------------------------// mmTable (Serialization)

	///////////////////////////////////////////////////////
	// METHOD: 將 MM 套裝成 Serializable 的 MM 物件
	///////////////////////////////////////////////////////
	
	// 1. 取得 MM 物件
	// 2. 
}


