package server;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class VideoRecorder extends Thread{

	private RTeacherSession rTeacherSession;
	private String sessionDirectoryName;
	private int i=0;
	public VideoRecorder(RTeacherSession rTeacherSession){
		this.rTeacherSession=rTeacherSession;
		start();
	}
	public void run(){
		while(rTeacherSession.getMainApplication()==null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sessionDirectoryName=System.getProperty("user.home")+"/rteacher/"+rTeacherSession.getMainApplication().getApplicationContextVariables().getSessionId();
		File sessionDirectory=new File(sessionDirectoryName);
		if(!sessionDirectory.exists())sessionDirectory.mkdirs();
		while(rTeacherSession.isRunning()){
			String s="";
			if(i<10000)s="0"+s;
			if(i<1000)s="0"+s;
			if(i<100) s="0"+s;
			if(i<10)s="0"+s;
			s=s+i;
			File outputfile = new File(sessionDirectoryName+"/"+s+".jpeg");
			 try {
				ImageIO.write(this.rTeacherSession.getMainApplication().getApplicationContextVariables().getCurrentPage().getBufferedImage(), "jpg", outputfile);
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
