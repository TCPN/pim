package pim;


import java.util.ArrayList;

public interface ClientAPI {
    Member logIn(String userEmail, String userPassword) throws Exception;
    boolean createAccount(String userEmail, String userPassword) throws Exception;

    ArrayList<Project> getMemberProjectList(Member member) throws Exception;
    ArrayList<MeetingMinutes> getMeetingMinutesList(Project project) throws Exception;

    ArrayList<Project> getMemberInvitationList(Member member) throws Exception;
    boolean respondInvitation(Member member, Project project, Boolean accept) throws Exception;

    boolean createProject(Project newProject) throws Exception;
    boolean createProjectMemberList(ArrayList<String> email) throws Exception;
    boolean createMeetingMinutes(MeetingMinutes newMeetingMinutes) throws Exception;

    boolean modifyProject(Project project) throws Exception;
    boolean modifyMeetingMinutes(MeetingMinutes minutes) throws Exception;
}
