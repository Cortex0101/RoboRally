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

  public void startConnection(String ip, int port) {
    try {
      clientSocket = new Socket(ip, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public String post(String msg) {
    try {
      out.println(msg);
      String response = in.readLine();
      return response;
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

  public static void main(String argvs[]) throws InterruptedException {
    Client client1 = new Client();
    Client client2 = new Client();
    client1.startConnection("127.0.0.1", 6666);
    client2.startConnection("127.0.0.1", 6666);
  }
}
