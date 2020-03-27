package server;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;

import userInterface.MainApplication;

public class ClientListener implements Runnable {
	
	private SocketChannel socketChannel;
	private ArrayList<RTeacherSession> rTeacherSessions;
	private boolean isLoggedIn = false;
	private boolean loginFailed = false;
	private String sessionId;
	private String email;
	private String teacher;
	private boolean running = true;
	ByteBuffer buf = ByteBuffer.allocate(1024);
	StringBuffer receivedMessage = new StringBuffer();
	RTeacherSession relatedRTeacherSession = null;

	public ClientListener(SocketChannel socketChannel,
			ArrayList<RTeacherSession> rTeacherSessions) {
		this.socketChannel = socketChannel;
		this.rTeacherSessions = rTeacherSessions;
	//	System.out.println("rteacherSessions.size:"+rTeacherSessions.size());
	}

	private void disconnect() throws IOException {
		System.out.println("Disconnecting "+email+"  from address "+socketChannel.getRemoteAddress()+"  with sessionID "+relatedRTeacherSession.getSessionId());
		running = false;
		socketChannel.close();
		relatedRTeacherSession.getClientListeners().remove(this);
		ArrayList<String> loginLogoutMessagesToRemove=new ArrayList<String>();

		for(int i=0;i<relatedRTeacherSession.getCommands().size();i++){
			String command=relatedRTeacherSession.getCommands().get(i);
			if(command.equals("##LOGGEDIN_USER "+email+"::::"))	loginLogoutMessagesToRemove.add(command);
			if(command.equals("##LOGGEDOUT_USER "+email+"::::"))	loginLogoutMessagesToRemove.add(command);
			
		}
		synchronized (relatedRTeacherSession.getCommands()) {
			for(int i=0;i<loginLogoutMessagesToRemove.size();i++){
//				System.out.println("Removing="+loginLogoutMessagesToRemove.get(i));
				relatedRTeacherSession.getCommands().remove(loginLogoutMessagesToRemove.get(i));
			}
		}
//		try{
//			if(email.equals(teacher)){
//				relatedRTeacherSession.getVideoServer().getVideoTeacherServer().disconnect();
//				relatedRTeacherSession.getAudioServer().getAudioTeacherServer().disconnect();
//			}else{
//				relatedRTeacherSession.getVideoServer().getVideoStudentServer().disconnect();
//				relatedRTeacherSession.getAudioServer().getAudioStudentServer().disconnect();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		routeTheMessage("##LOGGEDOUT_USER "+email+"::::");
		
		if(!relatedRTeacherSession.isRunning()){
			synchronized (rTeacherSessions) {
				rTeacherSessions.remove(relatedRTeacherSession);
			}
		}
		
		ArrayList removeList=new ArrayList();
		for(int i=0;i<relatedRTeacherSession.getVideoListeners().size();i++){
			if(relatedRTeacherSession.getVideoListeners().get(i).getEmail().equals(email)){
				removeList.add(relatedRTeacherSession.getVideoListeners().get(i));
				relatedRTeacherSession.getVideoListeners().get(i).setRunning(false);
				relatedRTeacherSession.getVideoListeners().get(i).disconnect();
			}
		}
		
		for(int i=0;i<removeList.size();i++){
			relatedRTeacherSession.getVideoListeners().remove(removeList.get(i));
		}
		
		removeList=new ArrayList();

		for(int i=0;i<relatedRTeacherSession.getAudioListeners().size();i++){
			if(relatedRTeacherSession.getAudioListeners().get(i).getEmail().equals(email)){
				removeList.add(relatedRTeacherSession.getAudioListeners().get(i));
				relatedRTeacherSession.getAudioListeners().get(i).setRunning(false);
				relatedRTeacherSession.getAudioListeners().get(i).disconnect();
			}
		}
		
		for(int i=0;i<removeList.size();i++){
			relatedRTeacherSession.getVideoListeners().remove(removeList.get(i));
		}

		System.out.println("Disconnected  "+email+"  from address "+socketChannel.getRemoteAddress()+"  with sessionID "+relatedRTeacherSession.getSessionId());
	}

