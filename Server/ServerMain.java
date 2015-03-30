package Server;

import java.net.ServerSocket;
import java.net.Socket;

import Client.Config;

public class ServerMain {

   public static void main(String args[]) 
   throws Exception {
      ServerSocket ssock = new ServerSocket(Config.PORT);
      System.out.println("Listening");
      while (true) {
         Socket sock = ssock.accept();
         System.out.println("Connected");
         new Thread(new Server(sock)).start();
      }
   }

}