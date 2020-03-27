/*
 * Created on Oct 21, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

import network.MessageSender;

/**
 * @author maya
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class Shape {

	public long previousShapesId=0;
	public long nextShapesId=0;
	public long id=System.currentTimeMillis();
    public Color color=Color.WHITE;
    public boolean isCreux=true;
    public int X1=0,X2=0,Y1=0,Y2=0,SELECT_X=0,SELECT_Y=0;
    public boolean isLocal=true;
    
    public abstract void paint(Graphics g);
    public abstract void setInitialPoint1(int X,int Y );
	public abstract void setInitialPoint2(int X,int Y );
    public abstract boolean isInside(int X,int Y);
    

    public  void move(int egetX,int egetY,boolean calledFromNetwork,MessageSender messageSender,Page currentPage){
    	if(!calledFromNetwork)messageSender.move(currentPage,this, egetX, egetY);
		X1=X1+(egetX-SELECT_X);
		X2=X2+(egetX-SELECT_X);
		Y1=Y1+(egetY-SELECT_Y);
		Y2=Y2+(egetY-SELECT_Y);
		SELECT_X=egetX;
		SELECT_Y=egetY;
    }
    public abstract void setIsCreux(boolean creusite);
    public abstract String getCreateMessageExtension();
    public void setParameters(int X1, int X2, int Y1, int Y2, int colorHashCode,boolean isCreux, long id,long previousShapeId,long nextShapesId,boolean isLocal){
		this.X1=X1;
		this.X2=X2;
		this.Y1=Y1;
		this.Y2=Y2;
		this.color=new Color(colorHashCode);
		this.isCreux=isCreux;
		this.previousShapesId=previousShapeId;
		this.nextShapesId=nextShapesId;
		this.id=id;
    	this.isLocal=isLocal;
    }

}
