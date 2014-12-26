package pim;


import java.util.ArrayList;

public interface ClientAPI {
	
	Member logIn(String userEmail, String userPassword) throws Exception;
	
	//new function
	boolean forgetPassword(String userEmail) throws Exception;
																//new parameter
	boolean createAccount(String userEmail, String userPassword, String userName) throws Exception; 
	
    ArrayList<Project> getMemberProjectList(Member member) throws Exception;
    ArrayList<MeetingMinutes> getMeetingMinutesList(Project project) throws Exception;

	// ok
    ArrayList<Project> getInvitationListForMember(Member member) throws Exception;
   
	boolean respondInvitation(Member member, Project project, Boolean accept) throws Exception;

	// create project
    boolean createProject(Project newProject, ArrayList<String> emailList) throws Exception;
	
	// get projectMember list
	ArrayList<ProjectMember> getProjectMemberList(Project project) throws Exception;
	ArrayList<ProjectMember> getProjectInvitationList(Project project) throws Exception;
	
	// create Meeting Minutes
								//need this parameter
	boolean createMeetingMinutes(Project project, MeetingMinutes newMeetingMinutes) throws Exception;
	
    // modify project and meeting minutes
// if you're going to use this, please new a new instance of Project, 
// ths new manager ID and Name can get from ProjectMember List
	boolean modifyProject(Project project) throws Exception;
	
    boolean modifyMeetingMinutes(MeetingMinutes minutes) throws Exception;
	
}
