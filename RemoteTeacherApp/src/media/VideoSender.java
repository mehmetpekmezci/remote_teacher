package media;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.ZipInputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JOptionPane;

import userInterface.ApplicationContextVariables;

import com.github.sarxos.webcam.Webcam;

public class VideoSender extends Thread{
	Webcam webcam = Webcam.getDefault();
	SocketChannel socketChannel;
	ImageWriter imageWriter;
	ImageWriteParam param ;
	BufferedImage bufferedImage;
	ByteArrayOutputStream byteArrayOutputStream;
	ApplicationContextVariables applicationContextVariables;
	long lastSendTimeOfEmptyImage=0;
	boolean isLoggedIn=false;
	boolean loginMessageSent=false;
	ByteBuffer loginMessageReturnBuffer=ByteBuffer.allocate(100);


	public VideoSender(SocketChannel socketChannel,ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
        this.socketChannel=socketChannel;
		initWebcam();
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		if (!writers.hasNext()) throw new IllegalStateException("No writers found");
		imageWriter = (ImageWriter) writers.next();
		byteArrayOutputStream=new ByteArrayOutputStream();
		try {
			ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
			imageWriter.setOutput(ios);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		param=imageWriter.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(0.4f);
	//	param.setCompressionQuality(0.5f);

	}


	public void run() {
	//	if(true)return;
		while(true){
			   if(!applicationContextVariables.getMediaConnector().getVideoReceiver().isLoggedIn){
				   try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 continue;
			   }
			   
	//		   System.out.println(applicationContextVariables.getUserEmail()+" videosender");
			   if(socketChannel!=null&&socketChannel.isConnected() &&socketChannel.isOpen()){
		//		   System.out.println(applicationContextVariables.getUserEmail()+" videosender "+ socketChannel.isConnected());
				   if(webcam.isOpen() &&webcam.isImageNew())   bufferedImage=webcam.getImage(); 
				   if(bufferedImage!=null){
					   bufferedImage.getGraphics().drawString(applicationContextVariables.getUserEmail(), 20, 190);
					   try {
						         imageWriter.write(null, new IIOImage(bufferedImage, null, null), param);
								byte[] imageBytes=byteArrayOutputStream.toByteArray();
						        socketChannel.write(ByteBuffer.wrap(imageBytes));
			//			        System.out.println(applicationContextVariables.getUserEmail()+"  sending video "+imageBytes.length);
						        byteArrayOutputStream.reset();
						        if(applicationContextVariables.getMainFrame().getLeftPanel().getCameraPanel1().isShowOwnCamera()){
						        	applicationContextVariables.getMainFrame().getLeftPanel().getCameraPanel1().drawImage(bufferedImage);
								   }
					    } catch (IOException e) {
//					    	try {
//								socketChannel.close();
//							} catch (IOException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}socketChannel=null;
				    		e.printStackTrace();
					   }
				   }else{
					   bufferedImage=new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
					   long now=System.currentTimeMillis();
					   if(now-lastSendTimeOfEmptyImage>1000){
						   bufferedImage.getGraphics().drawString("WebCam is not available ", 20,50);
						   bufferedImage.getGraphics().drawString(""+new Date(), 20,100);
						   bufferedImage.getGraphics().drawString(applicationContextVariables.getUserEmail(), 20, 150);
						   lastSendTimeOfEmptyImage=now;
						   try {
						         imageWriter.write(null, new IIOImage(bufferedImage, null, null), param);
								byte[] imageBytes=byteArrayOutputStream.toByteArray();
								
								//ZIP dusun...
								
						        socketChannel.write(ByteBuffer.wrap(imageBytes));

				//		        System.out.println(applicationContextVariables.getUserEmail()+"  sending video "+imageBytes.length);
						        byteArrayOutputStream.reset();
					    } catch (IOException e) {
							try {
								socketChannel.close();
								socketChannel=null;
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				    		e.printStackTrace();
					   }
					   }

					   
				   }
		   }
		   try {
				Thread.sleep(150);
		   } catch (InterruptedException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
		   }
   
		}
	}
	public void initWebcam(){
		try{
		webcam.setViewSize(new Dimension(320,240));
		webcam = Webcam.getDefault();
		webcam.open(true);
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can't get the camera input !\nPlease close other applications using webcam.\nThen close/repopen this application. ", "WEBCAM ERROR", JOptionPane.INFORMATION_MESSAGE);

		}

	}

	
}
