package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client implements Runnable{
	private Boolean burst;
	private Boolean bucket;
	private Boolean stop;
	private Socket socket;
	
	private DataOutputStream os = null;
	private DataInputStream is = null;
	
	public Client(Boolean burst,Boolean bucket) {
		
		this.burst = burst;
		this.bucket = bucket;
		stop = false;

		try {
			socket = new Socket(Config.HOSTNAME, Config.PORT);
			
			os = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void activateBurst() {
		burst = true;
	}
	
	public void deactivateBurst() {
		burst = false;
	}
	
	public void activateBucket() {
		bucket = true;
	}
	
	public void deactivateBucket() {
		bucket = false;
	}

	
	@Override
	public void run() {
		
		try {
			os.writeBoolean(burst);
			os.writeBoolean(bucket);
			
			while (!stop) {
				is.read();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
