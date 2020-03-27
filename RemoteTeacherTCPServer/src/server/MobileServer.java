package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class MobileServer  extends Thread {
    private int PORT=2010;
	Server server;
	ServerSocketChannel serverSocketChannel;


	public MobileServer(Server server) {
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
					System.out
							.println("serverSocketChannel is null waiting 1 second ...");
					Thread.sleep(1000);
				}
				socketChannel = serverSocketChannel.accept();
	//			System.out.println("MobileServer Connection from "+socketChannel.socket().getRemoteSocketAddress()+" accepted");
				socketChannel.configureBlocking(false);
				MobileListener mobileListener=new MobileListener(server, socketChannel);
				mobileListener.start();
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
