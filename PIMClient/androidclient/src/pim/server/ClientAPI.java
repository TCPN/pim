package pim.server;
import pim.* ;
 
import java.util.ArrayList;
 
public interface ClientAPI {
    
	//Account related
    Member logIn(String userEmail, String userPassword) throws Exception;
    Boolean forgetPassword(String userEmail) throws Exception;
    Boolean createAccount(String userEmail, String userPassword, String userName) throws Exception;
    
    //Read related
    ArrayList<Project> getMemberProjectList(Member member) throws Exception;
    ArrayList<MeetingMinutes> getMeetingMinutesList(Project project) throws Exception;
    
    //Invitation related
    ArrayList<Project> getInvitationListForMember(Member member) throws Exception;
    Boolean respondInvitation(Member member, Project project, Boolean acceptance) throws Exception;
 
    //should have param "Member"
    Boolean createProject(Member member, Project newProject, ArrayList<String> emailList) throws Exception;
     
    // get projectMember list
    ArrayList<ProjectMember> getProjectMemberList(Project project) throws Exception;
    ArrayList<ProjectMember> getProjectInvitationList(Project project) throws Exception;
     
    // create Meeting Minutes
                                //need this parameter
    Boolean createMeetingMinutes(Project project, MeetingMinutes newMeetingMinutes) throws Exception;
     
    // modify project and meeting minutes
    // if you're going to use this, please new a new instance of Project,
    // ths new manager ID and Name can get from ProjectMember List
    // Backend needs member to verified
    Boolean modifyProject(Member member, Project project) throws Exception;
     
    Boolean modifyMeetingMinutes(MeetingMinutes minutes) throws Exception;
     
}