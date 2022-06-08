package com.roborally.server;

import com.roborally.RoboRally;
import com.roborally.StartRoboRally;
import com.roborally.fileaccess.LoadBoard;
import com.roborally.model.Command;
import com.roborally.model.CommandCard;
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
  public RoboRally roboRally;
  private int playersReady = 0;
  private boolean everyoneReady = false;

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

      if (message.equals("GET_BOARD")) {
        roboRally.getAppController().storeGame();
        String jsonBoard = LoadBoard.getBoardContent();
        out.print(jsonBoard);
        out.println();
      }

      if (message.equals("SUBTRACT_READY")) {
        --playersReady;
        if (playersReady == 0) {
          everyoneReady = false;
        }
        out.println("OK");
      }

      if (message.equals("PLAYER_READY")) {
        ++playersReady;
        if (playersReady == clientHandlerList.size()) {
          everyoneReady = true;
        }
        out.println("OK");
      }

      if (message.equals("IS_EVERYONE_READY")) {
        if (everyoneReady) {
          out.println("YES");
        } else {
          out.println("NO");
        }
      }

      if (message.equals("GET_CLIENT_NUM")) {
        out.println(clientHandlerList.size());
      }

      if (message.startsWith("C")) {
        String[] card = message.split(" ");
        Command command = null;
        System.out.println(card[3]);
        if (card.length > 4) {
          switch (card[3] + " " + card[4]) {
            case "Move 1" -> {
              command = Command.MOVE1;
            }
            case "Move 2" -> {
              command = Command.MOVE2;
            }
            case "Move 3" -> {
              command = Command.MOVE3;
            }
            case "Turn Right" -> {
              command = Command.RIGHT;
            }
            case "Turn Left" -> {
              command = Command.LEFT;
            }
            case "Left OR" -> {
              command = Command.OPTION_LEFT_RIGHT;
            }
            default -> {
              command = Command.U_TURN;
            }
          }
          roboRally.getAppController().getGameController().board.getPlayer(
                  Integer.parseInt(card[1])).getProgramField(Integer.parseInt(card[2]))
              .setCard(new CommandCard(command));
          out.println("OK");
        } else {
          command = Command.U_TURN;
          roboRally.getAppController().getGameController().board.getPlayer(
                  Integer.parseInt(card[1])).getProgramField(Integer.parseInt(card[2]))
              .setCard(new CommandCard(command));
          out.println("OK");
        }
      }
    }
  }
}