package media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

import javax.imageio.ImageIO;

import userInterface.ApplicationContextVariables;
import userInterface.CameraPanel;

public class VideoReceiver extends Thread{
	SocketChannel socketChannel;
	BufferedImage bufferedImage;
	ByteBuffer byteBuffer = ByteBuffer.allocate(11000);
	byte[] previousImcompleteArray;
	ApplicationContextVariables applicationContextVariables;
	private CameraPanel cameraPanel;
	
	boolean isLoggedIn=false;
	boolean loginMessageSent=false;
	ByteBuffer loginMessageReturnBuffer=ByteBuffer.allocate(100);
	
	VideoReceiver previousVideoListener;
	



	public VideoReceiver(SocketChannel socketChannel,ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
		this.socketChannel=socketChannel;
	}


	/*
	
newly allocated read   bufferPosition: 0 limit: 15360 remaining: 15360 capacity: 15360
after first read  bufferPosition: 13409 limit: 15360 remaining: 1951 capacity: 15360
before flip  bufferPosition: 13409 limit: 15360 remaining: 1951 capacity: 15360
after flip bufferPosition: 0 limit: 13409 remaining: 13409 capacity: 15360
after first get  bufferPosition: 1024 limit: 13409 remaining: 12385 capacity: 15360
after second get  bufferPosition: 2048 limit: 13409 remaining: 11361 capacity: 15360
after clear  bufferPosition: 0 limit: 15360 remaining: 15360 capacity: 15360
after second read  bufferPosition: 0 limit: 15360 remaining: 15360 capacity: 15360
before flip  bufferPosition: 0 limit: 15360 remaining: 15360 capacity: 15360
after flip  bufferPosition: 0 limit: 0 remaining: 0 capacity: 15360
	
	 */

	public CameraPanel getCameraPanel() {
		return cameraPanel;
	}

	public void setCameraPanel(CameraPanel cameraPanel) {
		this.cameraPanel = cameraPanel;
	}

