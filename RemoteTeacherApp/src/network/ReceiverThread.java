package network;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import javax.swing.JOptionPane;

import userInterface.ApplicationContextVariables;

public class ReceiverThread  implements Runnable{
	public boolean running=true;
	MessageHandler receivedMessageHandler;
	public NetworkConnector networkConnector=null;
	StringBuffer receivedMessage=new StringBuffer();
	ApplicationContextVariables applicationContextVariables;
   public ReceiverThread(NetworkConnector networkConnector,ApplicationContextVariables applicationContextVariables){
	   this.applicationContextVariables=applicationContextVariables;
	   this.networkConnector=networkConnector;
	   receivedMessageHandler=new MessageHandler(applicationContextVariables);
   }
	public void run() {
		boolean bufferIsRead=false;
        System.out.println("Inside receivemsg");
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            while (running) {
				buf.clear();
                while (networkConnector.getSocketChannel().read(buf) > 0) {
                	buf.flip();
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer charBuffer = decoder.decode(buf);
                    receivedMessage.append(charBuffer);
                    buf.flip();
                }
                if(receivedMessage.toString().trim().length()>0 && !receivedMessage.toString().equals("##PING")){
                	String receivedMessageString=receivedMessage.toString();
                	while(receivedMessageString.startsWith("##PING::::"))receivedMessageString=receivedMessageString.substring(10);
                	while(receivedMessageString.endsWith("##PING::::"))receivedMessageString=receivedMessageString.substring(0,receivedMessageString.length()-10);
                    if(receivedMessageString.trim().length()>0){
                    	String[] splittedMessages=receivedMessageString.split("##");
                    	for(int i=0;i<splittedMessages.length;i++){
                    		receivedMessageHandler.handleMessage(splittedMessages[i]);
                    	}
            			
                    }
                	receivedMessage=new StringBuffer();
                }

              	Thread.sleep(100);

            }
        } catch (Exception e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(null,  "Cannot receive messages , the reason is : \n"+e.getMessage(), "Network Problem", JOptionPane.INFORMATION_MESSAGE);
        } 
	}
}
