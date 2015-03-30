package Client;

import java.io.File;

public class ClientMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Need config file as argument");
		}
		
		String curDir = System.getProperty("user.dir");

		
		ClientConfig config = new ClientConfig(new File(args[0]));
		
		Thread[] clients = new Thread[config.numConnections];
		
		for (int i = 0; i < config.numConnections; i++) {			
			clients[i] = new Thread(new Client(config.usesBurst[i], config.usesBucket[i]));
			clients[i].start();	
		}
	}
	
}
