package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerRequestHandler implements Runnable{

	byte[] packet;
	Socket socket;
	int sleep;
	
	ServerRequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {

			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			DataInputStream is = new DataInputStream(socket.getInputStream());

			Boolean burst = is.readBoolean();
			
			if (burst) {
				this.packet = new byte[120000];
				this.sleep = 15000;
			} else {
				this.packet = new byte[800];
				this.sleep = 100;
			}

			Boolean bucket = is.readBoolean();
			System.out.println("bucket is " + bucket);


			while(true) {
				try {
					
					os.write(packet);
					Thread.sleep(sleep);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					
					// Client disconnected
					System.out.println("connection close");
					socket.close();
					break;
				}
			}


		}
		catch (IOException e) {
			System.out.println(e);
		}

	}

}
