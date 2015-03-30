package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Server implements Runnable{

	byte[] packet;
	Socket socket;
	
	Server(Socket socket) {
		this.socket = socket;
		this.packet = new byte[800];
	}

	@Override
	public void run() {
		try {

			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			DataInputStream is = new DataInputStream(socket.getInputStream());

			Boolean burst = is.readBoolean();
			System.out.println("burst is " + burst);

			Boolean bucket = is.readBoolean();
			System.out.println("bucket is " + bucket);


			while(true) {
				try {
					os.write(packet);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		}
		catch (IOException e) {
			System.out.println(e);
		}

	}

}
