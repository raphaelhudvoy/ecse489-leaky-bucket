package Server;

import java.net.ServerSocket;
import java.net.Socket;

import Client.ClientConfig;

public class ServerMain {

   public static void main(String args[]) throws Exception {
		ServerConfig config = new ServerConfig(args);
   
      ServerSocket ssock = new ServerSocket(ClientConfig.PORT);
      System.out.println("Listening");
      while (true) {
         Socket sock = ssock.accept();
         System.out.println("Connected");
         new Thread(new ServerRequestHandler(sock)).start();
      }
   }

}