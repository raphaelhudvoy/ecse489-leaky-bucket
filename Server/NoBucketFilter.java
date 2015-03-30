package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class NoBucketFilter implements Runnable, IOutputFilter {

	private ByteBuffer buffer;
	private DataOutputStream os;
	private Socket socket;

	NoBucketFilter (ByteBuffer buffer, Socket socket) {
		this.buffer = buffer;
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
			buffer.put(packet);
		}
	}

	@Override
	public void run() {
		byte[] currentByte = new byte[0];
		int bufferPosition = 0;
		
		while(true) {
			
			synchronized (buffer) {
				bufferPosition = buffer.position();
				if (bufferPosition > 0) {
					currentByte = buffer.array();
					buffer.clear();

				}
			}

			if (bufferPosition > 0) {
				try {
					
					os.write(Arrays.copyOfRange(currentByte, 0, bufferPosition));
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
}
