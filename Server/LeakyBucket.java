package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class LeakyBucket implements Runnable {
	private Socket socket;
	private int size = 0;
	private final int capacity;
	private final int emissionInterval; // the rate at which the bucket leaks
	
	private boolean leaking = false;
	private DataOutputStream os;
	
	private int packetSize = 100;
	
	public LeakyBucket(Socket socket, int capacity, int emissionInterval) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Bucket capacity must be positive");
		}
		if (emissionInterval <= 0) {
			throw new IllegalArgumentException("Emission interval must be positive");
		}
		if (socket == null) {
			throw new IllegalArgumentException("Socket must be instantiated");
		}
		this.capacity = capacity;
		this.emissionInterval = emissionInterval;
		this.socket = socket;
		
		try {
			this.os = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// returns true if adding a packet was successful
	public synchronized boolean addPackets(int numPackets) {
		if (size + numPackets>= capacity) {
			return false;
		}
		size += numPackets;
		return true;
	}
	
	public synchronized void leak() {
		if (size > 0) {
			size -= packetSize;
			
			try {
				
				os.write(new byte[packetSize]);
			} catch (IOException e) {
				
				// Client disconnected
				System.out.println("connection close");
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		}
		
	}
	
	public void stopLeaking() {
		leaking = false;
	}
	
	public void run() {
		leaking = true;
		try {
			while (leaking) {
				leak();
				Thread.sleep(emissionInterval);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	

}