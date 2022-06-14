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
import com.roborally.view.DialogFacade;
import com.roborally.view.SetupScreen;
import designpatterns.observer.Observer;
import designpatterns.observer.Subject;

import com.roborally.RoboRally;

import com.roborally.model.Board;
import com.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;

import java.io.File;
import java.util.*;

public class AppController implements Observer {
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

    // TODO: The server should communicate with the client directly somehow, instead of having this mouse event be the trigger.
    roboRally.getPrimaryScene().setOnMouseMoved(e -> {
      if (roboRally.readyToUpdateBoard) {
        gameController.roboRally.readyToUpdateBoard = false;
        setGame();
      }
    });
  }

  /**
   * @author Lucas Eiruff
   *
   * Updates the game state on the hosts side.
   */
  public void setGame() {
    if (roboRally.isHost) {
      startGame(loadBoard("tempSave"));
    } else if (roboRally.isClient) {
      startGame(loadBoard(roboRally.client.post("GET_BOARD")));
      roboRally.client.post("SUBTRACT_READY");
      gameController.roboRally = roboRally;
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * Saves the game state into a .json file
   */
  public void saveGame(String name) {
    LoadBoard.saveBoard(gameController.board, name);
  }


  public Board loadBoard(String name) {
    return LoadBoard.loadBoard(name);
  }

  /**
   * @author Lucas Eiruff
   *
   * Starts a new game with the board.
   * @param board the board to start the new game with
   */
  public void startGame(Board board) {
    gameController = new GameController(Objects.requireNonNull(board));
    setAIPlayers(false);
    gameController.roboRally = roboRally;
    gameController.startProgrammingPhase(board.resetRegisters);
    roboRally.createBoardView(gameController);
  }

  /**
   * @author Lucas Eiruff
   *
   * Prompts the user with a choice dialog
   * The user choose on of the saved games in the boards folder.
   *
   * @return the name of the save game chosen by the user
   */
  public String getLoadNameFromUser() {
    final List<String> savedGames = getFileNames(System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards\\");
    ChoiceDialog<String> dialog = new ChoiceDialog<>(savedGames.get(0), savedGames);
    dialog.setTitle("Load game");
    dialog.setHeaderText("Select game to be loaded");
    Optional<String> boardToLoad = dialog.showAndWait();
    return boardToLoad.orElseThrow();
  }

  /**
   * @author Lucas Eiruff
   *
   * Prompts the user with a text input dialog.
   * The user should enter a save game name.
   * The prompt will be prompted untill the user enters a valid name.
   * @return the string the user entered
   */
  public String getSaveNameFromUser() {
    String result;
    do {
      result = DialogFacade.newTextInputDialog("Save game", "Type name of save file");
    } while (!checkForIllegalCharacters(result));

    return result;
  }

  /**
   * @author Lucas Eiruff
   *
   * Instantiates a new game without a UI, used for testing and AI players
   */
  public void newGameWithoutUI(String boardName, boolean useTempBoard) {
    Board board = loadBoard(useTempBoard ? "tempBoard" : boardName);
    gameController = new GameController(Objects.requireNonNull(board));
    gameController.startProgrammingPhase(board.resetRegisters);
  }

  /**
   * @author Lucas Eiruff
   *
   * Stop playing the current game, giving the user the option to save the game or to cancel
   * stopping the game.
   */
  public void stopGame() {
    if (!isGameRunning())
      return;

    saveGame(getSaveNameFromUser());
    gameController = null;
    roboRally.createBoardView(null);
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
    return name.matches("[a-zA-Z]+");
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

  /**
   * @author August Hjortholm
   *
   * Opens an information window showing which player has won, then exits the program
   */
  public void endGame(String player) {
    if (!isGameRunning())
      return;

    if (!DialogFacade.newInformationAlert("Victory!", player + "has won!")) {
      return;
    }

    Platform.exit();
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
   * Opens a prompt to exit the game, then ask to save the game, then exits the program
   */
  public void exit() {
    if (!isGameRunning())
      return;

    if (DialogFacade.newConfirmationAlert("Exit RoboRally?",
        "Are you sure you want to exit RoboRally?")) {
      return;
    }

    stopGame();
    if (roboRally.isHost) {
      roboRally.server.stop();
    }
    if (roboRally.isClient) {
      roboRally.client.stopConnection();
    }
    Platform.exit();
  }

  // TODO: This method probably belongs in some client class
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

  public GameController getGameController() {
    return gameController;
  }

  public void setGameController(GameController gameController) {
    this.gameController = gameController;
  }

  public boolean isGameRunning() {
    return gameController != null;
  }

  @Override
  public void update(Subject subject) {}
}
