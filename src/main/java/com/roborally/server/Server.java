package com.roborally.server;

import com.roborally.RoboRally;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

  private ServerSocket serverSocket;
  private List<ClientHandler> clientHandlerList = new ArrayList<>();

  public void start(int port) {
    try {
      serverSocket = new ServerSocket(port);
      while (true) {
        ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
        clientHandler.start();
        clientHandlerList.add(clientHandler);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      stop();
    }
  }

  public void stop() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class ClientHandler extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = true;

    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
    }

    public void run() {
      try {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null && connected) {
          processMessage(inputLine);
        }

        in.close();
        out.close();
        clientSocket.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private void processMessage(String message) {
      if (message.equals("exit")) {
        out.println("bye");
        connected = false;
      }
    }
  }
}