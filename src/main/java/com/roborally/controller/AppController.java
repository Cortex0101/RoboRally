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
import com.roborally.view.SetupScreen;
import designpatterns.observer.Observer;
import designpatterns.observer.Subject;

import com.roborally.RoboRally;

import com.roborally.model.Board;
import com.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.util.*;

public class AppController implements Observer {

  final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
  final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey",
      "magenta");

  final private RoboRally roboRally;

  private GameController gameController;

  public AppController(RoboRally roboRally) {
    this.roboRally = roboRally;
  }

  public void newGame() {
    SetupScreen setupScreen = new SetupScreen(roboRally);
    roboRally.setScene(setupScreen.getScene());
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

  public void newGameWithoutUI(String boardName) {
    Board board = LoadBoard.loadBoard(boardName);
    gameController = new GameController(Objects.requireNonNull(board));
    gameController.startProgrammingPhase(board.resetRegisters);
  }

  private boolean validateSaveName(String name) {
    return true; // TODO: Validate the name. For instance it can not contain an extension ie .json, and it must not already exsist in the save games directory or else we can overwrite it maybe?
  }

  public void saveGame() {
    Optional<String> result;
    do {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Save game");
      dialog.setHeaderText("Type name of save file");
      result = dialog.showAndWait();
    } while (!validateSaveName(result.orElse("mysave")));

    LoadBoard.saveBoard(gameController.board, result.orElse("mysave"));
  }

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

  private void setAIPlayers() {
    Board board = gameController.board;
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      if (board.getPlayer(i).getIsAI()) {
        gameController.setAI(new RoboAI(this, board.getPlayer(i)), i);
      }
    }
  }

  public void loadGame() {
    final List<String> savedGames = getFileNames(
        "D:\\Development\\RoboRally\\src\\main\\resources\\com\\roborally\\boards\\");
    ChoiceDialog<String> dialog = new ChoiceDialog<>(savedGames.get(0), savedGames);
    dialog.setTitle("Load game");
    dialog.setHeaderText("Select game to be loaded");
    Optional<String> boardToLoad = dialog.showAndWait();

    String boardLoaded = boardToLoad.orElse("defaultboard");
    Board board = LoadBoard.loadBoard(boardLoaded);
    gameController = new GameController(Objects.requireNonNull(board));
    // TODO: Dont set here
    setAIPlayers();
    gameController.startProgrammingPhase(board.resetRegisters);
    roboRally.createBoardView(gameController);
  }

  /**
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

  public boolean isGameRunning() {
    return gameController != null;
  }


  @Override
  public void update(Subject subject) {
    // XXX do nothing for now
  }

}
