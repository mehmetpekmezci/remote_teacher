package server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import userInterface.MainApplication;

public class RTeacherSession {

	private long creationTime=System.currentTimeMillis();
	private String sessionId;
	private ArrayList<String> commands = new ArrayList<String>();
	private MainApplication mainApplication;
   private FileOutputStream audioRecordFileOutputStream;
   String sessionDirectoryName;
	private  boolean running=true;
	private VideoRecorder videoRecorder;
	private AudioFileFormat.Type audioFileType = AudioFileFormat.Type.WAVE;
	private AudioFormat  audioFormat; 
	private  ByteArrayOutputStream audioByteArrayOutputStream=new ByteArrayOutputStream();
	private int audioFileNumber=0;		
	private ArrayList<AudioListener> audioListeners=new ArrayList<AudioListener>();
	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getSessionDirectoryName() {
		return sessionDirectoryName;
	}

	public void setSessionDirectoryName(String sessionDirectoryName) {
		this.sessionDirectoryName = sessionDirectoryName;
	}

	public ArrayList<AudioListener> getAudioListeners() {
		return audioListeners;
	}

	public void setAudioListeners(ArrayList<AudioListener> audioListeners) {
		this.audioListeners = audioListeners;
	}

	public ArrayList<VideoListener> getVideoListeners() {
		return videoListeners;
	}

	public void setVideoListeners(ArrayList<VideoListener> videoListeners) {
		this.videoListeners = videoListeners;
	}
	private ArrayList<VideoListener> videoListeners=new ArrayList<VideoListener>();
	private ArrayList<ClientListener> clientListeners=new ArrayList<ClientListener>();

	public ArrayList<ClientListener> getClientListeners() {
		return clientListeners;
	}

	public void setClientListeners(ArrayList<ClientListener> clientListeners) {
		this.clientListeners = clientListeners;
	}

	public void endSession(){
		Process avconvJpegToMP4Process=null;
		
		
		running=false;
		mainApplication.getApplicationContextVariables().getMainFrame().dispose();
		mainApplication=null;
		for(int i=0;i<clientListeners.size();i++){
			clientListeners.get(i).setRunning(false);
		}
		try {
			saveAudioFile();
//			System.out.println("generating video with avconv ...");
	//		System.out.println("rteacherAvconvJpegToMP4.sh  "+sessionDirectoryName);
			avconvJpegToMP4Process = Runtime.getRuntime().exec("rteacherAvconvJpegToMP4.sh  "+sessionDirectoryName);
	        BufferedReader processInputStreamReader=new BufferedReader(new InputStreamReader( avconvJpegToMP4Process.getErrorStream()));
	        String line;
	        while((line=processInputStreamReader.readLine())!=null){
	        	System.out.println(line);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			avconvJpegToMP4Process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


	public RTeacherSession(String sessionID,String teacher){
		this.sessionId=sessionID;
		sessionDirectoryName=System.getProperty("user.home")+"/rteacher/"+sessionID;
		File sessionDirectory=new File(sessionDirectoryName);
		if(sessionDirectory.exists()){
			File[] files=sessionDirectory.listFiles();
			for(int i=0;i<files.length;i++){
				files[i].delete();
			}
		}
		sessionDirectory.mkdirs();

		commands.add("##ADD_PAGE@@"+new SimpleDateFormat("HH:mm:ss.SSS").format(new Date())+"::::");
		videoRecorder=new VideoRecorder(this);
		
		
	     float sampleRate = 8000;
	     int sampleSizeInBits = 8;
	     int channels = 1;
	     boolean signed = true;
	     boolean bigEndian = true;
	     audioFormat =  new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}




	public VideoRecorder getVideoRecorder() {
		return videoRecorder;
	}

	public void setVideoRecorder(VideoRecorder videoRecorder) {
		this.videoRecorder = videoRecorder;
	}

	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public ArrayList<String> getCommands() {
		return commands;
	}


	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}


	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void setMainApplication(MainApplication mainApplication){
		this.mainApplication=mainApplication;
	}
	public  MainApplication getMainApplication(){
		return mainApplication;
	}
	public synchronized void recordAudio(byte[] bytes) throws IOException, UnsupportedAudioFileException{
		
		audioByteArrayOutputStream.write(bytes);
		if(audioByteArrayOutputStream.size()>100000000){
                  saveAudioFile();
                  audioByteArrayOutputStream=new ByteArrayOutputStream();
		}
	}
	public void saveAudioFile(){
		try {
			File audioFile=new File(sessionDirectoryName+"/audio."+audioFileNumber+".wav");
			if(audioFile.exists())audioFile.delete();
			audioFile.createNewFile();
			audioRecordFileOutputStream=new FileOutputStream(audioFile);
			if(audioRecordFileOutputStream!=null){
				AudioSystem.write(
						AudioSystem.getAudioInputStream(
								AudioFormat.Encoding.PCM_SIGNED,
								new AudioInputStream(new ByteArrayInputStream(audioByteArrayOutputStream.toByteArray()), audioFormat, audioByteArrayOutputStream.toByteArray().length/audioFormat.getFrameSize())
								),
								audioFileType, 
								audioRecordFileOutputStream
				);
				audioByteArrayOutputStream=new ByteArrayOutputStream();
			}
			audioRecordFileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}