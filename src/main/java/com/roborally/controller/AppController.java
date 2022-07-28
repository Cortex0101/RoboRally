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

import com.roborally.RoboRally;
import com.roborally.fileaccess.LoadBoard;
import com.roborally.fileaccess.LoadBoard.BoardConfig;
import com.roborally.fileaccess.LoadBoard.DefaultBoard;
import com.roborally.model.Board;
import com.roborally.model.CommandCardField;
import com.roborally.model.Player;
import com.roborally.view.DialogFacade;
import com.roborally.view.SetupScreen;
import designpatterns.observer.Observer;
import designpatterns.observer.Subject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javax.swing.LookAndFeel;

public class AppController implements Observer {

  public final RoboRally roboRally;
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
   * <p>
   * Instantiates a new game and sets all the parameters
   */
  public void newGame() {
    SetupScreen setupScreen = new SetupScreen(roboRally);
    roboRally.setScene(setupScreen.getScene());

    // TODO: The server should communicate with the client directly somehow, instead of having this mouse event be the trigger.
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
      if (roboRally.readyToUpdateBoard) {
        gameController.roboRally.readyToUpdateBoard = false;
        setGame();
      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    /*
    roboRally.getPrimaryScene().setOnMouseMoved(e -> {
      if (roboRally.readyToUpdateBoard) {
        gameController.roboRally.readyToUpdateBoard = false;
        setGame();
      }
    });

     */

    roboRally.getPrimaryScene().setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode() == KeyCode.Z && keyEvent.isShiftDown()) {
        this.gameController.playerCommandManager.redoLast();
      } else if (keyEvent.getCode() == KeyCode.Z) {
        this.gameController.playerCommandManager.undoLast();
      }
    });
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Updates the game state on the hosts side.
   */
  public void setGame() {
    if (roboRally.isHost) {
      startGame(loadBoard("tempSave"));
    } else if (roboRally.isClient) {
      String json = roboRally.client.post("GET_BOARD");
      System.out.println(json.indexOf("com.roborally.controller.CheckPoint"));
      if (json.indexOf("com.roborally.controller.CheckPoint") == 137) {
        LoadBoard.playerStartingPositions[0] = new int[]{11, 1};
        LoadBoard.playerStartingPositions[1] = new int[]{12, 3};
        LoadBoard.playerStartingPositions[2] = new int[]{11, 4};
        LoadBoard.playerStartingPositions[3] = new int[]{11, 5};
        LoadBoard.playerStartingPositions[4] = new int[]{12, 6};
        LoadBoard.playerStartingPositions[5] = new int[]{11, 8};
        System.out.println("hard map!");
      } else {
        LoadBoard.playerStartingPositions[0] = new int[]{1, 0};
        LoadBoard.playerStartingPositions[1] = new int[]{0, 2};
        LoadBoard.playerStartingPositions[2] = new int[]{1, 4};
        LoadBoard.playerStartingPositions[3] = new int[]{1, 5};
        LoadBoard.playerStartingPositions[4] = new int[]{0, 7};
        LoadBoard.playerStartingPositions[5] = new int[]{1, 9};
      }
      startGame(LoadBoard.loadBoardFromJson(json));
      roboRally.client.post("SUBTRACT_READY");
      gameController.roboRally = roboRally;
    }
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Saves the game state into a .json file
   */
  public void saveGame(String name) {
    LoadBoard.saveBoard(gameController.board, name);
  }


  public Board loadBoard(String name) {
    return LoadBoard.loadBoardFromFile(name);
  }

  /**
   * @param board the board to start the new game with
   * @author Lucas Eiruff
   * <p>
   * Starts a new game with the board.
   */
  public void startGame(Board board) {
    gameController = new GameController(Objects.requireNonNull(board));
    setAIPlayers(false);
    gameController.roboRally = roboRally;
    gameController.startProgrammingPhase(board.resetRegisters);
    roboRally.createBoardView(gameController);
  }

  /**
   * @return the name of the save game chosen by the user
   * @author Lucas Eiruff
   * <p>
   * Prompts the user with a choice dialog The user choose on of the saved games in the boards
   * folder.
   */
  public String getLoadNameFromUser() {
    final List<String> savedGames = getFileNames(
        System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards\\");
    return DialogFacade.newChoiceDialog(savedGames, "Load game", "Select game to be loaded");
  }

  /**
   * @return the string the user entered
   * @author Lucas Eiruff
   * <p>
   * Prompts the user with a text input dialog. The user should enter a save game name. The prompt
   * will be prompted untill the user enters a valid name.
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
   * <p>
   * Instantiates a new game without a UI, used for testing and AI players
   */
  public void newGameWithoutUI(String boardName, boolean useTempBoard) {
    Board board = loadBoard(useTempBoard ? "tempBoard" : boardName);
    gameController = new GameController(Objects.requireNonNull(board));
    gameController.startProgrammingPhase(board.resetRegisters);
  }

  public void newGameWithTestBoard() {
    BoardConfig config = new BoardConfig(2, 0);
    config.playerNames[0] = "player1";
    config.playerNames[1] = "player2";
    config.playerColors[0] = "red";
    config.playerColors[1] = "blue";
    Board board = LoadBoard.loadDefaultBoard(config, DefaultBoard.test);
    gameController = new GameController(Objects.requireNonNull(board));
    gameController.startProgrammingPhase(board.resetRegisters);
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Stop playing the current game, giving the user the option to save the game or to cancel
   * stopping the game.
   */
  public void stopGame() {
    if (!isGameRunning()) {
      return;
    }

    saveGame(getSaveNameFromUser());
    gameController = null;
    roboRally.createBoardView(null);
  }

  /**
   * @param name the name of the file
   * @return true if the string is legal, false otherwise
   * @author August Hjortholm
   * <p>
   * checks a string for any characters not in the alphabet. this method is mainly to prevent issues
   * when saving a game
   */
  private boolean checkForIllegalCharacters(String name) {
    if (name.isEmpty()) {
      return true; // allow empty name
    }

    return name.matches("[a-zA-Z]+");
  }

  /**
   * @param directory the directory of the boards folder
   * @return a list of all file names
   * @author Lucas Eiruff
   * <p>
   * returns all the names of the files saved in the boards folder
   */
  private List<String> getFileNames(String directory) {
    File[] fileArray = new File(directory).listFiles(File::isFile);
    List<String> fileNames = new ArrayList<>();
    for (File file : Objects.requireNonNull(fileArray)) {
      fileNames.add(file.getName().substring(0, file.getName().length() - 5));
    }
    return fileNames;
  }

  /**
   * @author August Hjortholm
   * <p>
   * Opens an information window showing which player has won, then exits the program
   */
  public void endGame(String player) {
    if (!isGameRunning()) {
      return;
    }

    if (!DialogFacade.newInformationAlert("Victory!", player + "has won!")) {
      return;
    }

    Platform.exit();
  }

  public void setAIPlayers(boolean fromNew) {
    Board board = gameController.board;
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      if (board.getPlayer(i).isAI()) {
        gameController.setAI(new RoboAI(this, board.getPlayer(i), fromNew), i);
      }
    }
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Opens a prompt to exit the game, then ask to save the game, then exits the program
   */
  public void exit() {
    if (!isGameRunning()) {
      Platform.exit();
      return;
    }

    if (DialogFacade.newConfirmationAlert("Exit RoboRally?",
        "Are you sure you want to exit RoboRally?")) {
      return;
    }

    stopGame();

    if (roboRally.isHost) {
      roboRally.server.stop();
    } else if (roboRally.isClient) {
      roboRally.client.stopConnection();
    }

    Platform.exit();
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Uploads the game state from the clients. The server responds "OK" if the update is received
   */
  public void uploadProgram() throws Exception {
    if (!roboRally.isClient) {
      return;
    }

    Board board = this.gameController.board;
    final int playerNum = roboRally.clientNum - 1;
    for (int i = 0; i < Player.NO_REGISTERS; i++) {
      Player player = board.getPlayer(playerNum);
      CommandCardField field = player.getProgramField(i);
      String name = field.getCard().getName();
      String response = roboRally.client.post("C " + playerNum + " " + i + " " + name);
      if (!response.equals("OK")) {
        throw new Exception("Failed to update player program");
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
  public void update(Subject subject) {
    // Nothing to update.
  }
}
