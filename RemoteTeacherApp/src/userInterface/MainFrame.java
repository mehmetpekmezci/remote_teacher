package userInterface;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import network.MessageSender;

public class MainFrame extends JFrame {
	LeftPanel leftPanel;
	DrawingArea drawingArea=null;
   public ApplicationContextVariables applicationContextVariables;
   public boolean isTeacher=false;
	public MainFrame( ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
		isTeacher=applicationContextVariables.isTeacher();
       drawingArea=new DrawingArea(applicationContextVariables);
       add(drawingArea);
	   leftPanel=new LeftPanel(applicationContextVariables);
	   add(leftPanel);
	   setLayout(null);
	   drawingArea.setBounds(0, 0,applicationContextVariables.getWidth()-applicationContextVariables.getLeftPanelWidth(),applicationContextVariables.getHeight());
	   leftPanel.setBounds(applicationContextVariables.getWidth()-applicationContextVariables.getLeftPanelWidth(), 0,applicationContextVariables.getLeftPanelWidth(),applicationContextVariables.getHeight());
		setSize(applicationContextVariables.getWidth(),applicationContextVariables.getHeight()+30);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		if(!applicationContextVariables.getUserEmail().trim().equals("recorder@localhost")){
		    addWindowListener(new WindowAdapter() {
			     public void windowClosing(WindowEvent e) {
						if (JOptionPane.showConfirmDialog(null, "Are you sure ?", "Quit", 
							    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
							    == JOptionPane.YES_OPTION){
										try {	saveSessionAsZIP();} catch (IOException e1) {e1.printStackTrace();}
										if (isTeacher && JOptionPane.showConfirmDialog(null, "The Session Is Finished ?", "Close", 
											    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
											    == JOptionPane.YES_OPTION){		
											           endSession();
											            try {
															Thread.sleep(500);
														} catch (InterruptedException e1) {
															// TODO Auto-generated catch block
															e1.printStackTrace();
														}
										}
										dispose();
										System.exit(0);
						}
			     }
		     });
		}
  }
	//@TODO saveSessionAsPDF
		public  void saveSessionAsZIP() throws IOException {
			String sessionDirectoryName=System.getProperty("user.home")+"/rteacher/"+applicationContextVariables.getSessionId();
			File sessionDirectory=new File(sessionDirectoryName);
			if(!sessionDirectory.exists())sessionDirectory.mkdirs();
			Iterator iterator=applicationContextVariables.getPageList().keySet().iterator();
			while(iterator.hasNext()){
				Page page=(Page)applicationContextVariables.getPageList().get(iterator.next());
		//		System.out.println(page.getNameTime());
				String nameTime=page.getNameTime().replace(':', '_');
				

				
				File outputfile = new File(sessionDirectoryName+"/"+nameTime+".jpg");
				if (outputfile.exists())outputfile.delete();
				 ImageIO.write(page.getBufferedImage(), "jpg", outputfile);
			}
			JOptionPane.showMessageDialog(null, "Your session is saved as series of  jpeg images \n into "+ sessionDirectoryName, "SAVED SESSION: " +applicationContextVariables.getSessionId(), JOptionPane.INFORMATION_MESSAGE);
		}
		
	    public LeftPanel getLeftPanel() {
			return leftPanel;
		}
		public void setLeftPanel(LeftPanel leftPanel) {
			this.leftPanel = leftPanel;
		}
		public DrawingArea getDrawingArea() {
			return drawingArea;
		}
		public void setDrawingArea(DrawingArea drawingArea) {
			this.drawingArea = drawingArea;
		}
		public void endSession(){
			applicationContextVariables.getNetworkConnector().getMessageSender().endSession();
		}

}
