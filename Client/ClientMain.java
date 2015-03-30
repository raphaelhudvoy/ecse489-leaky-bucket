package Client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {

	public static void main(String[] args) {
		try {
			// Connecting to the server
			Socket client = new Socket(Config.HOSTNAME, Config.PORT);

			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
