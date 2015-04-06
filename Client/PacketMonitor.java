package Client;

import java.util.HashMap;
import java.util.Map;

public class PacketMonitor {
	private Map<Integer, Integer>  map;
	
	public PacketMonitor () {
		map = new HashMap<Integer, Integer>();
	}
	
	public void addPacket (byte[] packet) {
		
		// Increment by 1 if key already exits
		if (map.containsKey(packet.length)) {
			int value = map.get(packet.length) + 1;
			map.replace(packet.length, value);
		}
		else {
			map.put(packet.length, 1);
		}
	}
	
	public String toString () {
		
		String result = "";
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			Integer key = entry.getKey();
			Integer val = entry.getValue();
			
			result += "Recived " + val + " packets of size " + key + "\n";
		}
		
		return result;
	}
	
}
