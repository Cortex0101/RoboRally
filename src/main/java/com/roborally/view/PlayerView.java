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

import com.roborally.controller.GameController;
import com.roborally.model.Command;
import com.roborally.model.CommandCardField;
import com.roborally.model.Phase;
import com.roborally.model.Player;
import com.roborally.model.PlayerCommandManager;
import designpatterns.observer.Subject;
import java.util.Timer;
import java.util.TimerTask;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * Generate a view for the programmin panel for each player
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayerView extends Tab implements ViewObserver {

  public final Player player;

  private final GridPane programPane;

  private final CardFieldView[] programCardViews;

  private final VBox buttonPanel;

  private final Button finishButton;
  private final Button executeButton;
  private final Button stepButton;

  private final VBox playerInteractionPanel;

  private final GameController gameController;

  public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
    super(player.getName());
    this.setStyle("-fx-text-base-color: #" + player.getColor().substring(2) + ";");

    VBox top = new VBox();
    this.setContent(top);

    this.gameController = gameController;
    this.player = player;

    Label programLabel = new Label("Program");

    programPane = new GridPane();
    programPane.setVgap(2.0);
    programPane.setHgap(2.0);
    programCardViews = new CardFieldView[Player.NO_REGISTERS];
    for (int i = 0; i < Player.NO_REGISTERS; i++) {
      CommandCardField cardField = player.getProgramField(i);
      if (cardField != null) {
        programCardViews[i] = new CardFieldView(gameController, cardField);
        programPane.add(programCardViews[i], i, 0);
      }
    }

    // XXX  the following buttons should actually not be on the tabs of the individual
    //      players, but on the PlayersView (view for all players). This should be
    //      refactored.

    finishButton = new Button("Finish Programming");
    finishButton.setOnAction(e -> {
      if (!gameController.roboRally.isMultiplayer) {
        gameController.finishProgrammingPhase();
      } else {
        try {
          gameController.roboRally.getAppController().uploadProgram();
          String ignore = gameController.roboRally.client.post("PLAYER_READY");
        } catch (Exception ex) {
          ex.printStackTrace();
        }

        TimerTask task = new TimerTask() {
          public void run() {
            System.out.println("Task performed on ");
            requestUpdatedMap();
          }

          private void requestUpdatedMap() {
            String ready = gameController.roboRally.client.post("IS_EVERYONE_READY");
            if (ready.equals("YES")) {
              gameController.roboRally.readyToUpdateBoard = true;
              cancel();
            }
          }
        };
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, 1000L, 1000L);
      }
    });

    executeButton = new Button("Execute Program");
    executeButton.setOnAction(e -> gameController.executePrograms());

    stepButton = new Button("Execute Current Register");
    stepButton.setOnAction(e -> gameController.executeStep());

    buttonPanel = new VBox(finishButton, executeButton, stepButton);
    buttonPanel.setAlignment(Pos.CENTER_LEFT);
    buttonPanel.setSpacing(3.0);
    // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

    playerInteractionPanel = new VBox();
    playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
    playerInteractionPanel.setSpacing(3.0);

    Label cardsLabel = new Label("Command Cards");
    GridPane cardsPane = new GridPane();
    cardsPane.setVgap(2.0);
    cardsPane.setHgap(2.0);
    CardFieldView[] cardViews = new CardFieldView[Player.NO_CARDS];
    for (int i = 0; i < Player.NO_CARDS; i++) {
      CommandCardField cardField = player.getCardField(i);
      if (cardField != null) {
        cardViews[i] = new CardFieldView(gameController, cardField);
        cardsPane.add(cardViews[i], i, 0);
      }
    }

    VBox undoRedoPanel = new VBox();
    Button undoButton = new Button("Undo");
    Button redoButton = new Button("Redo");
    undoRedoPanel.getChildren().addAll(undoButton, redoButton);
    programPane.add(undoRedoPanel, Player.NO_REGISTERS + 1, 0);

    undoButton.setDisable(false);
    redoButton.setDisable(true);

    undoButton.setOnAction(e -> {
      PlayerCommandManager commandManager = gameController.playerCommandManager;
      commandManager.undoLast();
      undoButton.setDisable(!commandManager.hasUndoesLeft());
      redoButton.setDisable(!commandManager.hasRedoesLeft());
    });

    redoButton.setOnAction(e -> {
      PlayerCommandManager commandManager = gameController.playerCommandManager;
      commandManager.redoLast();
      undoButton.setDisable(!commandManager.hasUndoesLeft());
      redoButton.setDisable(!commandManager.hasRedoesLeft());
    });

    top.getChildren().add(programLabel);
    top.getChildren().add(programPane);
    top.getChildren().add(cardsLabel);
    top.getChildren().add(cardsPane);

    if (player.board != null) {
      player.board.attach(this);
      update(player.board);
    }
  }

  @Override
  public void updateView(Subject subject) {
    if (subject == player.board) {
      for (int i = 0; i < Player.NO_REGISTERS; i++) {
        CardFieldView cardFieldView = programCardViews[i];
        if (cardFieldView != null) {
          if (player.board.getPhase() == Phase.PROGRAMMING) {
            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
          } else {
            if (i < player.board.getStep()) {
              cardFieldView.setBackground(CardFieldView.BG_DONE);
            } else if (i == player.board.getStep()) {
              if (player.board.getCurrentPlayer() == player) {
                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
              } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer())
                  > player.board.getPlayerNumber(player)) {
                cardFieldView.setBackground(CardFieldView.BG_DONE);
              } else {
                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
              }
            } else {
              cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
            }
          }
        }
      }

      if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
        if (!programPane.getChildren().contains(buttonPanel)) {
          programPane.getChildren().remove(playerInteractionPanel);
          programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
        }
        switch (player.board.getPhase()) {
          case INITIALISATION -> {
            finishButton.setDisable(true);
            executeButton.setDisable(false);
            stepButton.setDisable(true);
          }
          case PROGRAMMING -> {
            finishButton.setDisable(false);
            executeButton.setDisable(true);
            stepButton.setDisable(true);
          }
          case ACTIVATION -> {
            finishButton.setDisable(true);
            executeButton.setDisable(false);
            stepButton.setDisable(false);
          }
          default -> {
            finishButton.setDisable(true);
            executeButton.setDisable(true);
            stepButton.setDisable(true);
          }
        }
      } else {
        if (!programPane.getChildren().contains(playerInteractionPanel)) {
          programPane.getChildren().remove(buttonPanel);
          programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
        }
        playerInteractionPanel.getChildren().clear();
        if (player.board.getCurrentPlayer() == player) {
          Command command = player.getProgramField(player.board.getStep()).getCard().command;
          if (command.isInteractive()) {
            for (Command option : command.getOptions()) {
              Button button = new Button(option.displayName);
              button.setOnAction(e -> gameController.executeCommandOptionAndContinue(option));
              button.setDisable(false);
              playerInteractionPanel.getChildren().add(button);
            }
          }
        }
      }
    }
  }

}
