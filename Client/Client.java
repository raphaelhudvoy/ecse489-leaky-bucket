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
			
			initializationInformation();
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void initializationInformation() {
		System.out.println(name + ": burst is " + burst + " and bucket is " + bucket);
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
			
			// Configure burst
			boolean confirmed = false;
			while (!confirmed) {
				os.writeByte(10);
				os.writeBoolean(burst);
				
				byte value = is.readByte();
				
				if (value == 10) confirmed = true;
			}
			
			// Configure bucket
			confirmed = false;
			while (!confirmed) {
				os.writeByte(20);
				os.writeBoolean(bucket);
				
				byte value = is.readByte();
				
				if (value == 20) confirmed = true;
			}
			
			// Start sending command
			confirmed = false;
			while (!confirmed) {
				os.writeByte(30);
				
				byte value = is.readByte();
				
				if (value == 30) confirmed = true;
			}

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
