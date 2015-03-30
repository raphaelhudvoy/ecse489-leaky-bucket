package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Request {
	private Boolean burst;
	private Boolean bucket;
	private Boolean stop;
	
	DataOutputStream os = null;
	DataInputStream is = null;
	

	public Request(Socket client) {
		
		burst = false;
		bucket = false;
		stop = false;

		try {
			os = new DataOutputStream(client.getOutputStream());
			is = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	public Response send() throws IOException {

		// Setup readBuffer
		byte[] stringInBytes = payload.getBytes();
		int stringByteSize = stringInBytes.length;

		// Send request
		sendCommandToServer();

		
		while (!end) {

			try	{
				int bytesRead = is.read(messageByte);
				messageString += new String(messageByte, 0, bytesRead);
				if (messageString.length() == payloadSize) {
					end = true;
				}
			} catch (StringIndexOutOfBoundsException e) {
				end = true;
			}
			

		}

		// os.flush();
		// is.reset();

		Response response = new Response();

		return response;

	}
	
	private void sendCommandToServer() throws IOException {

		os.writeBoolean(burst);
		os.writeBoolean(bucket);

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
	}



	
}