	public void run() {
		boolean bufferIsRead=false;
		boolean anyMessageSent=false;
		while (running) {
			try {
				bufferIsRead=false;
				buf.clear();
				anyMessageSent=false;
				while (socketChannel.read(buf) > 0) {
					buf.flip();
					Charset charset = Charset.forName("UTF-8");
					CharsetDecoder decoder = charset.newDecoder();
					CharBuffer charBuffer = decoder.decode(buf);
					receivedMessage.append(charBuffer);
					bufferIsRead=true;
				}

				if (bufferIsRead && receivedMessage.length() > 0) {
					if (!isLoggedIn) {
						login(receivedMessage.toString());
					} else {
						anyMessageSent=routeTheMessage(receivedMessage.toString());
					}
					receivedMessage = new StringBuffer();
				}
			} catch (IOException e) {
				try {
					disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			if (!anyMessageSent&&socketChannel.isConnected()) {
				try {
					socketChannel.write(ByteBuffer.wrap("##PING::::".getBytes()));
				} catch (IOException e) {
					try {
						disconnect();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			} else if(!socketChannel.isConnected()){
				try {
					disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void login(String inputMessage) throws IOException {
		String remoteAddress=socketChannel.getRemoteAddress().toString();
		remoteAddress=remoteAddress.substring(1,remoteAddress.indexOf(':'));
		
	//	System.out.println("socketChannel.getRemoteAddress().toString()="+remoteAddress);
	//	System.out.println("LOGIN INPUT: " + inputMessage+"::::");
		if(inputMessage==null || inputMessage.trim().length()==0){
			socketChannel.write(ByteBuffer.wrap("##LOGIN ERROR::::".getBytes()));
			loginFailed = true;
			socketChannel.close();
			running = false;
			if(relatedRTeacherSession!=null)relatedRTeacherSession.getClientListeners().remove(this);
		}
		String[] messageFields = inputMessage.split(" ");
		ArrayList<WebSession> rSessions = WebSession.getAvailableRSessionsList();
		String sessionId = messageFields[1].trim();
		String email = messageFields[2].trim().replace("::::", "");
		String teacher = "";
		boolean found = false;

		for (int i = 0; i < rSessions.size(); i++) {
			WebSession rSession = rSessions.get(i);
			if (rSession.sessionId.trim().equals(sessionId.trim())){
				if(rSession.attendee.trim().equals(email.trim()) || 	rSession.teacher.trim().equals(email.trim())){
					found = true;
					teacher = rSession.teacher;
				}else if(email.trim().equals("recorder@localhost") && (remoteAddress.equals("localhost")|| remoteAddress.equals("127.0.0.1"))){
					found = true;
					teacher = rSession.teacher;
				}
			}
		}
					


		synchronized (rTeacherSessions) {
			//ArrayList rTeacherSessionsToBeRemoved=new ArrayList();
			
			for (RTeacherSession rTeacherSession : rTeacherSessions) {

		//		System.out.println("RT sessionid="+rTeacherSession.getSessionId()+" my sessionid="+sessionId);
				if(rTeacherSession.getSessionId().equals(sessionId)){
		//			System.out.println("RT session is found :"+rTeacherSession.getSessionId());
					relatedRTeacherSession = rTeacherSession;
				}
				
			}
			//for(Object rTeacherSession :rTeacherSessionsToBeRemoved)rTeacherSessions.remove(rTeacherSession);
		}

		if (found) {
			isLoggedIn = true;
			this.sessionId = sessionId;
			this.email = email;
			this.teacher = teacher;

			if (relatedRTeacherSession == null) {
	//			System.out.println("RELATED rteacherSession is null");
				RTeacherSession rTeacherSession = new RTeacherSession(sessionId,teacher);
				synchronized (rTeacherSessions) {
					rTeacherSessions.add(rTeacherSession);
				}
				relatedRTeacherSession = rTeacherSession;
				if(relatedRTeacherSession.getMainApplication()==null)relatedRTeacherSession.setMainApplication(new MainApplication("localhost", sessionId, "recorder@localhost",teacher));
			}
			
			if (!relatedRTeacherSession.getClientListeners().contains(this))
				relatedRTeacherSession.getClientListeners().add(this);

			ArrayList<String> loginLogoutMessagesToRemove=new ArrayList<String>();
			for(int i=0;i<relatedRTeacherSession.getCommands().size();i++){
				String command=relatedRTeacherSession.getCommands().get(i);
				if(command.equals("##LOGGEDIN_USER "+email+"::::"))	loginLogoutMessagesToRemove.add(command);
				if(command.equals("##LOGGEDOUT_USER "+email+"::::"))	loginLogoutMessagesToRemove.add(command);
			}
			synchronized (relatedRTeacherSession.getCommands()) {
				for(int i=0;i<loginLogoutMessagesToRemove.size();i++){
	//				System.out.println("Removing="+loginLogoutMessagesToRemove.get(i));
					relatedRTeacherSession.getCommands().remove(loginLogoutMessagesToRemove.get(i));
				}
			}
			
			// send previous messages to client
			for (String command : relatedRTeacherSession.getCommands()) {
	//			System.out.println("Sending command from queue to  " + email + "  the message  = "+command);
				socketChannel.write(ByteBuffer.wrap(command.getBytes()));
			}
//			System.out.println("RTACHER SESSION COMMANDS  SIZE:"+relatedRTeacherSession.getCommands().size());
	//		System.out.println("RTACHER SESSION  SIZE:"+rTeacherSessions.size());
			
			if(!email.trim().equals("recorder@localhost"))routeTheMessage("##LOGGEDIN_USER "+email+"::::");
			System.out.println("LOGGED IN  "+email+"  from address "+socketChannel.getRemoteAddress()+"  with sessionID "+relatedRTeacherSession.getSessionId());
		} else {
			socketChannel.write(ByteBuffer.wrap("##LOGIN ERROR::::".getBytes()));
			loginFailed = true;
			socketChannel.close();
			running = false;
			if(relatedRTeacherSession!=null)relatedRTeacherSession.getClientListeners().remove(this);
			System.out.println("LOGIN UNSUCCESSFULL for   "+email+"  from address "+socketChannel.getRemoteAddress()+"  with sessionID "+relatedRTeacherSession.getSessionId());
		} 
	}

	public boolean routeTheMessage(String inputMessage) throws IOException {
		boolean anyMessageSent=false;
		if(inputMessage.contains("##END_SESSION::::")){
			relatedRTeacherSession.endSession();
			rTeacherSessions.remove(relatedRTeacherSession);
		}
	  
        if( inputMessage.equals("##PING::::") ||  inputMessage.equals("##PING::::##PING::::") ||  inputMessage.equals("##PING::::##PING::::##PING::::"))	return anyMessageSent;	
		if(inputMessage.endsWith("##PING::::"))inputMessage=inputMessage.substring(0,inputMessage.length()-10);
		if(inputMessage.startsWith("##PING::::"))inputMessage=inputMessage.substring(10);
		if(inputMessage.trim().length()==0)return anyMessageSent;

		
			
		synchronized (relatedRTeacherSession) {
			relatedRTeacherSession.getCommands().add(inputMessage);									
		}
		
		for (ClientListener clientListener : relatedRTeacherSession.getClientListeners()) {
			if (clientListener != this && clientListener.isLoggedIn()&& clientListener.getSessionId().equals(sessionId)) {
	//				System.out.println("Sending message from " + email + "  to "+ clientListener.getEmail() + "  the message  = "+ inputMessage);
					clientListener.getSocketChannel().write(ByteBuffer.wrap(inputMessage.getBytes()));
					anyMessageSent=true;		
				}
		}			
		return anyMessageSent;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isLoginFailed() {
		return loginFailed;
	}

	public void setLoginFailed(boolean loginFailed) {
		this.loginFailed = loginFailed;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}



	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
}


