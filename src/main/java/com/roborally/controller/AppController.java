/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.roborally.controller;

import com.roborally.fileaccess.LoadBoard;
import com.roborally.model.CommandCardField;
import com.roborally.view.SetupScreen;
import designpatterns.observer.Observer;
import designpatterns.observer.Subject;

import com.roborally.RoboRally;

import com.roborally.model.Board;
import com.roborally.model.Player;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.util.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AppController implements Observer {

  final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
  final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey",
      "magenta");

  final private RoboRally roboRally;

  private GameController gameController;

  /**
   * Manages the app, start, load, exit etc
   *
   * @param roboRally The game of roborally
   */
  public AppController(RoboRally roboRally) {
    this.roboRally = roboRally;
  }
  /**
   * @author Lucas Eiruff
   *
   * Instantiates a new game and sets all the parameters
   */
  public void newGame() {
    SetupScreen setupScreen = new SetupScreen(roboRally);
    roboRally.setScene(setupScreen.getScene());

    roboRally.getPrimaryScene().setOnMouseMoved(e -> {
      if (roboRally.readyToUpdateBoard) {
        gameController.roboRally.readyToUpdateBoard = false;
        setGame();
      }
    });

    roboRally.getPrimaryScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.S) {
          storeGame();
          event.consume();
        }
        if (event.getCode() == KeyCode.L) {
          setGame();
          event.consume();
        }
        if (event.getCode() == KeyCode.U) {
          try {
            uploadProgram();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  /**
   * @author Lucas Eiruff
   *
   * Uploads the game state from the clients. The server responds "OK" if the update is received
   */
  public void uploadProgram() throws Exception {
    if (roboRally.isClient) {
      Board board = this.gameController.board;
      final int playerNum = roboRally.clientNum - 1;
      for (int i = 0; i < Player.NO_REGISTERS; i++) {
        Player player = board.getPlayer(playerNum);
        CommandCardField field = player.getProgramField(i);
        String name = field.getCard().getName();
        String response = roboRally.client.post("C " + playerNum + " " + i  + " " + name);
        if (!response.equals("OK")) {
          throw new Exception("Failed to update player program");
        }
      }
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * stores the game state in a temporary .json file
   */
  public void storeGame() {
    LoadBoard.saveBoard(gameController.board, "tempSave");
  }

  /**
   * @author Lucas Eiruff
   *
   * Updates the game state on the hosts side.
   */
  public void setGame() {
    if (roboRally.isHost) {
      Board board = LoadBoard.loadBoard("tempSave");
      gameController = new GameController(Objects.requireNonNull(board));
      setAIPlayers(false);
      gameController.startProgrammingPhase(board.resetRegisters);
      roboRally.createBoardView(gameController);
    } else if (roboRally.isClient) {
      String jsonBoard = roboRally.client.post("GET_BOARD");
      String ignore = roboRally.client.post("SUBTRACT_READY");
      Board board = LoadBoard.loadBoardFromJson(jsonBoard);
      gameController = new GameController(Objects.requireNonNull(board));
      gameController.roboRally = roboRally;
      setAIPlayers(false);
      gameController.startProgrammingPhase(board.resetRegisters);
      roboRally.createBoardView(gameController);
    }
  }

  /*
  public void newGame() {
    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0),
        PLAYER_NUMBER_OPTIONS);
    dialog.setTitle("Player number");
    dialog.setHeaderText("Select number of players");
    Optional<Integer> result = dialog.showAndWait();

    if (result.isPresent()) {
      if (gameController != null) {
        // The UI should not allow this, but in case this happens anyway.
        // give the user the option to save the game or abort this operation!
        if (!stopGame()) {
          return;
        }
      }

      // XXX the board should eventually be created programmatically or loaded from a file
      //     here we just create an empty board with the required number of players.
      Board board = new Board(8, 8);
      gameController = new GameController(board);
      int no = result.get();
      for (int i = 0; i < no; i++) {
        Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1),
            board.getSpace(i % board.width, i));
        board.addPlayer(player);
        player.setSpace(board.getSpace(i % board.width, i));
      }

      // XXX: V2
      // board.setCurrentPlayer(board.getPlayer(0));
      gameController.startProgrammingPhase(board.resetRegisters);

      roboRally.createBoardView(gameController);
    }
  }
   */

  public GameController getGameController() {
    return gameController;
  }

  public void setGameController(GameController gameController) {
    this.gameController = gameController;
  }

  /**
   * @author Lucas Eiruff
   *
   * Instantiates a new game without a UI, used for testing and AI players
   */
  public void newGameWithoutUI(String boardName, boolean useTempBoard) {
    Board board = LoadBoard.loadBoard(useTempBoard ? "tempBoard" : boardName);//LoadBoard.loadBoard(boardName);
    gameController = new GameController(Objects.requireNonNull(board));
    gameController.startProgrammingPhase(board.resetRegisters);
  }

  /**
   * @author August Hjortholm
   *
   * checks a string for any characters not in the alphabet. this method is mainly to prevent issues when saving a game
   *
   * @param name the name of the file
   * @return true if the string is legal, false otherwise
   */
  private boolean checkForIllegalCharacters(String name) {
    byte[] characters = name.getBytes();
    for (int i = 0; i < characters.length; i++) {
      if (!((characters[i] >= 65 && characters[i] <= 90) ||
              (characters[i] >= 97 && characters[i] <= 122)) ||
              characters[i] == 0){
        System.out.println("error: invalid character");
        return false;
      }
    }
    return true;
  }

  /**
   * @author Lucas Eiruff
   *
   * Saves the game state into a .json file
   */
  public void saveGame() {
    Optional<String> result;
    do {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Save game");
      dialog.setHeaderText("Type name of save file");
      result = dialog.showAndWait();
    } while (!checkForIllegalCharacters(result.orElse("mysave")));

    LoadBoard.saveBoard(gameController.board, result.orElse("mysave"));
  }

  /**
   * @author Lucas Eiruff
   *
   * returns all the names of the files saved in the boards folder
   * @param directory the directory of the boards folder
   * @return a list of all file names
   */
  private List<String> getFileNames(String directory) {
    List<String> fileNames = new ArrayList<>();
    File folder = new File(directory);
    File[] listOfFiles = folder.listFiles();
    for (File file : Objects.requireNonNull(listOfFiles)) {
      if (file.isFile()) {
        fileNames.add(file.getName().substring(0, file.getName().length() - 5));
      }
    }
    return fileNames;
  }

  public void setAIPlayers(boolean fromNew) {
    Board board = gameController.board;
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      if (board.getPlayer(i).getIsAI()) {
        gameController.setAI(new RoboAI(this, board.getPlayer(i), fromNew), i);
      }
    }
  }
  /**
   * @author Lucas Eiruff
   *
   * Loads a the board from a .json file
   */
  public void loadGame() {
    final List<String> savedGames = getFileNames(System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards\\");
    ChoiceDialog<String> dialog = new ChoiceDialog<>(savedGames.get(0), savedGames);
    dialog.setTitle("Load game");
    dialog.setHeaderText("Select game to be loaded");
    Optional<String> boardToLoad = dialog.showAndWait();

    String boardLoaded = boardToLoad.orElse("defaultboard");
    Board board = LoadBoard.loadBoard(boardLoaded);
    gameController = new GameController(Objects.requireNonNull(board));
    setAIPlayers(false);
    gameController.startProgrammingPhase(board.resetRegisters);
    roboRally.createBoardView(gameController);
  }

  /**
   * @author Lucas Eiruff
   *
   * Stop playing the current game, giving the user the option to save the game or to cancel
   * stopping the game. The method returns true if the game was successfully stopped (with or
   * without saving the game); returns false, if the current game was not stopped. In case there is
   * no current game, false is returned.
   *
   * @return true if the current game was stopped, false otherwise
   */
  public boolean stopGame() {
    if (gameController != null) {

      // here we save the game (without asking the user).
      saveGame();

      gameController = null;
      roboRally.createBoardView(null);
      return true;
    }
    return false;
  }

  /**
   * @author Lucas Eiruff
   *
   * Opens a prompt to exit the game, then ask to save the game, then exits the program
   */
  public void exit() {
    if (gameController != null) {
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Exit RoboRally?");
      alert.setContentText("Are you sure you want to exit RoboRally?");
      Optional<ButtonType> result = alert.showAndWait();

      if (result.isEmpty() || result.get() != ButtonType.OK) {
        return; // return without exiting the application
      }
    }

    // If the user did not cancel, the RoboRally application will exit
    // after the option to save the game
    if (gameController == null || stopGame()) {
      Platform.exit();
    }
  }
  /**
   * @author August Hjortholm
   *
   * Opens an information window showing which player has won, then exits the program
   */
  public void endGame(String player) {
    if (gameController != null) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Victory!");
      alert.setContentText(player + " Has won!");
      Optional<ButtonType> result = alert.showAndWait();

      if (result.isEmpty() || result.get() != ButtonType.OK) {
        return; // return without exiting the application
      }
    }
    Platform.exit();
  }

  public boolean isGameRunning() {
    return gameController != null;
  }

  @Override
  public void update(Subject subject) {

  }
}
