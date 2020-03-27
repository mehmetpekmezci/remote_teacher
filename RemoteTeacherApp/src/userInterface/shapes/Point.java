/*
 * Created on Nov 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface.shapes;

import java.awt.Graphics;
import java.util.ArrayList;

import userInterface.Shape;

/**
 * @author maya
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Point extends Shape {
     class point{
     	int x;
     	int y;
     }
     ArrayList points=new ArrayList();
     
	/* (non-Javadoc)
	 * @see userInterface.Shape#paint(java.awt.Graphics)
	 */
	 
	public void paint(Graphics g) {
		if(points.size()<2)return;
		g.setColor(color);
		point p1=null;
		point p2=null;
		p1=(point)points.get(0);
		for(int i=0;i<points.size()-1;i++){
			
			
			p2=(point)points.get(i+1);
			
			g.drawLine(p1.x,p1.y,p2.x,p2.y);
			
			p1=p2;
			
		}

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint1(int, int)
	 */
	public void setInitialPoint1(int X, int Y) {
		X1=X;Y1=Y;
	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint2(int, int)
	 */
	public void setInitialPoint2(int X, int Y) {
		point p=new point();
		p.x=X;
		p.y=Y;
		points.add(p);
		X2=X;
		Y2=Y;
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

	public String getCreateMessageExtension() {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("POINT@@");
		for(int i=0;i<points.size();i++){
			point p=(point)points.get(i);
			stringBuffer.append(p.x).append(",").append(p.y).append("@@");
		}
		return stringBuffer.toString();
	}

}
