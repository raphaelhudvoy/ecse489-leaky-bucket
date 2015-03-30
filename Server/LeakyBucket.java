package Server;

import java.net.Socket;

public class LeakyBucket implements Runnable, IOutputFilter {
	private Socket socket;
	private int size = 0;
	private final int capacity;
	private final int emissionInterval; // the rate at which the bucket leaks
	
	private boolean leaking = false;
	
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
			size--;
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

	@Override
	public void send(byte[] packet) {
		
		
	}
	
	

}