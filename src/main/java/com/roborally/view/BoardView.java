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
package com.roborally.view;

import designpatterns.observer.Subject;
import com.roborally.controller.GameController;
import com.roborally.model.Board;
import com.roborally.model.Phase;
import com.roborally.model.Space;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class BoardView extends VBox implements ViewObserver {

  private final Board board;

  private final Label statusLabel;

  public BoardView(@NotNull GameController gameController) {
    board = gameController.board;

    GridPane mainBoardPane = new GridPane();
    PlayersView playersView = new PlayersView(gameController);
    statusLabel = new Label("<no status>");

    this.getChildren().add(mainBoardPane);
    this.getChildren().add(playersView);
    this.getChildren().add(statusLabel);

    SpaceView[][] spaces = new SpaceView[board.width][board.height];

    for (int x = 0; x < board.width; x++) {
      for (int y = 0; y < board.height; y++) {
        Space space = board.getSpace(x, y);
        SpaceView spaceView = new SpaceView(space);
        spaces[x][y] = spaceView;
        mainBoardPane.add(spaceView, x, y);
      }
    }

    board.attach(this);
    update(board);
  }

  @Override
  public void updateView(Subject subject) {
    if (subject == board) {
      Phase phase = board.getPhase();
      statusLabel.setText(board.getStatusMessage());
    }
  }
}
