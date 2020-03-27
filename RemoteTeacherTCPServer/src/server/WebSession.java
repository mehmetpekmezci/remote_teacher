package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WebSession {
public static String URL_RTEACHER_SESSION_VALIDATOR_FOR_TCP_SERVER= "http://www.rteacher.com/rteacher/rteacher/php/sessionValidatorForTCPServer.php";;
public String sessionId;
public String attendee;
public String teacher;

public static ArrayList<WebSession> getAvailableRSessionsList() throws IOException {
	String url=System.getProperty("URL_RTEACHER_SESSION_VALIDATOR_FOR_TCP_SERVER");
	if(url!=null && url.trim().length()>0)URL_RTEACHER_SESSION_VALIDATOR_FOR_TCP_SERVER=url;
	URL rteacher = new URL(URL_RTEACHER_SESSION_VALIDATOR_FOR_TCP_SERVER);
	URLConnection urlConnection = rteacher.openConnection();
	ArrayList<WebSession> rSessions = new ArrayList<WebSession>();
	BufferedReader in = new BufferedReader(new InputStreamReader(
			urlConnection.getInputStream()));
	String inputLine;
	while ((inputLine = in.readLine()) != null) {
		//System.out.println(inputLine);
		if (inputLine.contains("#")) {
			String[] inputArray = inputLine.split("#");
			WebSession rSession = new WebSession();
			rSession.sessionId = inputArray[0].trim();
			rSession.teacher = inputArray[1].trim();
			rSession.attendee = inputArray[2].trim();
			rSessions.add(rSession);
		}
	}

	in.close();
	return rSessions;
}
}