package Server;

public class ServerConfig {
	
	public int capacity = -1;
	public int rate = -1;

	public ServerConfig(String[] commandLineArgs) {
		if (commandLineArgs.length < 4) {
			throw new IllegalArgumentException("Not enough arguments" +
				"\nFormat : ServerMain -c [capacity] -r [leak rate]");
		}	
		
		for (int i = 0; i < commandLineArgs.length - 1; i++) {
			if (commandLineArgs[i].equals("-c")) {
				capacity = Integer.parseInt(commandLineArgs[i + 1]);
				if (capacity <= 0) {
					throw new IllegalArgumentException("Capacity must be positive");
				}
				break;
			}
		}
		for (int i = 0; i < commandLineArgs.length - 1; i++) {
			if (commandLineArgs[i].equals("-r")) {
				rate = Integer.parseInt(commandLineArgs[i + 1]);				
				if (rate <= 0) {
					throw new IllegalArgumentException("Rate must be positive");
				}
				break;
			}
		}
		
		if (capacity < 0 || rate < 0) {
			throw new RuntimeException("Unable to parse arguments");
		}
		
		
	}
}