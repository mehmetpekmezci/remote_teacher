package network;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import userInterface.ApplicationContextVariables;
import userInterface.Page;
import userInterface.Shape;
import userInterface.shapes.Circle;
import userInterface.shapes.Img;
import userInterface.shapes.Line;
import userInterface.shapes.Point;
import userInterface.shapes.Rectangle;
import userInterface.shapes.StringShape;

public class MessageHandler {
	ApplicationContextVariables applicationContextVariables;
	public MessageHandler(ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
	}
	private String  lastIncompleteMessage="";
	
	public  void handleAddPageCommand(String newNameTime){
		
	}
	public  void handleRemovePageCommand(String deletedNameTime){
		applicationContextVariables.getMainFrame().getLeftPanel().getPageSelector().removePage(deletedNameTime);
	}

	public void handleMessages(String rawmessage){
		String[] splittedMEssages=rawmessage.split("##");
		for(int i=0;i<splittedMEssages.length;i++){
			handleMessage(splittedMEssages[i]);
		}
	}
	
	public void handleMessage(String message){
		System.out.println(message);
		if(message==null || message.length()==0)return;
		try{
	//	System.out.println(applicationContextVariables.getUserEmail()+" received the message :"+message);
		if(!message.endsWith("::::")){
			lastIncompleteMessage=lastIncompleteMessage+message;
		//	System.out.println("Appending "+lastIncompleteMessage +" \n AND \n"+message);
			return;
		}
		if(lastIncompleteMessage.length()>0){
			message=lastIncompleteMessage+message;
			lastIncompleteMessage="";
		}
		
		message=message.replace("::::","");
		
		if(message.startsWith("LOGIN ERROR")){
			JOptionPane.showMessageDialog(null, "Can not login to server ... \nCheck that your session occurs today.\nOther sessions are not allowed. ", "LOGIN ERROR", JOptionPane.INFORMATION_MESSAGE);
		}else if(message.startsWith("END_SESSION")){
				if(!applicationContextVariables.getUserEmail().equals("recorder@localhost"))JOptionPane.showMessageDialog(null, "Teacher closed the session, please close your window...", "END_SESSION", JOptionPane.INFORMATION_MESSAGE);	
		}
//		else if(message.startsWith("LOGGEDIN_USER")){
//			applicationContextVariables.getMainFrame().getLeftPanel().getUserListPanel().addUser(message.split(" ")[1]);
//		}else if(message.startsWith("LOGGEDOUT_USER")){
//			applicationContextVariables.getMainFrame().getLeftPanel().getUserListPanel().removeUser(message.split(" ")[1]);
//		}
		else if(message.startsWith("ADD_PAGE")){
			while(applicationContextVariables.getMainFrame()==null){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//when we receive the page message we can say that we can start video and audio connections
			// otherwise vidoelistener may not find rTeacherSession
			applicationContextVariables.setReadyToStartMedia(true);
//			System.out.println("message="+message);
			applicationContextVariables.getMainFrame().getLeftPanel().getPageSelector().addPage((message.split("@@"))[1]);
		}else if(message.startsWith("REMOVE_PAGE")){
			applicationContextVariables.getMainFrame().getLeftPanel().getPageSelector().removePage(message.split("@@")[1]);
		}else if(message.startsWith("SELECT_PAGE")){
			applicationContextVariables.getMainFrame().getLeftPanel().getPageSelector().selectPage(message.split("@@")[1]);
		}else if(message.startsWith("PAGE")){

			String[] msgArray=message.split("@@");
//			if(msgArray[1].equals("LAST"))msgArray[1]=applicationContextVariables.getCurrentPage().getNameTime();
			String selectedPageNameTime="Page  @ "+msgArray[1];
			Page selectedPage=applicationContextVariables.getPageList().get(selectedPageNameTime);
//			for(int i=0;i<msgArray.length;i++){
//				System.out.println("msgArray["+i+"]="+msgArray[i]);
//			}
			if(selectedPage!=null){
				if(msgArray[2].equals("CREATE")){
					Shape shape=null;
					long id=Long.parseLong(msgArray[3]);
					long previousShapeId=Long.parseLong(msgArray[4]);
					long nextShapesId=0;
					int colorHashCode=Integer.parseInt(msgArray[5]);
					boolean isCreux=Boolean.parseBoolean(msgArray[6]);
					int X1=Integer.parseInt(msgArray[7]);
					int X2=Integer.parseInt(msgArray[8]);
					int Y1=Integer.parseInt(msgArray[9]);
					int Y2=Integer.parseInt(msgArray[10]);
					if(msgArray[11].equals("CIRCLE")){
						shape=new Circle(isCreux);
						((Circle)shape).setInitialPoint1(X1,Y1);
					}else if(msgArray[11].equals("IMG")){
						BufferedImage bufferedImage=null;
						try {
							bufferedImage = ImageIO.read(new URL("http://"+applicationContextVariables.getServerIp()+"/rteacher/rteacher/images/"+id+"."+msgArray[12]));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						int width=applicationContextVariables.getCurrentPage().getBufferedImage().getWidth();
						int height=applicationContextVariables.getCurrentPage().getBufferedImage().getHeight();
						shape=new Img();
						((Img)shape).extension=msgArray[12];
						((Img)shape).image=(Image)bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
						((Img)shape).setInitialPoint1(X1,Y1);
						((Img)shape).setInitialPoint2(width,height);
						((Img)shape).setWidth(width);
						((Img)shape).setHeight(height);
						applicationContextVariables.getCurrentPage().addShape(((Img)shape),true); 
						applicationContextVariables.getMainFrame().getDrawingArea().repaint();
						System.out.println("IMG:"+width+" "+height+" "+X1+" "+Y1+((Img)shape).X2+" "+((Img)shape).Y2+" "+((Img)shape).image.toString());
					}else if(msgArray[11].equals("LINE")){
						shape=new Line();
						((Line)shape).setInitialPoint1(X1,Y1);
					}else if(msgArray[11].equals("POINT")){
						shape=new Point();
						((Point)shape).setInitialPoint1(X1,Y1);
						for(int i=12;i<msgArray.length;i++){
							X2=Integer.parseInt(msgArray[i].split(",")[0]);
							Y2=Integer.parseInt(msgArray[i].split(",")[1]);
							((Point)shape).setInitialPoint2(X2, Y2);	
						}
					}else if(msgArray[11].equals("RECTANGLE")){
						shape=new Rectangle(isCreux);
						((Rectangle)shape).setInitialPoint1(X1,Y1);
					}else if(msgArray[11].equals("STRING") && msgArray.length>13){
						shape=new StringShape(Integer.parseInt(msgArray[12]));
						((StringShape)shape).setInitialPoint1(X1,Y1);
						((StringShape)shape).setStr(new StringBuffer(msgArray[13]));
					}
					if(shape!=null){
						shape.setParameters(X1, X2, Y1, Y2, colorHashCode, isCreux,  id, previousShapeId, nextShapesId,false);
					}
					selectedPage.addShape(shape,true);
					
				}
				if(msgArray[2].equals("REMOVE")){
					long id=Long.parseLong(msgArray[3]);
					selectedPage.removeShape(id,true);
				}

				
				if(msgArray[2].equals("MOVE")){
					Shape shape=null;
					long id=Long.parseLong(msgArray[3]);
					int X1=Integer.parseInt(msgArray[4]);
					int X2=Integer.parseInt(msgArray[5]);
					int Y1=Integer.parseInt(msgArray[6]);
					int Y2=Integer.parseInt(msgArray[7]);
					selectedPage.moveShapeFromNetwork(id, X1, X2, Y1, Y2);
				}

			}
		}
		//System.out.println("RECEIVED MESSAGE IS :"+message);
    	//TODO mesaji isle
    	// FIRST LOGIN ERROR
		while(applicationContextVariables.getMainFrame()==null || applicationContextVariables.getMainFrame().getDrawingArea()==null){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		applicationContextVariables.getMainFrame().getDrawingArea().repaint();
		}catch(Exception e){
			//lastIncompleteMessage=lastIncompleteMessage+message;
			e.printStackTrace();
			
		}
	}
	
	
	

	
	
	

	
}
