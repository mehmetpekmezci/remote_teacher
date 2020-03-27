package userInterface;

import java.awt.Color;
import java.util.HashMap;

import media.MediaConnector;
import network.NetworkConnector;

public class ApplicationContextVariables {
	private  String userEmail;
	private  String teacherEmail;
	private  boolean isTeacher=false;
	private  int  width=1315;
	private  int height=720;
	private  int leftPanelWidth=315;
	private  String serverIp;
	private  String sessionId;
	private  MainFrame mainFrame;
	private  NetworkConnector networkConnector ;
	private  MediaConnector mediaConnector ;
	private  Color currentColor=Color.BLACK;
	private  String currentMod="DRAW";
	private  HashMap<String,Page> pageList=new HashMap<String,Page>();
	private  Page currentPage;
	private  int fontSize=16;
	private boolean readyToStartMedia=false;



	private  int audioPort=2008;
	private  int videoPort=2009;
	
	
	public boolean isReadyToStartMedia() {
		return readyToStartMedia;
	}
	public void setReadyToStartMedia(boolean readyToStart) {
		this.readyToStartMedia = readyToStart;
	}
	public  String getUserEmail() {
		return userEmail;
	}
	public  void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public  int getWidth() {
		return width;
	}
	public  void setWidth(int width) {
		this.width = width;
	}
	public  int getHeight() {
		return height;
	}
	public  void setHeight(int height) {
		this.height = height;
	}
	public  int getLeftPanelWidth() {
		return leftPanelWidth;
	}
	public  void setLeftPanelWidth(int leftPanelWidth) {
		this.leftPanelWidth = leftPanelWidth;
	}
	public  String getServerIp() {
		return serverIp;
	}
	public  void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public  String getSessionId() {
		return sessionId;
	}
	public  void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public  MainFrame getMainFrame() {
		return mainFrame;
	}
	public  void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	public  NetworkConnector getNetworkConnector() {
		return networkConnector;
	}
	public  void setNetworkConnector(NetworkConnector networkConnector) {
		this.networkConnector = networkConnector;
	}
	public  Color getCurrentColor() {
		return currentColor;
	}
	public  void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}
	
	public  String getCurrentMod() {
		return currentMod;
	}
	public  void setCurrentMod(String currentMod) {
		this.currentMod = currentMod;
	}
	public  HashMap<String, Page> getPageList() {
		return pageList;
	}
	public  void setPageList(HashMap<String, Page> pageList) {
		this.pageList = pageList;
	}
	public  Page getCurrentPage() {
		return currentPage;
	}
	public  void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
	}
	public  String getTeacherEmail() {
		return teacherEmail;
	}
	public  void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}
	public  boolean isTeacher() {
		return isTeacher;
	}
	public  void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}
	public  MediaConnector getMediaConnector() {
		return mediaConnector;
	}
	public  void setMediaConnector(MediaConnector mediaConnector) {
		this.mediaConnector = mediaConnector;
	}

	public int getAudioPort() {
		return audioPort;
	}
	public void setAudioPort(int audioPort) {
		this.audioPort = audioPort;
	}
	public int getVideoPort() {
		return videoPort;
	}
	public void setVideoPort(int videoPort) {
		this.videoPort = videoPort;
	}
	public  int getFontSize() {
		return fontSize;
	}

	public  void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	
}
