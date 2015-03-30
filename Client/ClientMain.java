package Client;


public class ClientMain {

	public static void main(String[] args) {
		
		Thread client1 = new Thread(new Client(false, false));
		client1.start();
		
		
	}
}
