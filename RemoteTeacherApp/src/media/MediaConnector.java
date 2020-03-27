package media;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import userInterface.ApplicationContextVariables;

public class MediaConnector extends Thread{

	ApplicationContextVariables applicationContextVariables;
    private VideoSender videoSender;
	private VideoReceiver videoReceiver;
	private VideoReceiver videoReceiver2;

	private AudioSenderReceiver audioSenderReceiver;
	
	private SocketChannel videoSocketChannel2;
	private SocketChannel videoSocketChannel;
	private SocketChannel audioSocketChannel;

	public MediaConnector(ApplicationContextVariables applicationContextVariables){
	   this.applicationContextVariables=applicationContextVariables;
		if(!applicationContextVariables.getUserEmail().equals("recorder@localhost"))this.start();
	}
	
	public void run(){
//		System.out.println("Constants.getAudioPort()="+applicationContextVariables.getAudioPort());
		
		while(applicationContextVariables.getPageList().size()==0){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//waiting for networkconnector to login, other wise rteacherSession object would not be set , so would not be found, so got a null pointer exception.
			
		}
//		System.out.println("INITIALISING MEDIA NETWORK");
		
		initNetwork();
		videoSender=new VideoSender(videoSocketChannel,applicationContextVariables);
		videoReceiver=new VideoReceiver(videoSocketChannel,applicationContextVariables);
		videoReceiver2=new VideoReceiver(videoSocketChannel2,applicationContextVariables);
		audioSenderReceiver=new AudioSenderReceiver(audioSocketChannel,this);
		

		videoReceiver.setCameraPanel(applicationContextVariables.getMainFrame().getLeftPanel().getCameraPanel1());
		videoReceiver2.setCameraPanel(applicationContextVariables.getMainFrame().getLeftPanel().getCameraPanel2());

		
		videoSender.start();
		videoReceiver.start();
		videoReceiver2.start();
		videoReceiver2.setPreviousVideoListener(videoReceiver);
		audioSenderReceiver.start();
		
		
		
		
	}
	
	
	public VideoSender getVideoSender() {
		return videoSender;
	}

	public void setVideoSender(VideoSender videoSender) {
		this.videoSender = videoSender;
	}

	public VideoReceiver getVideoReceiver() {
		return videoReceiver;
	}

	public void setVideoReceiver(VideoReceiver videoReceiver) {
		this.videoReceiver = videoReceiver;
	}


	public SocketChannel getVideoSocketChannel() {
		return videoSocketChannel;
	}

	public void setVideoSocketChannel(SocketChannel videoSocketChannel) {
		this.videoSocketChannel = videoSocketChannel;
	}

	public SocketChannel getAudioSocketChannel() {
		return audioSocketChannel;
	}

	public void setAudioSocketChannel(SocketChannel audioSocketChannel) {
		this.audioSocketChannel = audioSocketChannel;
	}

	public void initNetwork(){
		 int videoPort=applicationContextVariables.getVideoPort();
		 int audioPort=applicationContextVariables.getAudioPort();
		 
		   try{
	           videoSocketChannel=SocketChannel.open();
	           videoSocketChannel2=SocketChannel.open();
				   InetSocketAddress videoIinetSocketAddress= new InetSocketAddress(applicationContextVariables.getServerIp(), videoPort);
				   videoSocketChannel.connect(videoIinetSocketAddress);
				   videoSocketChannel2.connect(videoIinetSocketAddress);
				   videoSocketChannel.configureBlocking(true);
				   videoSocketChannel2.configureBlocking(true);

		   }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can not connect to server  "+applicationContextVariables.getServerIp()+" on port "+videoPort+", \n  the reason is  : \n  "+ e.getMessage(), "Network Problem", JOptionPane.INFORMATION_MESSAGE);
		  }

		   try{
		       audioSocketChannel=SocketChannel.open();
 		       InetSocketAddress audioIinetSocketAddress= new InetSocketAddress(applicationContextVariables.getServerIp(), audioPort);
			   audioSocketChannel.connect(audioIinetSocketAddress);
			   audioSocketChannel.configureBlocking(false);
			  //blocking false olunca read'de beklemiyor, yoksa read de current thread block oluyor.
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can not connect to server  "+applicationContextVariables.getServerIp()+" on port "+audioPort+", \n  the reason is  : \n  "+ e.getMessage(), "Network Problem", JOptionPane.INFORMATION_MESSAGE);
		  }

	}
	public VideoReceiver getVideoReceiver2() {
		return videoReceiver2;
	}

	public void setVideoReceiver2(VideoReceiver videoReceiver2) {
		this.videoReceiver2 = videoReceiver2;
	}

	public SocketChannel getVideoSocketChannel2() {
		return videoSocketChannel2;
	}

	public void setVideoSocketChannel2(SocketChannel videoSocketChannel2) {
		this.videoSocketChannel2 = videoSocketChannel2;
	}

	public ApplicationContextVariables getApplicationContextVariables() {
		return applicationContextVariables;
	}

	public void setApplicationContextVariables(
			ApplicationContextVariables applicationContextVariables) {
		this.applicationContextVariables = applicationContextVariables;
	}
	
	public byte[] getLoginMessage(){
		return new String("##LOGIN "+applicationContextVariables.getSessionId()+" "+applicationContextVariables.getUserEmail()+"::::").getBytes();
	}

}
