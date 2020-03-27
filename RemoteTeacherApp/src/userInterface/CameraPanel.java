package userInterface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.nio.channels.SocketChannel;

public class CameraPanel extends Canvas implements MouseListener{

	private boolean showOwnCamera=false;


	public CameraPanel(){
		addMouseListener(this);
	   
	}
	
	public void drawImage(BufferedImage bufferedImage){
		getGraphics().drawImage(bufferedImage, 0, 0,bufferedImage.getWidth(), bufferedImage.getHeight(), this);
        update(bufferedImage.getGraphics());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		if(showOwnCamera){
			showOwnCamera=false;
		}else{
			showOwnCamera=true;
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isShowOwnCamera() {
		return showOwnCamera;
	}

	public void setShowOwnCamera(boolean showOwnCamera) {
		this.showOwnCamera = showOwnCamera;
	}
}
