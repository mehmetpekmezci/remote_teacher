package network;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import userInterface.ApplicationContextVariables;
import userInterface.LeftPanel;
import userInterface.Page;
import userInterface.PageSelector;
import userInterface.Shape;
import userInterface.shapes.Circle;
import userInterface.shapes.Img;
import userInterface.shapes.Line;
import userInterface.shapes.Point;
import userInterface.shapes.Rectangle;
import userInterface.shapes.StringShape;

public class MessageSender {

	ApplicationContextVariables applicationContextVariables;
	 ArrayList<String> queue;
	public MessageSender(ArrayList<String> queue,ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
		this.queue=queue;
	}

	public   void move(Page page,Shape shape,int egetX,int egetY){
    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
        	if(applicationContextVariables.getCurrentPage().getNameTime()!=null)	queue.add("PAGE@@"+page.getNameTime()+"@@"+"MOVE@@"+shape.id+"@@"+shape.X1+"@@"+shape.X2+"@@"+shape.Y1+"@@"+shape.Y2);
		}
    }
	public   void create(Page page,Shape shape){
    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
    	if(applicationContextVariables.getCurrentPage().getNameTime()!=null)	queue.add("PAGE@@"+page.getNameTime()+"@@"+"CREATE@@"+shape.id+"@@"+shape.previousShapesId+"@@"+shape.color.hashCode()+"@@"+shape.isCreux+"@@"+shape.X1+"@@"+shape.X2+"@@"+shape.Y1+"@@"+shape.Y2+"@@"+shape.getCreateMessageExtension());
    }}
	public   void delete(Page page,Shape shape){    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
    	if(applicationContextVariables.getCurrentPage().getNameTime()!=null)	queue.add("PAGE@@"+page.getNameTime()+"@@"+"REMOVE@@"+shape.id+"@@"+shape.previousShapesId+"@@"+shape.nextShapesId);
    }}
	public   void addPage(String nameTime){    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
    	queue.add("ADD_PAGE@@"+nameTime);
    }}
	public   void removePage(String nameTime){    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
    	queue.add("REMOVE_PAGE@@"+nameTime);
    }}
	public   void selectPage(String nameTime){    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
    	queue.add("SELECT_PAGE@@"+nameTime);
    }}
	public   void endSession(){    	synchronized (applicationContextVariables.getNetworkConnector().getSendQueueForStringCommands()) {
    	queue.add("END_SESSION");
    }}

}
