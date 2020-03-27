/*
 * Created on Dec 5, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package network;

import java.awt.Image;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import userInterface.ApplicationContextVariables;

/**
 * @author maya
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NetworkConnector  {



	ApplicationContextVariables applicationContextVariables=null;
   public SocketChannel socketChannel = null;
   public InetSocketAddress inetSocketAddress = null;
   public SenderThread senderThread = null;
   public ReceiverThread receiverThread = null;
   public ArrayList<String> sendQueueForStringCommands=new ArrayList<String>();
   public  int PORT=2007;
   private MessageSender messageSender;
   
   public NetworkConnector(ApplicationContextVariables applicationContextVariables) {
	   this.applicationContextVariables=applicationContextVariables;
	   messageSender=new MessageSender(sendQueueForStringCommands,applicationContextVariables);
	   try{
	        socketChannel=SocketChannel.open();
	   }catch(IOException e){
		   e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can not open socket channel , the reason is  : \n  "+ e.getMessage(), "Network Problem", JOptionPane.INFORMATION_MESSAGE);
	   }
	   inetSocketAddress= new InetSocketAddress(applicationContextVariables.getServerIp(), PORT);
	   try {
		socketChannel.connect(inetSocketAddress);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Can not connect to server  "+applicationContextVariables.getServerIp()+" on port "+PORT+", \n  the reason is  : \n  "+ e.getMessage(), "Network Problem", JOptionPane.INFORMATION_MESSAGE);

	}
	   try {
		socketChannel.configureBlocking(false);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Can not configure non-blocking mode  ", "Network Problem", JOptionPane.INFORMATION_MESSAGE);
	}
	   sendQueueForStringCommands.add("LOGIN "+applicationContextVariables.getSessionId()+" "+applicationContextVariables.getUserEmail());
	   try{
	   receiverThread=new ReceiverThread(this,applicationContextVariables);
	   senderThread=new SenderThread(this);
	   new Thread(receiverThread).start();
	   new Thread(senderThread).start();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
   }

public MessageSender getMessageSender() {
	return messageSender;
}

public void setMessageSender(MessageSender messageSender) {
	this.messageSender = messageSender;
}

public SocketChannel getSocketChannel() {
	return socketChannel;
}

public void setSocketChannel(SocketChannel socketChannel) {
	this.socketChannel = socketChannel;
}

public InetSocketAddress getInetSocketAddress() {
	return inetSocketAddress;
}

public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
	this.inetSocketAddress = inetSocketAddress;
}

public SenderThread getSenderThread() {
	return senderThread;
}

public void setSenderThread(SenderThread senderThread) {
	this.senderThread = senderThread;
}

public ReceiverThread getReceiverThread() {
	return receiverThread;
}

public void setReceiverThread(ReceiverThread receiverThread) {
	this.receiverThread = receiverThread;
}

public ArrayList<String> getSendQueueForStringCommands() {
	return sendQueueForStringCommands;
}

public void setSendQueueForStringCommands(
		ArrayList<String> sendQueueForStringCommands) {
	this.sendQueueForStringCommands = sendQueueForStringCommands;
}


public ApplicationContextVariables getApplicationContextVariables() {
	return applicationContextVariables;
}
}
