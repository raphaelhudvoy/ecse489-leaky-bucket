package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import Client.Client;

public class ServerRequestHandler implements Runnable{

	byte[] packet;
	Socket socket;
	int sleep;
	
	final int PACKET_SIZE = 800;
	final int SLEEP = 100;
	final int BURST_PACKET_SIZE = 120000;
	final int BURST_SLEEP = 15000;
	
	ServerRequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {

			DataInputStream is = new DataInputStream(socket.getInputStream());
			
			ByteBuffer buffer = ByteBuffer.allocate(BURST_PACKET_SIZE*4);
			
			Boolean burst = is.readBoolean();
			
			if (burst) {
				this.packet = new byte[BURST_PACKET_SIZE];
				this.sleep = BURST_SLEEP;
			} else {
				this.packet = new byte[PACKET_SIZE];
				this.sleep = SLEEP;
			}
			
			System.out.println("burst is " + burst);


			Boolean bucket = is.readBoolean();
			
			if (bucket) {
				
			} else {
				Thread filterThread = new Thread(new NoBucketFilter(buffer, socket));
				filterThread.start();
			}
			
			System.out.println("bucket is " + bucket);


			while(true) {
				try {
					
					if (socket.isClosed()) break;
					
					synchronized (buffer) {
						System.out.println("send packet");
						buffer.put(packet);
					}
					
					Thread.sleep(sleep);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}


		}
		catch (IOException e) {
			System.out.println(e);
		}

	}

}
