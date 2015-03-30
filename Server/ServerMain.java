package Sever;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import Client.Config;

public class ServerMain implements Runnable {
   Socket csocket;
   ServerMain(Socket csocket) {
      this.csocket = csocket;
   }

   public static void main(String args[]) 
   throws Exception {
      ServerSocket ssock = new ServerSocket(Config.PORT);
      System.out.println("Listening");
      while (true) {
         Socket sock = ssock.accept();
         System.out.println("Connected");
         new Thread(new ServerMain(sock)).start();
      }
   }
   public void run() {
      try {
//         PrintStream pstream = new PrintStream(csocket.getOutputStream());
//         for (int i = 100; i >= 0; i--) {
//            pstream.println(i + 
//            " bottles of beer on the wall");
//         }
//         pstream.close();
    	  
    	  DataOutputStream os = new DataOutputStream(csocket.getOutputStream());
    	  DataInputStream is = new DataInputStream(csocket.getInputStream());

    	  int length = is.read(new byte[2]);
			System.out.println("packet has " + length + " of length");
			
    	  os.write(new byte[800]);
    		
         csocket.close();
      }
      catch (IOException e) {
         System.out.println(e);
      }
   }
}