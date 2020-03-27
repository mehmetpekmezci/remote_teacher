package userInterface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.StringTokenizer;

import userInterface.shapes.Circle;
import userInterface.shapes.Line;
import userInterface.shapes.Point;
import userInterface.shapes.Rectangle;
import userInterface.shapes.StringShape;

public class DrawingArea extends Canvas implements MouseListener,MouseMotionListener,KeyListener {
	private int mouse_x = 0, mouse_y = 0;
	private Shape current_shape = null;
	private StringShape current_string ;
	java.awt.Toolkit toolkit;
	Clipboard clipboard = null;
    ApplicationContextVariables applicationContextVariables;
	public DrawingArea(ApplicationContextVariables applicationContextVariables) {
		this.applicationContextVariables=applicationContextVariables;
		current_string = new StringShape(applicationContextVariables.getFontSize());
		this.setVisible(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		toolkit = getToolkit();
		clipboard = toolkit.getSystemClipboard();
	}
	public void mouseDragged(MouseEvent e) {
		if (current_shape != null && !applicationContextVariables.getCurrentMod().equals("MOVE"))
			current_shape.setInitialPoint2(e.getX(), e.getY());
		else if(applicationContextVariables.getCurrentMod().equals("MOVE")){
			current_shape.move(e.getX(),e.getY(),false,applicationContextVariables.getNetworkConnector().getMessageSender(),applicationContextVariables.getCurrentPage());
		}

		repaint();
	}
	public void mouseMoved(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		try {

			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
				doPaste();
			}else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
				 doCopy() ;
			}else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

				if (current_string.getStr().length() > 0) {
					current_string.getStr().deleteCharAt(current_string.getStr().length() - 1);
				}	else if (applicationContextVariables.getCurrentPage().getStringListSize() > 0) {
					current_string =
						(StringShape) applicationContextVariables.getCurrentPage().getString(applicationContextVariables.getCurrentPage().getStringListSize() - 1);
					applicationContextVariables.getCurrentPage().removeString(current_string,false);
					current_string.getStr().deleteCharAt(current_string.getStr().length() - 1);
				}
				repaint();
				return;
			}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				return;
			}else  if (e.getKeyCode() == KeyEvent.VK_ENTER) { // ENTER
				mouse_x = current_string.X1;
				mouse_y = mouse_y + current_string.getFontSize()+2;
                resetString();
				repaint();
				return;
			}else{
			    //else if (e.getKeyCode() != KeyEvent.VK_UNDEFINED) {
				this.current_string.getStr().append(e.getKeyChar());
			}
			
			repaint();
		} catch (Exception e1) {
			System.out.println(e1);
			// TODO Auto-generated catch block

		}
	}

	public void keyReleased(KeyEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {

		mouse_x = e.getX();
		mouse_y = e.getY();

		if (current_string.getStr().length() > 0) {
			applicationContextVariables.getCurrentPage().addString(current_string,false);
		}

		StringShape ss = null;
		for (int i =applicationContextVariables.getCurrentPage().getStringListSize(); i > 0; i--) {
			ss = ((StringShape) applicationContextVariables.getCurrentPage().getString(i - 1));
			if (e.getX() - ss.X1 < (ss.getStr().length() * 7)
				&& Math.abs(e.getY() - ss.Y1) < 8
				&& e.getX() > ss.X1)
				break;
			else
				ss = null;
		}
		if (ss != null)
			current_string = ss;
		else {
			current_string = new StringShape(applicationContextVariables.getFontSize());
			current_string.color = applicationContextVariables.getCurrentColor();
			current_string.setInitialPoint1(mouse_x, mouse_y);

		}

		switch (applicationContextVariables.getCurrentMod()) {
			case "MOVE" :
				{
					int l = applicationContextVariables.getCurrentPage().getDrawingListSize();
					Shape s = null;
					if (l > 0) {

						for (int i = l; i > 0; i--) {
							s = applicationContextVariables.getCurrentPage().getShape(i - 1);
							if (s.isInside(e.getX(), e.getY()))
								break;
							else
								s = null;
						}
						s.SELECT_X=e.getX();s.SELECT_Y=e.getY();
						current_shape = s;
					}
                  
					break;
				}
			case "LINE" :
				{
					current_shape = new Line();
					current_shape.X1=mouse_x;
					current_shape.Y1=mouse_y;
					current_shape.X2=mouse_x;
					current_shape.Y2=mouse_y;
					
					break;
				}
			case "RECTANGLE" :
				{
					current_shape = new Rectangle(true);
					break;
				}
			case "FILLED RECT" :
				{
					current_shape = new Rectangle(false);
					break;
				}
			case "OVAL" :
				{
					current_shape = new Circle(true);
					break;
				}
			case "FILLED OVAL" :
				{
					current_shape = new Circle(false);
					break;
				}
			case "DRAW" :
				{
					current_shape = new Point();
					break;
				}
			default :
				{
					current_shape = null;
					break;
				}
		}
		if (current_shape == null)
			return;
		if(!applicationContextVariables.getCurrentMod().equals("MOVE")){
			current_shape.setInitialPoint1(e.getX(), e.getY());
			current_shape.color = applicationContextVariables.getCurrentColor();			
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (current_shape != null) {
			if (current_shape.X2 != 0)
				if (current_shape.Y2 != 0) {
					if (!applicationContextVariables.getCurrentMod().equals("MOVE"))
						applicationContextVariables.getCurrentPage().addShape(current_shape,false);
				}
		}
		current_shape = null;
		repaint();
	}

	public void mouseEntered(MouseEvent e) {this.requestFocus();}
	public void mouseExited(MouseEvent e) {}

	public void update(Graphics g) {
		applicationContextVariables.getCurrentPage().getBufferedImage().getGraphics().setColor(getBackground());
		applicationContextVariables.getCurrentPage().getBufferedImage().getGraphics().fillRect(0, 0, getWidth(), getHeight());
		applicationContextVariables.getCurrentPage().getBufferedImage().getGraphics().setColor(applicationContextVariables.getCurrentColor());
		paint(applicationContextVariables.getCurrentPage().getBufferedImage().getGraphics());
		g.drawImage(applicationContextVariables.getCurrentPage().getBufferedImage(), 0, 0, this);
	}

	public void paint(Graphics g) {
		if(g==null)return;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(applicationContextVariables.getCurrentColor());
		Shape s = null;
		for (int i = 0; i < applicationContextVariables.getCurrentPage().getDrawingListSize(); i++) {
			s = (Shape) applicationContextVariables.getCurrentPage().getShape(i);
			s.paint(g);
		}
		if (current_shape != null)
			current_shape.paint(g);
		current_string.paint(g);

	}




	public void doPaste() {
		Transferable t = clipboard.getContents(this);
		if (t != null) {
			try {
				String s = (String) t.getTransferData(DataFlavor.stringFlavor);
				StringTokenizer st =
					new StringTokenizer(
						s,
						System.getProperty("line.separator"));
				while (st.hasMoreTokens()) {
					StringShape ss = new StringShape(applicationContextVariables.getFontSize());
					ss.setStr(new StringBuffer(st.nextToken()));
					ss.color = applicationContextVariables.getCurrentColor();
					ss.setInitialPoint1(mouse_x, mouse_y);
					applicationContextVariables.getCurrentPage().addString(ss,false);
					mouse_y = mouse_y + 14;
					repaint();
				
				}
				System.out.println(s);
			} catch (Exception e) {
			}
		}
	}
	public void doCopy() {
		
		StringBuffer str=new StringBuffer();
		for(int i =0;i<applicationContextVariables.getCurrentPage().getStringListSize();i++){
			str.append(((StringShape)applicationContextVariables.getCurrentPage().getString(i)).getStr());
		}
		StringSelection stringSelection = new StringSelection(str.toString());
	    clipboard.setContents(stringSelection, stringSelection);
		
	}

	public void undo(){
		applicationContextVariables.getCurrentPage().undo(false);
		repaint();
	}
	
	public void resetString(){
		if(current_string==null || current_string.getStr()==null || current_string.getStr().length()==0)return;
		applicationContextVariables.getCurrentPage().addString(current_string,false);
		current_string = new StringShape(applicationContextVariables.getFontSize());
		current_string.color = applicationContextVariables.getCurrentColor();
		current_string.setInitialPoint1(mouse_x, mouse_y);
		
	}



}
