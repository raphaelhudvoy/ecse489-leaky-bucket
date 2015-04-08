package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Client implements Runnable{
	private Boolean burst;
	private Boolean bucket;
	private Boolean stop;
	private Socket socket;
	
	private DataOutputStream os = null;
	private DataInputStream is = null;
	
	private byte[] readData = new byte[120000];
	private String name;
	private PacketMonitor packetMonitor;
	
	public Client(Boolean burst,Boolean bucket, String name) {
		
		this.burst = burst;
		this.bucket = bucket;
		this.name = name;
		this.packetMonitor = new PacketMonitor();
		stop = false;

		try {
			socket = new Socket(ClientConfig.HOSTNAME, ClientConfig.PORT);
			
			os = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String toString () {
		String result = name + " results \n";
			result += "------------------- \n";
			result += this.packetMonitor.toString();
			
		return result;
	}
	
	public void stop () {
		this.stop = true;
	}

	
	@Override
	public void run() {
		
		try {
			os.writeByte(10);
			os.writeBoolean(burst);
			os.writeByte(20);
			os.writeBoolean(bucket);
			
			while (!stop) {
				int length = is.read(readData);
				this.packetMonitor.addPacket(Arrays.copyOfRange(readData, 0, length));
			}
			
			this.socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
