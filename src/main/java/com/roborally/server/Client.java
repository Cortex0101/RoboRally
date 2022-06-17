package com.roborally.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  /**
   * @author Lucas Eiruff
   *
   * Connects the client to the host
   *
   * @param ip the ip of the host
   * @param port the port used for the game
   */
  public void startConnection(String ip, int port) {
    try {
      clientSocket = new Socket(ip, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * @author Lucas Eiruff
   *
   * Updates the server on the game changes from the clients side
   *
   * @param msg
   * @return
   */
  public String post(String msg) {
    try {
      out.println(msg);
      System.out.println(in.ready());
      String line = in.readLine();
      StringBuilder response = new StringBuilder();

      do {
        response.append(line);
      } while ((in.ready() && (line = in.readLine()) != null));

      return response.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void stopConnection() {
    try {
      in.close();
      out.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  //This method is for testing purposes only. it starts the game as a client by default
  public static void main(String[] argvs) throws InterruptedException {
    Client client1 = new Client();
    client1.startConnection("127.0.0.1", 6666);
    //String reponse1 = client1.post("exit");
    String response2 = client1.post("GET_BOARD");
    System.out.println(response2);
  }
}
