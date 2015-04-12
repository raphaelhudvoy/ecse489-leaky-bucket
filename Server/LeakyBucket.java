package Server;

import java.awt.List;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class LeakyBucket implements Runnable, IOutputFilter {
	private Socket socket;
	private int size = 0;
	private final int capacity;
	private final int emissionInterval; // the rate at which the bucket leaks
	
	private boolean leaking = false;
	private DataOutputStream os;
	
	private int leakingPacketSize = 100;
	private ArrayList<Byte> buffer;
	
	public LeakyBucket(Socket socket, ServerConfig config) {
		if (config.capacity <= 0) {
			throw new IllegalArgumentException("Bucket capacity must be positive");
		}
		if (config.rate <= 0) {
			throw new IllegalArgumentException("Emission interval must be positive");
		}
		if (socket == null) {
			throw new IllegalArgumentException("Socket must be instantiated");
		}
		this.capacity = config.capacity;
		this.emissionInterval = config.rate;
		this.leakingPacketSize = config.packetSize;
		this.socket = socket;
		this.buffer = new ArrayList<Byte>();
;
		
		try {
			this.os = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// returns true if adding nothing has leaked
	public synchronized boolean addPackets(byte[] packet) {
		int packetSize = packet.length;
		
		// Bucket is full
		if (size >= capacity) {
			return false;
		}
		
		// Bucket is not full but there is not  enough
		// place to fit the whole packet (leaking)
		else if (size + packetSize >= capacity) {
			int remainingRoom = capacity - size;
			
			byte[] newPacket = Arrays.copyOfRange(packet, 0, remainingRoom - 1);
			enqueuePacket(newPacket);
			size = capacity;
			return false;
		}
		
		else {
			size += packetSize;
			enqueuePacket(packet);
		}
		return true;
	}
	
	private void enqueuePacket(byte[] packet) {
		
		synchronized (buffer) {
			for (int i = 0; i < packet.length; i++) {
				buffer.add(packet[i]);
			}
		}
	}

	public synchronized void leak() {
		if (size > 0) {
			size -= leakingPacketSize;
			
			try {
				
				byte[] packetToLeak = new byte[leakingPacketSize];
 				synchronized(buffer) {
 					
 					// make sure the buffer contains more than the leaking size. Otherwise send the whole buffer
 					int leaking_size = buffer.size() > leakingPacketSize ? leakingPacketSize : buffer.size();
 					
 					
 					// Create the packet to send
					for (int i = 0; i < leaking_size; i++) {
						packetToLeak[i] = buffer.get(i);
					}		
					
					// Remove send byte from the buffer
					if (leaking_size == leakingPacketSize) {
						buffer = new ArrayList<Byte>(buffer.subList(leakingPacketSize, buffer.size()));
					} else {
						buffer.clear();
					}
				}
 				
 				//send the packet
 				if (packetToLeak.length > 0) {
 					os.write(packetToLeak);
 				}

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

	@Override
	public void send(byte[] packet) {
		addPackets(packet);
		
	}
	
	

}