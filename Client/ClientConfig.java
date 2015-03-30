package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ClientConfig {	
	public static final int PORT = 5005;
	public static final String HOSTNAME = "localhost";
	
	public int numConnections;
	public boolean[] usesBurst;
	public boolean[] usesBucket;
	
	public ClientConfig(File file) {
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			
			numConnections = Integer.parseInt(br.readLine());
			if (numConnections < 0) {
				throw new RuntimeException("Number of connections must be positive");
			}
			System.out.println(numConnections);
			
			usesBurst = new boolean[numConnections];
			String[] tokens = (br.readLine()).split(",", numConnections);
			
			for (int i = 0; i < numConnections; i++) {
				if (tokens[i].equalsIgnoreCase("C")) {
					usesBurst[i] = false;
				} else if (tokens[i].equalsIgnoreCase("B")) {
					usesBurst[i] = true;
				} else {
					throw new 
						RuntimeException("Second line must be C's or B's delimited by ,'s (vertical bars)");
				}
	
			}
			
			usesBucket = new boolean[numConnections];
			tokens = (br.readLine()).split(",", numConnections);
			for (int i = 0; i < numConnections; i++) {
				if (tokens[i].equalsIgnoreCase("Y")) {
					usesBucket[i] = true;
				} else if (tokens[i].equalsIgnoreCase("N")) {
					usesBucket[i] = false;
				} else {
					throw new 
						RuntimeException("Second line must be Y's or N's delimited by |'s (vertical bars)");
				}
	
			}
			
		} catch(Exception e) {
			System.out.println("Unable to read client config");
			System.out.println(e);
		}
	}
}
