/*
 * Created on Oct 26, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface.shapes;

import java.awt.Graphics;

import userInterface.Shape;

/**
 * @author maya
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Line extends Shape{

	/* (non-Javadoc)
	 * @see userInterface.Shape#paint(java.awt.Graphics)
	 */
	 public Line(){
	 	super();
	 }
	public void paint(Graphics g) {
		g.setColor(color);
		g.drawLine(X1,Y1,X2,Y2);
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint1(int, int)
	 */
	public void setInitialPoint1(int X, int Y) {
		X1=X;
		Y1=Y;
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint2(int, int)
	 */
	public void setInitialPoint2(int X, int Y) {
		X2=X;Y2=Y;
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#isInside(int, int)
	 */
	public boolean isInside(int X, int Y) {
		double d=Math.sqrt((X-X1)*(X-X1)+(Y-Y1)*(Y-Y1))+
		Math.sqrt((X-X2)*(X-X2)+(Y-Y2)*(Y-Y2))-
		Math.sqrt((X2-X1)*(X2-X1)+(Y2-Y1)*(Y2-Y1));
		return d<3; 
		
		
	}


	/* (non-Javadoc)
	 * @see userInterface.Shape#setIsCreux(boolean)
	 */
	public void setIsCreux(boolean creusite) {
		// TODO Auto-generated method stub
		
	}

	public String getCreateMessageExtension() {
		return "LINE";
	}

}
