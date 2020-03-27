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
public class Circle extends Shape {
	int pointdedepartx=0;
		int pointdedeparty=0;
		public Circle(boolean est_creux){
			this.setIsCreux(est_creux);
		}
	/* (non-Javadoc)
	 * @see userInterface.Shape#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		g.setColor(this.color);
		if(isCreux){
			g.drawOval(X1,Y1,X2-X1,Y2-Y1);
		}
		else {
			g.fillOval(X1,Y1,X2-X1,Y2-Y1);
		} 
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint1(int, int)
	 */
	public void setInitialPoint1(int X, int Y) {
		this.pointdedepartx=X;
				this.pointdedeparty=Y;
				this.X1=X;
				this.Y1=Y;
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see userInterface.Shape#setInitialPoint2(int, int)
	 */
	public void setInitialPoint2(int X, int Y) {
		// TODO Auto-generated method stub
		if (X< pointdedepartx)
					   {X1 =X;X2=pointdedepartx;}
				else{X2 = X;X1=pointdedepartx;}
				if (Y < pointdedeparty)
					   {Y1 = Y;Y2=pointdedeparty;}
				else  {Y2 = Y;Y1=pointdedeparty;}
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
		this.isCreux=creusite;
		// TODO Auto-generated method stub

	}

	public String getCreateMessageExtension() {
		return "CIRCLE@@"+pointdedepartx+"@@"+pointdedeparty;
	}


}
