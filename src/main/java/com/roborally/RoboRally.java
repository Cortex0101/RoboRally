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
package com.roborally;

import com.roborally.controller.AppController;
import com.roborally.controller.GameController;
import com.roborally.server.Client;
import com.roborally.server.Server;
import com.roborally.view.BoardView;
import com.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main method for roborally. Instantiates the elements required to start a game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RoboRally extends Application {

  private static final int MIN_APP_WIDTH = 600;

  private Stage stage;
  private BorderPane boardRoot;
  private Scene primaryScene;

  private AppController appController;

  public boolean isHost = false;
  public boolean isClient = false;
  public boolean isMultiplayer = false;
  public Server server = null;
  public Client client = null;
  public int clientNum = 0;
  public boolean readyToUpdateBoard = false;

  /**
   * @author Lucas Eiruff
   *
   * Generates a window with the game.
   */
  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    appController = new AppController(this);
    RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);
    boardRoot = new BorderPane();
    VBox vbox = new VBox(menuBar, boardRoot);
    vbox.setMinWidth(MIN_APP_WIDTH);
    primaryScene = new Scene(vbox);
    stage.setScene(primaryScene);
    stage.setTitle("RoboRally");
    stage.setOnCloseRequest(
        e -> {
          e.consume();
          System.out.println("test");
          appController.exit();
        });
    stage.setResizable(false);
    stage.sizeToScene();
    stage.show();
  }

  public AppController getAppController() {
    return appController;
  }

  public Scene getPrimaryScene() {
    return primaryScene;
  }

  public void setScene(Scene scene) {
    stage.setScene(scene);
  }

  public void createBoardView(GameController gameController) {
    // if present, remove old BoardView
    boardRoot.getChildren().clear();

    if (gameController != null) {
      // create and add view for new board
      BoardView boardView = new BoardView(gameController);
      boardRoot.setCenter(boardView);
    }

    stage.sizeToScene();
  }

  public static void main(String[] args) {
    launch(args);
  }

}