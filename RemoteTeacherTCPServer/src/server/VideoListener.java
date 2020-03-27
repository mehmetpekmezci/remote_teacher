package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;


public class VideoListener  extends Thread{

	RTeacherSession rTeacherSession;
	SocketChannel socketChannel;
	SocketChannel secondSocketChannel;
	String[] socketChannelOccupiers=new String[2];
    boolean isLoggedIn=false;
    boolean loginProcess=false;
    Server server;
    private String email;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	boolean running=true;
	ByteBuffer byteBuffer = ByteBuffer.allocate(10000);

	
	public VideoListener(Server server , SocketChannel socketChannel){
		this.server=server;
		this.socketChannel=socketChannel;
	}
	
	public void run() {
		while (running) {
			try{
					if (socketChannel!= null&& socketChannel.isConnected()&& socketChannel.isOpen() && !loginProcess) {
						byteBuffer.clear();
						if (!isLoggedIn) {
							StringBuffer receivedMessage=new StringBuffer();
							while (socketChannel.read(byteBuffer) > 0) {
								byteBuffer.flip();
					//			System.out.println("VideoLietener:"+byteBuffer.limit());
								Charset charset = Charset.forName("UTF-8");
								CharsetDecoder decoder = charset.newDecoder();
								CharBuffer charBuffer = decoder.decode(byteBuffer);
								receivedMessage.append(charBuffer);
							}
							if(receivedMessage.length()>0)login(receivedMessage.toString());
						}else{
							if((socketChannel.read(byteBuffer) > 0) )routeTheMessage(byteBuffer);
						}
					}
					Thread.sleep(100);
				} catch (IOException e) {
					loginProcess=false;
					try {
						socketChannel .close();
						secondSocketChannel.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					socketChannel =null;
					secondSocketChannel=null;
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

	}

	public void login(String inputMessage) throws IOException {
//		System.out.println("Video Lİstener Login Message is  "+inputMessage);
		loginProcess=true;
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
	//			System.out.println("Video Listener RT sessionid="+rTeacherSession.getSessionId()+" my sessionid="+sessionId);
				if(rTeacherSession.getSessionId().equals(sessionId)){
		//			System.out.println("Video Listener RT session is found :"+rTeacherSession.getSessionId());
					this.rTeacherSession = rTeacherSession;
				}
		}

	  VideoListener existingVideoListener=null;
	  for(VideoListener videoListener:rTeacherSession.getVideoListeners()){
		  if(videoListener.getEmail().equals(email)){
			  existingVideoListener=videoListener;
		  }
	  }
	  
	 
	  
	  
	   if(existingVideoListener!=null ){
		   existingVideoListener.setSecondSocketChannel(socketChannel);
		   socketChannel.write(ByteBuffer.wrap(new String("OK").getBytes()));
		   running=false;
	   }else if (isLoggedIn) {
			rTeacherSession.getVideoListeners().add(this);
			socketChannel.write(ByteBuffer.wrap(new String("OK").getBytes()));
		} else {
			socketChannel.close();
			running = false;
		}
	   
	   loginProcess=false;
	}

	public void routeTheMessage(ByteBuffer byteBuffer) throws IOException {

		for (VideoListener videoListener : rTeacherSession.getVideoListeners()) {
//			System.out.println("FROM "+email+"  rTeacherSession.getVideoListeners().size()="+ rTeacherSession.getVideoListeners().size());
//			if(videoListener==null)System.out.println("Video Listener is NULL");
//			else System.out.println("Video Listener name is "+ videoListener.email);
			
			if (videoListener !=null && videoListener != this && videoListener.isRunning() &&  videoListener.isLoggedIn()) {
//				System.out.println("FROM "+email+ " to "+videoListener.getEmail()+" byteBuffer.position="+byteBuffer.position());
				    byteBuffer.flip();
				    if(videoListener.getSocketChannelOccupiers()[0]==null){
				    	videoListener.getSocketChannelOccupiers()[0]=email;
				    }
				    if(videoListener.getSocketChannelOccupiers()[0].equals(email)){
				    	if(videoListener.getSocketChannel()!=null && videoListener.getSocketChannel().isConnected()){
				    		try{
				    			videoListener.getSocketChannel().write(byteBuffer);
//				    			System.out.println("First Channel "+email+ " to "+videoListener.getEmail()+" byteBuffer.position="+byteBuffer.position());
				    		}catch(IOException e){
				    			e.printStackTrace();
				    			videoListener.getSocketChannel().close();
				    			videoListener.setSocketChannel(null);				    			
				    		}
				    		
				    		
				    	}
				    	
				    }else{
				    	if(videoListener.getSecondSocketChannel()!=null && videoListener.getSecondSocketChannel().isConnected()){
				    		try{
				    		videoListener.getSecondSocketChannel().write(byteBuffer);
//				    		System.out.println("Second Channel "+email+ " to "+videoListener.getEmail()+" byteBuffer.position="+byteBuffer.position());
				    		}catch(IOException e){
				    			e.printStackTrace();
				    			videoListener.getSecondSocketChannel().close();
				    			videoListener.setSecondSocketChannel(null);				    			
				    		}
				    		
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

	public RTeacherSession getrTeacherSession() {
		return rTeacherSession;
	}

	public void setrTeacherSession(RTeacherSession rTeacherSession) {
		this.rTeacherSession = rTeacherSession;
	}
	
	public void disconnect() {
		try {
			socketChannel.close();
			socketChannel=null;
			secondSocketChannel.close();
			secondSocketChannel=null;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	public SocketChannel getSecondSocketChannel() {
		return secondSocketChannel;
	}

	public void setSecondSocketChannel(SocketChannel secondSocketChannel) {
		this.secondSocketChannel = secondSocketChannel;
	}

	public String[] getSocketChannelOccupiers() {
		return socketChannelOccupiers;
	}

	public void setSocketChannelOccupiers(String[] socketChannelOccupiers) {
		this.socketChannelOccupiers = socketChannelOccupiers;
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
	
}
