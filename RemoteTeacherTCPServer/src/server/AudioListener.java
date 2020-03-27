package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;

public class AudioListener extends Thread {
	Server server;
	SocketChannel socketChannel ;
	ByteBuffer byteBuffer = ByteBuffer.allocate(500);
    RTeacherSession rTeacherSession;
    boolean isLoggedIn=false;
    boolean running=true;
    String email;
	public AudioListener(Server server,SocketChannel socketChannel) {
		this.server = server;
		this.socketChannel = socketChannel;
	}

	public void run() {
		while (running) {
			try{
					if (socketChannel!= null&& socketChannel.isConnected()&& socketChannel.isOpen()) {
						byteBuffer.clear();
						if (!isLoggedIn) {
							StringBuffer receivedMessage=new StringBuffer();
							while (socketChannel.read(byteBuffer) > 0) {
								byteBuffer.flip();
								//System.out.println("Audio Listener:"+byteBuffer.limit());
								Charset charset = Charset.forName("UTF-8");
								CharsetDecoder decoder = charset.newDecoder();
								CharBuffer charBuffer = decoder.decode(byteBuffer);
								receivedMessage.append(charBuffer);
							}
							if(receivedMessage.length()>0)login(receivedMessage.toString());
						}else{
							if(rTeacherSession!=null){
								if((socketChannel.read(byteBuffer) > 0) ){
								byte[] copyOFBytes = Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
								routeTheMessage(byteBuffer);
								rTeacherSession.recordAudio(copyOFBytes);
								}
							}

						}
					}

					Thread.sleep(50);
				} catch (IOException e) {
					try {
						socketChannel .close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					socketChannel =null;
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	}



	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	public void setByteBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	public RTeacherSession getrTeacherSession() {
		return rTeacherSession;
	}

	public void setrTeacherSession(RTeacherSession rTeacherSession) {
		this.rTeacherSession = rTeacherSession;
	}

	public void disconnect() {
		try {
			   socketChannel.close();
				socketChannel= null;
				
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	


	public void login(String inputMessage) throws IOException {
		if(inputMessage==null || inputMessage.trim().length()==0){
			socketChannel.close();
			running = false;
			if(rTeacherSession!=null)rTeacherSession.getAudioListeners().remove(this);
		}
		String[] messageFields = inputMessage.split(" ");
		ArrayList<WebSession> rSessions =WebSession.getAvailableRSessionsList();
		String sessionId = messageFields[1].trim();
		String email = messageFields[2].trim().replace("::::", "");

		for (int i = 0; i < rSessions.size(); i++) {
			WebSession rSession = rSessions.get(i);
			if (rSession.sessionId.trim().equals(sessionId.trim())){
				if(rSession.attendee.trim().equals(email.trim()) || 	rSession.teacher.trim().equals(email.trim())){
					isLoggedIn = true;
					this.email=email;
				}
			}
		}
	  for (RTeacherSession rTeacherSession : server.getrTeacherSessions()) {
//				System.out.println("Audio Listener RT sessionid="+rTeacherSession.getSessionId()+" my sessionid="+sessionId);
				if(rTeacherSession.getSessionId().equals(sessionId)){
	//				System.out.println("Audio Listener RT session is found :"+rTeacherSession.getSessionId());
					this.rTeacherSession = rTeacherSession;
				}
		}

		if (isLoggedIn) {
			rTeacherSession.getAudioListeners().add(this);
			socketChannel.write(ByteBuffer.wrap(new String("OK").getBytes()));
		} else {
			socketChannel.close();
			running = false;
		}
	}

	public void routeTheMessage(ByteBuffer byteBuffer) throws IOException {
		for (AudioListener audioListener : rTeacherSession.getAudioListeners()) {
			if (audioListener != this && audioListener.isLoggedIn()) {
				    byteBuffer.flip();
				    if(audioListener.getSocketChannel()!=null && audioListener.getSocketChannel().isConnected()){
				    	try{
				    audioListener.getSocketChannel().write(byteBuffer);
			    		}catch(IOException e){
			    			e.printStackTrace();
			    			audioListener.getSocketChannel().close();
			    			audioListener.setSocketChannel(null);				    			
			    		}
				    }
				}
		}			
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
