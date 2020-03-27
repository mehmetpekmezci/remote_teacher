package network;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SenderThread implements Runnable {
	public boolean running=true;
	public NetworkConnector networkConnector=null;
   public SenderThread(NetworkConnector networkConnector){
	   this.networkConnector=networkConnector;
   }
	public void run() {
        System.out.println("Inside SenderThread");
        boolean anyMessageSent=false;
        long pingLastTime=0;
        try {
            while (running) {
            	anyMessageSent=false;
                while (networkConnector.getSendQueueForStringCommands().size() > 0) {
                	String message=networkConnector.getSendQueueForStringCommands().get(0);
   //             	System.out.println("Sending message:##"+message+"::::");
                	ByteBuffer bb=ByteBuffer.wrap(("##"+message+"::::").getBytes());
                	networkConnector.getSocketChannel().write(bb);
                	synchronized (networkConnector.getSendQueueForStringCommands()) {
                		networkConnector.getSendQueueForStringCommands().remove(0);
					}
                	anyMessageSent=true;
                }

                
                
                Thread.sleep(100);
                
            	if(!anyMessageSent&& networkConnector.getSendQueueForStringCommands().size()==0 &&  System.currentTimeMillis()-pingLastTime>1000){
                	networkConnector.getSocketChannel().write(ByteBuffer.wrap("##PING::::".getBytes()));
                	pingLastTime=System.currentTimeMillis();
            	}

            }
        } catch (Exception e) {
            e.printStackTrace();
			JOptionPane.showMessageDialog(null,  "Cannot send messages , the reason is : \n"+e.getMessage(), "Network Problem", JOptionPane.INFORMATION_MESSAGE);
        } 
	}
}
