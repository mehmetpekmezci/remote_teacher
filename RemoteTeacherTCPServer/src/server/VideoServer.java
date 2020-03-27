package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class VideoServer  extends Thread{
	private int PORT=2009;
	Server server;
	ServerSocketChannel serverSocketChannel;

	
	public VideoServer(Server server){
		this.server = server;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				SocketChannel socketChannel;
				if (serverSocketChannel == null) {
					Thread.sleep(1000);
				}
				socketChannel = serverSocketChannel.accept();
				socketChannel.configureBlocking(false);
				VideoListener videoListener=new VideoListener(server, socketChannel);
				videoListener.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ServerSocketChannel getServerSocketChannel() {
		return serverSocketChannel;
	}

	public void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}


	
}
