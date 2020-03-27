package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Server implements Runnable {
	private int PORT = 2007;
	private ArrayList<RTeacherSession> rTeacherSessions = new ArrayList<RTeacherSession>();
	private ServerSocketChannel serverSocketChannel;

	public Server() throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
		AudioServer audioServer=new AudioServer(this);
		audioServer.start();
		VideoServer videoServer=new VideoServer(this);
		videoServer.start();
		MobileServer mobileServer=new MobileServer(this);
		mobileServer.start();
	}

	public void run() {
		while (true) {
			try {
				if (serverSocketChannel == null) {
					System.out
							.println("serverSocketChannel is null waiting 1 second ...");
					Thread.sleep(1000);
				} else {
					SocketChannel socketChannel = serverSocketChannel.accept();
					System.out.println("Server Connection from "+socketChannel.socket().getRemoteSocketAddress()+" accepted");
					socketChannel.configureBlocking(false);
					new Thread(new ClientListener(socketChannel, rTeacherSessions)).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new Thread(new Server()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public ArrayList<RTeacherSession> getrTeacherSessions() {
		return rTeacherSessions;
	}

	public void setrTeacherSessions(ArrayList<RTeacherSession> rTeacherSessions) {
		this.rTeacherSessions = rTeacherSessions;
	}

}

