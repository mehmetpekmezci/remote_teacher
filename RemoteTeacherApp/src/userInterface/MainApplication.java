/*
 * Created on Dec 8, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface;
import media.MediaConnector;
import network.NetworkConnector;

/**
 * @author mpekmezci
 */
public class MainApplication {
	ApplicationContextVariables applicationContextVariables=new ApplicationContextVariables();
	
	public static void main(String[] args)  {
		MainApplication mainApplication=new MainApplication(args[0],args[1],args[2],args[3]);
	}
	public MainApplication(String serverIp,String sessionId,String userEmail, String teacherEmail){
		applicationContextVariables.setServerIp(serverIp);
		applicationContextVariables.setSessionId(sessionId);
		applicationContextVariables.setUserEmail(userEmail);
		applicationContextVariables.setTeacherEmail(teacherEmail);
		applicationContextVariables.setTeacher(applicationContextVariables.getUserEmail().equals(applicationContextVariables.getTeacherEmail()));
		applicationContextVariables.setNetworkConnector(new NetworkConnector(applicationContextVariables));
		applicationContextVariables.setMediaConnector(new MediaConnector(applicationContextVariables));
		applicationContextVariables.setMainFrame(new MainFrame(applicationContextVariables));
		
		
	}
	public ApplicationContextVariables getApplicationContextVariables() {
		return applicationContextVariables;
	}
	public void setApplicationContextVariables(
			ApplicationContextVariables applicationContextVariables) {
		this.applicationContextVariables = applicationContextVariables;
	}
}
