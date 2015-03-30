package Server;

import java.net.Socket;

public class LeakyBucket extends Thread {
	private Socket socket;
	private int size = 0;
	private final int capacity;
	private final int emissionInterval; // the rate at which the bucket leaks
	
	private boolean leaking = false;
	
	public LeakyBucket(Socket socket, int capacity, int emissionInterval) {
		if (capacity <= 0) {
			throw new IllegalArgumentException();
		}
		if (emissionInterval <= 0) {
			throw new IllegalArgumentException();
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
		size--;
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