	public void run() {
		
	   while(true){
		   try {
			Thread.sleep(100);
		   } catch (InterruptedException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		   }			   
		   if(!applicationContextVariables.isReadyToStartMedia()){
			   try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 continue;
		   }
		   if(previousVideoListener!=null && ! previousVideoListener.isLoggedIn){
			   try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 continue;
		   }

		   if(!isLoggedIn){
			   try{
			   if(!loginMessageSent){
//				   System.out.println("Video "+new String(applicationContextVariables.getMediaConnector().getLoginMessage()));
					socketChannel.write(ByteBuffer.wrap(applicationContextVariables.getMediaConnector().getLoginMessage()));
				   loginMessageSent=true;
			   }
			   if(socketChannel.read(loginMessageReturnBuffer)>0){
				   loginMessageReturnBuffer.flip();
//				   System.out.println("Audio Listener:"+loginMessageReturnBuffer.limit());
					Charset charset = Charset.forName("UTF-8");
					CharsetDecoder decoder = charset.newDecoder();
					CharBuffer charBuffer = decoder.decode(loginMessageReturnBuffer);
					isLoggedIn=charBuffer.toString().equals("OK");
			   }
			   continue;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }

		   try { 
			   byteBuffer.clear();
			  while(socketChannel!=null && socketChannel.read(byteBuffer) > 0) {
				   if(byteBuffer.position()==0)continue;
                   //byteBuffer.flip();
                   byte[] bytesRead=Arrays.copyOf(byteBuffer.array(),byteBuffer.position());
    			   byteBuffer.clear();
    			   
				   int[] imageFinIndexes=new int[10];
				   for (int i=0;i<imageFinIndexes.length;i++)imageFinIndexes[i]=-1;
				   int imageCounter=0;
                   for(int i=0;i<bytesRead.length;i++) {
                	   if(i>0 && bytesRead[i-1]==-1 && bytesRead[i]==-39 ){
                		   imageCounter++;
                		   imageFinIndexes[imageCounter-1]=i;
                	   }
                   }

                   int previousFinIndex=0;
                   int i=0;
                   byte[] imageBytes = new byte[1];
//                   System.out.println("imageCounter="+imageCounter);
		           if(imageCounter>0){
		        	   try{
		        	   for( i=0;i<imageCounter;i++){
		        		   int imageArraySize=0;

		        		   if(previousImcompleteArray!=null)	   imageArraySize=previousImcompleteArray.length;
		        		   
		        		   imageArraySize+=imageFinIndexes[i]+1;
		        		   if(i>0)imageArraySize-=imageFinIndexes[i-1]+1;
		        		   
		        		    imageBytes = new byte[imageArraySize];
		        		    
//		        		    System.out.println("imageArraySize="+imageArraySize);
		        		   
		        		   if(previousImcompleteArray!=null){
		        			   System.arraycopy(previousImcompleteArray,0,imageBytes,0,previousImcompleteArray.length);


		        			   System.arraycopy(bytesRead,previousFinIndex,imageBytes,previousImcompleteArray.length,imageFinIndexes[i]-previousFinIndex+1);
	//	        			   System.out.println(" if     "+bytesRead[previousFinIndex]+"  "+imageBytes[0]+"  "+imageBytes[1]+"  ..... "+imageBytes[imageBytes.length-3]+"  "+imageBytes[imageBytes.length-2]+"  "+imageBytes[imageBytes.length-1]);

		        			   previousImcompleteArray=null;

		        		   }else{
		        			   System.arraycopy(bytesRead,previousFinIndex,imageBytes,0,imageFinIndexes[i]-previousFinIndex+1);
//		        			   System.out.println(" else  "+bytesRead[previousFinIndex]+"  "+imageBytes[0]+"  "+imageBytes[1]+"  ..... "+imageBytes[imageBytes.length-3]+"  "+imageBytes[imageBytes.length-2]+"  "+imageBytes[imageBytes.length-1]);
			        		   
		        		   }
		        		  
		        		   previousFinIndex=imageFinIndexes[i]+1;
//		        		     System.out.println("(imageBytes.length "+imageBytes.length+" index pointer="+imageFinIndexes[i]+"  "+imageBytes[0]+"  "+imageBytes[1]+"  ..... "+imageBytes[imageBytes.length-3]+"  "+imageBytes[imageBytes.length-2]+"  "+imageBytes[imageBytes.length-1]);
		        		     drawImage(imageBytes);
		        		     
		        	   }
		        	   if(imageFinIndexes[imageCounter-1]<bytesRead.length-1){
		        		   previousImcompleteArray=new byte[bytesRead.length-imageFinIndexes[imageCounter-1]];
//		        		    System.out.println("ZZZZ previousImcompleteArray.length "+previousImcompleteArray.length+"  "+bytesRead.length+"  "+imageCounter+"  "+imageFinIndexes[imageCounter-1]+"  "+(bytesRead.length-imageFinIndexes[imageCounter-1]));
		        		   System.arraycopy(bytesRead,imageFinIndexes[imageCounter-1]+1, previousImcompleteArray, 0, previousImcompleteArray.length-1);
//		        		    System.out.println("XXXX previousImcompleteArray.length "+previousImcompleteArray.length+"    "+previousImcompleteArray[0]+"  "+previousImcompleteArray[1]+"  ..... "+imageBytes[imageBytes.length-3]+"  "+imageBytes[imageBytes.length-2]+"  "+imageBytes[imageBytes.length-1]);

		        	   }
		        	   
		        	   }catch(Exception e){
		        		   e.printStackTrace();
		        		   System.out.println(bytesRead.length+"     "+previousFinIndex+"   "+imageBytes.length+"    "+imageFinIndexes[i]+"  "+(imageFinIndexes[i]-previousFinIndex));
		        	   }
		        	   
		           }else{
		        	   if(previousImcompleteArray!=null){
		        		   byte[] newArray=new byte[previousImcompleteArray.length+bytesRead.length];
		        		   System.arraycopy(previousImcompleteArray, 0,newArray, 0,previousImcompleteArray.length);
		        		   System.arraycopy(bytesRead, 0,newArray, previousImcompleteArray.length,bytesRead.length);
		        		   previousImcompleteArray=newArray;
		        	   }else{
		        		   byte[] previousImcompleteArray=new byte[bytesRead.length];
		        		   System.arraycopy(bytesRead, 0,previousImcompleteArray,0,bytesRead.length);
		        	   }
		        	      
		           }
			   } 

		   }catch(Exception e){
			   e.printStackTrace();

		   }
	   
	}
	
	}
	
	public void drawImage(byte[] imageBytes){
		if(cameraPanel==null)System.out.println("cameraPanel is null");
		if(imageBytes==null || imageBytes.length<2|| cameraPanel==null)return;
	     try {
			bufferedImage=ImageIO.read(new ByteArrayInputStream(imageBytes));


		 if(bufferedImage!=null && !applicationContextVariables.getMainFrame().getLeftPanel().getCameraPanel1().isShowOwnCamera()){
			// System.out.println(applicationContextVariables.getUserEmail()+" drawing image with size="+imageBytes.length);
			 
			 cameraPanel.drawImage(bufferedImage);
		 }

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}
	
	
	public VideoReceiver getPreviousVideoListener() {
		return previousVideoListener;
	}


	public void setPreviousVideoListener(VideoReceiver previousVideoListener) {
		this.previousVideoListener = previousVideoListener;
	}
}
