package userInterface;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class UserListPanel extends JPanel {
	 JList userList;
	 DefaultListModel  userListModel;
	 
	 public UserListPanel(String userEmail){
		 userList=new JList();
		 userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		 userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		 userList .setVisibleRowCount(-1);
		 userListModel=new DefaultListModel();
		 userList.setModel(userListModel);
		 userListModel.addElement(userEmail);
		 JScrollPane userListScrollPane=new JScrollPane(userList);
		userListScrollPane.setPreferredSize(new Dimension(250,100));
		 add(userListScrollPane);
	 }
	 public void addUser(String email){
		 if(!email.trim().equals("recorder@localhost"))
		 userListModel.addElement(email);
	 }
	 public void removeUser(String email){
		 userListModel.removeElement(email);
	 }
}
