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
	private boolean burst;
	private boolean bucket;
	
	ServerRequestHandler(Socket socket, ServerConfig config) {
		this.socket = socket;
		this.config = config;
	}

	@Override
	public void run() {
		try {

			DataInputStream is = new DataInputStream(socket.getInputStream());
			
			boolean configurated = false;
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			// Reads instruction 
			while (!configurated) {
				
				Byte code = is.readByte();
				switch (code) {
				case 10: 
					boolean value = is.readBoolean();

					if (value) {
						// Burst Mode
						this.packet = new byte[BURST_PACKET_SIZE];
						this.sleep = BURST_SLEEP;
					} else {
						this.packet = new byte[PACKET_SIZE];
						this.sleep = SLEEP;
					}
					break;
				case 20:
					bucket = is.readBoolean();
					break;
				case 30:
					configurated = true;
					System.out.println("A client has been properly configured with burst " + burst + " and bucket " + bucket );
					break;
				default:
					code = 0;
				} 
				os.writeByte(code);
				
			}
						
			
			IOutputFilter outputFilter;

			if (bucket) {
				outputFilter = new LeakyBucket(socket, config);
				
				Thread filterThread = new Thread((Runnable) outputFilter);
				filterThread.start();
			} else {
				outputFilter = new NoBucketFilter(socket);
				
				Thread filterThread = new Thread((Runnable) outputFilter);
				filterThread.start();
			}
			

			while(true) {
				try {
					
					if (socket.isClosed()) break;
					
					System.out.println("sending ... " + packet.length);
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
