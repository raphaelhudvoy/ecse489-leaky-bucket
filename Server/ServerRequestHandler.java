package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import Client.Client;

public class ServerRequestHandler implements Runnable{

	byte[] packet;
	Socket socket;
	int sleep;
	
	final int PACKET_SIZE = 800;
	final int SLEEP = 100;
	final int BURST_PACKET_SIZE = 120000;
	final int BURST_SLEEP = 15000;
	private ServerConfig config;
	
	ServerRequestHandler(Socket socket, ServerConfig config) {
		this.socket = socket;
		this.config = config;
	}

	@Override
	public void run() {
		try {

			DataInputStream is = new DataInputStream(socket.getInputStream());
			
			
			Boolean burst = is.readBoolean();
			
			byte number = 1;
			
			if (burst) {
				this.packet = new byte[BURST_PACKET_SIZE];
				this.sleep = BURST_SLEEP;
			} else {
				this.packet = new byte[PACKET_SIZE];
				this.sleep = SLEEP;
			}
			
			System.out.println("burst is " + burst);


			Boolean bucket = is.readBoolean();
			IOutputFilter outputFilter;

			if (bucket) {
				outputFilter = new LeakyBucket(socket, config.capacity, config.rate);
				
				Thread filterThread = new Thread((Runnable) outputFilter);
				filterThread.start();
			} else {
				outputFilter = new NoBucketFilter(socket);
				
				Thread filterThread = new Thread((Runnable) outputFilter);
				filterThread.start();
			}
			
			System.out.println("bucket is " + bucket);


			while(true) {
				try {
					
					if (socket.isClosed()) break;
					
					outputFilter.send(packet);

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
