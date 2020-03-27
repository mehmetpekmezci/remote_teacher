/*
 * Created on Oct 27, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface.shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import userInterface.Shape;

/**
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Img extends Shape {
     public java.awt.Image image=null;
     public ImageObserver imageobserver=null;
     public String extension;
	/* (non-Javadoc)
	 * @see userInterface.Shape#paint(java.awt.Graphics)
	 */
	int pointdedepartx;
	int pointdedeparty;
	int width;
	int height;
	 
	public void paint(Graphics g) {
          if(image!=null) g.drawImage(image,X1,Y1,(X2-X1),(Y2-Y1),imageobserver);
	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint1(int, int)
	 */
	public void setInitialPoint1(int X, int Y) {
		pointdedepartx=X1=X;
		pointdedeparty=Y1=Y;
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint2(int, int)
	 */
	public void setInitialPoint2(int X, int Y) {
		if (X< pointdedepartx)
					   {X1 =X;X2=pointdedepartx;}
				else{X2 = X;X1=pointdedepartx;}
				if (Y < pointdedeparty)
					   {Y1 = Y;Y2=pointdedeparty;}
				else  {Y2 = Y;Y1=pointdedeparty;}
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#isInside(int, int)
	 */
	public boolean isInside(int X, int Y) {
		// TODO Auto-generated method stub
		return (X1<X && Y1<Y && X2>X && Y2>Y);
	}



	/* (non-Javadoc)
	 * @see userInterface.Shape#setIsCreux(boolean)
	 */
	public void setIsCreux(boolean creusite) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#getBytes()
	 */
	public int[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCreateMessageExtension() {
//		return "IMG";
//		return "IMG@@"+pointdedepartx+"@@"+pointdedeparty+"@@"+width+"@@"+height;
		return "IMG@@"+extension;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
