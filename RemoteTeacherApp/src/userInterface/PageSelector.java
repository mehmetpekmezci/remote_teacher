package userInterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import network.MessageHandler;
import network.MessageSender;

public class PageSelector extends JPanel implements ActionListener,ListSelectionListener {

	
	JList pageList=new JList();;
	DefaultListModel pageListModel=new DefaultListModel();
	JButton addButton=new JButton("NEW PAGE");
	JButton removeButton=new JButton("DEL. SELECTED");
	ApplicationContextVariables applicationContextVariables;
	public PageSelector(ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
		setLayout(new BorderLayout());
		pageList.setModel(pageListModel);
//		String nameTime=new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
//		if(!Constants.isTeacher())nameTime="WAITING FOR TEACHER";
//		String currentPageName="Page  @ "+nameTime;
//		pageListModel.addElement(currentPageName);
		Page currentPage=new Page(applicationContextVariables);
//		currentPage.setNameTime(nameTime);
//		Constants.getPageList().put(currentPageName,currentPage);
		applicationContextVariables.setCurrentPage(currentPage);
		 JScrollPane pageListScrollPane=new JScrollPane(pageList);
		 add(pageListScrollPane,BorderLayout.CENTER);
		 JPanel buttonPanel=new JPanel();
		 buttonPanel.setLayout(new GridLayout(1,2));
		 buttonPanel.add(addButton);
		 buttonPanel.add(removeButton);
		 add(buttonPanel,BorderLayout.SOUTH);
		 addButton.addActionListener(this);
		 removeButton.addActionListener(this);
		 pageList.addListSelectionListener(this);
//		 if(!nameTime.equals("WAITING FOR TEACHER")){
//			 Constants.getNetworkConnector().getSendQueueForStringCommands().add(currentPage.getAddPageMessage());
//		 }
		 
//			if(!Constants.isTeacher()){
//				addButton.disable();
//				removeButton.disable();
//			}
	}


	public void actionPerformed(ActionEvent e) {
		applicationContextVariables.getMainFrame().getDrawingArea().resetString();
		if(e.getSource() instanceof JButton){
			if(!applicationContextVariables.isTeacher()){
				JOptionPane.showMessageDialog(null, "	Sorry , only Teacher/Instructor may Add/Remove pages..", "Teacher Restricted ! ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(((JButton)e.getSource()).getText().equals("NEW PAGE")){
				addPage(null);
			}else if(((JButton)e.getSource()).getText().equals("DEL. SELECTED")){
				if (JOptionPane.showConfirmDialog(null, "Are you sure ?", "Delete", 
					    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
					    == JOptionPane.YES_OPTION){
					if(pageListModel.size()<=1){
						JOptionPane.showMessageDialog(null, "	First page can not be deleted !", "First page can not be deleted ! ", JOptionPane.INFORMATION_MESSAGE);
					}else{
						removePage(null);
					}
			   }
			}
			// TODO Networking
			applicationContextVariables.getMainFrame().getDrawingArea().update(applicationContextVariables.getCurrentPage().getBufferedImage().getGraphics());
			applicationContextVariables.getMainFrame().getDrawingArea().repaint();
		}
		
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(applicationContextVariables.getPageList().get((String)pageList.getSelectedValue())!=null){
			applicationContextVariables.getNetworkConnector().getMessageSender().selectPage(((String)pageList.getSelectedValue()));
			selectPage((String)pageList.getSelectedValue());
		}

	}

	
	
	public void selectPage(String pageAtNameTime){
		applicationContextVariables.setCurrentPage(applicationContextVariables.getPageList().get(pageAtNameTime));
		applicationContextVariables.getMainFrame().getDrawingArea().update(applicationContextVariables.getCurrentPage().getBufferedImage().getGraphics());
		applicationContextVariables.getMainFrame().getDrawingArea().repaint();
		pageList.setSelectedValue(pageAtNameTime, true);
	}


	public DefaultListModel getPageListModel() {
		return pageListModel;
	}


	public void setPageListModel(DefaultListModel pageListModel) {
		this.pageListModel = pageListModel;
	}
	
	public void addPage(String networkNameTime){
		String nameTime=networkNameTime;
		if(nameTime==null) {
			nameTime=new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
		}
		String currentPageName="Page  @ "+nameTime;
		pageListModel.addElement(currentPageName);
		pageList.setSelectedValue(currentPageName, true);
		Page currentPage=new Page(applicationContextVariables);
		currentPage.setNameTime(nameTime);
		applicationContextVariables.getPageList().put(currentPageName,currentPage);
		applicationContextVariables.setCurrentPage(currentPage);
		if(networkNameTime==null){
			//if networkNameTime==null , this means that we did not received the addPage command from net.
			applicationContextVariables.getNetworkConnector().getMessageSender().addPage(nameTime);
		}	
	}
	
	public void removePage(String networkNameTime){
		String nameTime=networkNameTime;
		if(nameTime==null) {
			 nameTime=(String)pageList.getSelectedValue().toString().replace("Page  @ ", "");
		}
		pageListModel.removeElement("Page  @ "+nameTime);
		Page pageToRemove=applicationContextVariables.getPageList().get("Page  @ "+nameTime);
		applicationContextVariables.getPageList().remove("Page  @ "+nameTime);
		
		//set crrent page to 0
		if(applicationContextVariables.getPageList().size()>0){
			pageList.setSelectedIndex(0);
			applicationContextVariables.setCurrentPage(applicationContextVariables.getPageList().get(pageList.getSelectedValue()));		
		}
		
		if(networkNameTime==null) {
			//if pageAtNameTime==null , this means that we did not received the removePage command from net.
			applicationContextVariables.getNetworkConnector().getMessageSender().removePage(nameTime);
		}

	}
	
}
