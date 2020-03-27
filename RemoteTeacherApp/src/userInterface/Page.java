package userInterface;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import network.MessageSender;
import userInterface.shapes.Img;
import userInterface.shapes.StringShape;

public class Page {
		private BufferedImage bufferedImage;
		private ArrayList drawingList = new ArrayList();
		private HashMap<Long, Shape> drawingListById = new HashMap();
		private ArrayList stringList = new ArrayList();
		private String nameTime;
		ApplicationContextVariables applicationContextVariables;

		public Page(ApplicationContextVariables applicationContextVariables){
			this.applicationContextVariables=applicationContextVariables;
			bufferedImage=new BufferedImage(applicationContextVariables.getWidth()-applicationContextVariables.getLeftPanelWidth(),applicationContextVariables.getHeight(), ColorSpace.TYPE_RGB);
		}

		public void moveShapeFromNetwork(long id,int X1,int X2,int Y1,int Y2 ){
			drawingListById.get(new Long(id)).X1=X1;
			drawingListById.get(new Long(id)).X2=X2;
			drawingListById.get(new Long(id)).Y1=Y1;
			drawingListById.get(new Long(id)).Y2=Y2;
		}
		public void addShape(Shape shape,boolean calledFromNetwork){
			if(drawingList.size()>0){
				Shape lastShape=(Shape)drawingList.get(drawingList.size()-1);
				lastShape.nextShapesId=shape.id;
				shape.previousShapesId=lastShape.id;
			}
			synchronized (drawingList) {
				drawingList.add(shape);
			}
			synchronized (drawingListById) {
				drawingListById.put(new Long(shape.id),shape);
			}
			if(!calledFromNetwork)applicationContextVariables.getNetworkConnector().getMessageSender().create(this,shape);
			
		}
		public void removeShape(Shape shape,boolean calledFromNetwork){
			if(shape==null)return;
	//		System.out.println("removing shape:"+shape.id);
			
			Shape previousShape=null;
			Shape nextShape=null;
			for(int i=0;i<drawingList.size();i++){
				if(((Shape)drawingList.get(i)).id==shape.id){
					if(i<drawingList.size()-1){
						nextShape=(Shape)drawingList.get(i++);
					}
					break;
				}else{
					previousShape=(Shape)drawingList.get(i);
				}
			}
			int removeIndex=-1;
			synchronized (drawingList) {
			
				for(int i=0;i<drawingList.size();i++){
					Shape s=(Shape)drawingList.get(i);
					if(s.id==shape.id){
						removeIndex=i;
//						System.out.println(s.getClass().getName());
						if(s.getClass().getName().equals("userInterface.shapes.Img")){
							((Img)s).image=null;
						}
						break;
					}
					
				}
				if(removeIndex>-1){
					drawingList.remove(removeIndex);	
				}
				if(nextShape!=null){
					if(previousShape!=null)
					nextShape.previousShapesId=previousShape.id;
					else nextShape.previousShapesId=0;
				}if(previousShape!=null){
					if(nextShape!=null)previousShape.nextShapesId=nextShape.id;
					else previousShape.nextShapesId=0;
				}
			}
			synchronized (drawingListById) {
				drawingListById.remove(new Long(shape.id));
			}
			
			if(!calledFromNetwork)applicationContextVariables.getNetworkConnector().getMessageSender().delete(this, shape);
		}
		public void addString(StringShape shapeString,boolean calledFromNetwork){
			synchronized (stringList) {
				stringList.add(shapeString);
				addShape(shapeString,calledFromNetwork);
			}
		}
		public void removeString(StringShape shapeString,boolean calledFromNetwork){
			synchronized (stringList) {
				stringList.remove(shapeString);
				removeShape(shapeString,calledFromNetwork);
			}
		}
		public int getStringListSize(){
			return stringList.size();
		}
		public int getDrawingListSize(){
			return drawingList.size();
		}
		public StringShape getString(int i){
			return (StringShape)stringList.get(i);
		}
		public BufferedImage getBufferedImage() {
			return bufferedImage;
		}
		public void setBufferedImage(BufferedImage bufferedImage) {
			this.bufferedImage = bufferedImage;
		}
		public String getNameTime() {
			return nameTime;
		}
		public void setNameTime(String nameTime) {
			this.nameTime = nameTime;
		}
		public Shape getShape(int i){
			return (Shape)drawingList.get(i);
		}
		
		public void undo(boolean calledFromNetwork){

			int l =getDrawingListSize();
			Shape s = null;
			if (l > 0) {
				int i = l;
				do {
					s = (Shape) getShape(i - 1);
					if (s.getClass().getName().equals(StringShape.class.getName())  ) {
						i--;
						//System.out.println(i);
						continue;
					} else
						break;

				} while (i > 0);
				if ( !s.getClass().getName().equals(StringShape.class.getName()))
					removeShape(s,calledFromNetwork);
			}
		}

		public void removeShape(long shapeId,boolean calledFromNetwork){
			removeShape(drawingListById.get(new Long(shapeId)), calledFromNetwork);
		}
}
