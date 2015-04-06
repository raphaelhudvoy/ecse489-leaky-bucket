package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class NoBucketFilter implements Runnable, IOutputFilter {

	private ArrayList<Byte> buffer;
	private DataOutputStream os;
	private Socket socket;

	NoBucketFilter (Socket socket) {
		this.buffer = new ArrayList<Byte>();
		this.socket = socket;
		
		try {
			this.os = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(byte[] packet) {
		synchronized (buffer) {
			for (int i = 0; i < packet.length; i++) {
				buffer.add(packet[i]);
			}
		}
	}

	@Override
	public void run() {
		byte[] packetToSend = new byte[0];
		int bufferSize = 0;
		
		while(true) {
			
			synchronized (buffer) {
				
				// Convert Arraylist<Byte> to byte[]
				bufferSize = buffer.size();
				if (bufferSize > 0) {
					packetToSend = new byte[bufferSize];
					
					for (int i = 0; i < bufferSize; i++ ) {
						packetToSend[i] = buffer.get(i);
					}
				} else {
					packetToSend = new byte[0];
				}
			}

			try {
				if (packetToSend.length > 0) {
					os.write(packetToSend);
					buffer.clear();
					packetToSend = new byte[0];
				}
				
			} catch (IOException e) {
				
				// Client disconnected
				System.out.println("connection close");
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				break;
				
			}
			
		}
	}
}
