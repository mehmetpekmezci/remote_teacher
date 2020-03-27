/*
 * Created on Nov 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface.shapes;

import java.awt.Font;
import java.awt.Graphics;

import userInterface.ApplicationContextVariables;
import userInterface.Shape;

/**
 * @author maya
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StringShape extends Shape {
     StringBuffer str=new StringBuffer();
     int fontSize;
	/* (non-Javadoc)
	 * @see userInterface.Shape#paint(java.awt.Graphics)
	 */
	public StringShape(int fontSize){
		this.fontSize=fontSize;
	}

	public void paint(Graphics g) {
		
		// TODO Auto-generated method stub
		g.setColor(color);
		
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
		g.setFont(font);


        g.drawString(str.toString(),X1,Y1);
        
	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint1(int, int)
	 */
	public void setInitialPoint1(int X, int Y) {
		X1=X;Y1=Y;
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint2(int, int)
	 */
	public void setInitialPoint2(int X, int Y) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#isInside(int, int)
	 */
	public boolean isInside(int X, int Y) {
		// TODO Auto-generated method stub
		return false;
	}



	/* (non-Javadoc)
	 * @see userInterface.Shape#setIsCreux(boolean)
	 */
	public void setIsCreux(boolean creusite) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#serialize()
	 */
	public int[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public StringBuffer getStr() {
		return str;
	}

	/**
	 * @param buffer
	 */
	public void setStr(StringBuffer buffer) {
		str = buffer;
	}
	public String getCreateMessageExtension() {
		return "STRING@@"+fontSize+"@@"+str.toString();
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
