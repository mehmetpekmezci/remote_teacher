package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;

public class MobileListener extends Thread {
	Server server;
	SocketChannel socketChannel ;
	ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
	boolean running=true;
	public MobileListener(Server server,SocketChannel socketChannel) {
		this.server = server;
		this.socketChannel = socketChannel;
		try {
			socketChannel.configureBlocking(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("mobile listener is running...");
		while(running){
			try{
					if (socketChannel!= null&& socketChannel.isConnected()&& socketChannel.isOpen()) {
						
						byteBuffer.clear();
							StringBuffer receivedMessage=new StringBuffer();
							while (socketChannel.read(byteBuffer) > 0) {
								byteBuffer.flip();
								
								//System.out.println("Audio Listener:"+byteBuffer.limit());
								Charset charset = Charset.forName("UTF-8");
								CharsetDecoder decoder = charset.newDecoder();
								CharBuffer charBuffer = decoder.decode(byteBuffer);
								receivedMessage.append(charBuffer);
							}
							if(receivedMessage.length()>0)processmessage(receivedMessage.toString());
					}

					Thread.sleep(100);
					
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

	public void processmessage(String inputMessage) throws IOException {
		running=false;
		System.out.println("inputMessage="+inputMessage);
		if(inputMessage==null || inputMessage.trim().length()==0){
			return;
		}
		String[] messageFields = inputMessage.split(" ");
		String sessionId =messageFields[0].trim();
		String imageid=messageFields[1].trim();
		String width=messageFields[2].trim();
		String height=messageFields[3].trim();
//		String width="640";
//		String height="480";
		System.out.println("sessionId="+sessionId+"  imageid="+imageid);

	  for (RTeacherSession rTeacherSession : server.getrTeacherSessions()) {
				if(rTeacherSession.getSessionId().equals(sessionId)){
					 String nameTime="";
					 for(int i=0;i<rTeacherSession.getCommands().size();i++){
						 String command=rTeacherSession.getCommands().get(i);
						 if(command.contains("ADD_PAGE")){
							 nameTime=command.replaceAll(".*##ADD_PAGE@@","").replaceAll("@@.*","").replaceAll("::::","");
						 }
					 }
					 inputMessage="##PAGE@@"+nameTime+"@@"+"CREATE@@"+imageid+"@@0@@-1@@true@@0@@"+width+"@@0@@"+height+"@@"+ "IMG@@jpg::::";
					 rTeacherSession.getCommands().add(inputMessage);
						for (ClientListener clientListener : rTeacherSession.getClientListeners()) {
							System.out.println("writing to "+clientListener.getEmail()); 
								clientListener.getSocketChannel().write(ByteBuffer.wrap(inputMessage.getBytes()));
								
						}	
                      System.out.println(inputMessage);
                      break;
				}
		}
	  
	}

	

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

}
