package Client;

import java.io.File;

public class ClientMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Need config file as argument");
		}
		
		String curDir = System.getProperty("user.dir");
		int simulationTimeInSec = 60*5;

		
		ClientConfig config = new ClientConfig(new File(args[0]));
		
//		Thread[] clients = new Thread[config.numConnections];
//		
//		for (int i = 0; i < config.numConnections; i++) {			
//			clients[i] = new Thread(new Client(config.usesBurst[i], config.usesBucket[i], "Client" + i));
//			clients[i].start();	
//		}
		
		Client[] clients = new Client[config.numConnections];
		for (int i = 0; i < config.numConnections; i++) {
			clients[i] = new Client(config.usesBurst[i], config.usesBucket[i], "Client" + i);
			Thread client = new Thread(clients[i]);
			client.start();
		}
		
		try {
			Thread.sleep(simulationTimeInSec*1000);
			
			for (int i = 0; i < clients.length; i++) {
				System.out.println(clients[i].toString());
				clients[i].stop();
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
