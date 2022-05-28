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

import com.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

  final public Board board;
  private final RoboAI[] ai;

  public GameController(@NotNull Board board) {
    this.board = board;
    this.ai = new RoboAI[board.getPlayersNumber()];
  }

  public void setWinner(Player player) {
    System.out.println("Player '" + player.getName() + "' wins!" );
    notImplemented(); // TODO: do something when player wins!
  }

  public void setAI(RoboAI roboAI, int i) {
    this.ai[i] = roboAI;
  }

  /**
   * This method should be used for debugging only. To move players accounting for pushing and other
   * game features use moveToSpace()
   *
   * @param space the space to which the current player should move
   */
  public void moveCurrentPlayerToSpace(@NotNull Space space) {
      if (space.getPlayer() != null) {
          return;
      }

    final Player currentPlayer = board.getCurrentPlayer();

    // Move player
    currentPlayer.getSpace().setPlayer(null);
    space.setPlayer(currentPlayer);

    // Set next player as active
    final int playerNumber = board.getPlayerNumber(currentPlayer);
    if (playerNumber < (board.getPlayersNumber() - 1)) {
      board.setCurrentPlayer(board.getPlayer(playerNumber + 1));
    } else {
      board.setCurrentPlayer(board.getPlayer(0));
    }

    // Increment counter
    board.setStep(board.getStep() + 1);
  }

  private void setPlayerProgram(Player player, List<CommandCard> cards) {
    for (int i = 0; i < Player.NO_REGISTERS; i++) {
      CommandCardField field = player.getProgramField(i);
      field.setCard(cards.get(i));
      field.setVisible(true);
    }
  }

  // XXX: V2
  public void startProgrammingPhase(boolean resetRegisters) {
    board.setPhase(Phase.PROGRAMMING);
    board.setCurrentPlayer(board.getPlayer(0));
    board.setStep(0);

      if (resetRegisters) {
          resetRegisters();
      }
  }

  private void resetRegisters() {
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      Player player = board.getPlayer(i);
      if (player != null) {
        for (int j = 0; j < Player.NO_REGISTERS; j++) {
          CommandCardField field = player.getProgramField(j);
          field.setCard(null);
          field.setVisible(true);
        }
        for (int j = 0; j < Player.NO_CARDS; j++) {
          CommandCardField field = player.getCardField(j);
          field.setCard(generateRandomCommandCard());
          field.setVisible(true);
        }
      }
    }
  }

  // XXX: V2
  private CommandCard generateRandomCommandCard() {
    Command[] commands = Command.values();
    int random = (int) (Math.random() * commands.length);
    return new CommandCard(commands[random]);
  }

  private void setAIPrograms() {
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      if (board.getPlayer(i).getIsAI()) {
        setPlayerProgram(board.getPlayer(i), ai[i].findBestProgramToGetTo(
            board.getCheckPoint(board.getPlayer(i).getLastCheckpoint() + 1)));
      }
    }
  }

  private boolean hasAnyAI() {
    for (RoboAI i : ai) {
        if (i != null) {
            return true;
        }
    }
    return false;
  }

  // XXX: V2
  public void finishProgrammingPhase() {
    makeProgramFieldsInvisible();
    makeProgramFieldsVisible(0);
      if (hasAnyAI()) {
          setAIPrograms();
      }
    board.setPhase(Phase.ACTIVATION);
    board.setCurrentPlayer(board.getPlayer(0));
    board.setStep(0);
  }

  // XXX: V2
  private void makeProgramFieldsVisible(int register) {
    if (register >= 0 && register < Player.NO_REGISTERS) {
      for (int i = 0; i < board.getPlayersNumber(); i++) {
        Player player = board.getPlayer(i);
        CommandCardField field = player.getProgramField(register);
        field.setVisible(true);
      }
    }
  }

  // XXX: V2
  private void makeProgramFieldsInvisible() {
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      Player player = board.getPlayer(i);
      for (int j = 0; j < Player.NO_REGISTERS; j++) {
        CommandCardField field = player.getProgramField(j);
        field.setVisible(false);
      }
    }
  }

  // XXX: V2
  public void executePrograms() {
    board.setStepMode(false);
    continuePrograms();
  }

  // XXX: V2
  public void executeStep() {
    board.setStepMode(true);
    continuePrograms();
  }

  // XXX: V2
  private void continuePrograms() {
    do {
      executeNextStep();
    } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
  }

  // XXX: V2
  private void executeNextStep() {
    Player currentPlayer = board.getCurrentPlayer();
    if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
      int step = board.getStep();
      if (step >= 0 && step < Player.NO_REGISTERS) {
        if (!currentPlayer.isRebooting()) {
          CommandCard card = currentPlayer.getProgramField(step).getCard();
          if (card != null) {
            Command command = card.command;
            if (command.isInteractive()) {
              board.setPhase(Phase.PLAYER_INTERACTION);
              return;
            }
            executeCommand(currentPlayer, command);
          }
        }
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getPlayersNumber()) {
          //
          board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
          step++;
          if (step < Player.NO_REGISTERS) {
            makeProgramFieldsVisible(step);
            board.setStep(step);
            //
            board.setCurrentPlayer(board.getPlayer(0));
            activateBoardElements();
          } else {
            startProgrammingPhase(true);
            activateBoardElements();
            for (int i = 0; i < board.getPlayersNumber(); i++) {
              if (board.getPlayer(i).isRebooting()) {
                board.getPlayer(i).reboot(this);
              }
            }
          }
        }
      } else {
        // this should not happen
        assert false;
      }
    } else {
      // this should not happen
      assert false;
    }
  }

  private void activateBoardElements() {
    // Iterate over all board spaces
    for (int i = 0; i < board.width; i++) {
      for (int j = 0; j < board.height; j++) {
        Space space = board.getSpace(i, j);
        for (FieldAction action : space.getActions()) {
          action.doAction(this, space);
        }
      }
    }
  }

  // XXX: V2
  private void executeCommand(@NotNull Player player, Command command) {
    if (player.board == board && command != null) {
      switch (command) {
        case MOVE1 -> this.move1Forward(player);
        case MOVE2 -> this.move2Forward(player);
        case MOVE3 -> this.move3Forward(player);
        case RIGHT -> this.turnRight(player);
        case LEFT -> this.turnLeft(player);
        case U_TURN -> this.uTurn(player);
        default -> {
        }
        // DO NOTHING (for now)
      }
    }
  }

  static class ImpossibleMoveException extends Exception {

    private final Player player;
    private final Space space;
    private final Heading heading;

    public ImpossibleMoveException(Player player, Space space, Heading heading, String message) {
      super(message);
      this.player = player;
      this.space = space;
      this.heading = heading;
    }

    public ImpossibleMoveException(Player player, Space space, Heading heading) {
      super("Impossible move!");
      this.player = player;
      this.space = space;
      this.heading = heading;
    }
  }


  void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading)
      throws ImpossibleMoveException {
    assert board.getNeighbour(player.getSpace(), heading)
        == space; // make sure the move to here is possible in principle
    if (player.getSpace().getWalls().contains(player.getHeading()) ||
        space.getWalls().contains(player.getHeading().next().next())) {
      throw new ImpossibleMoveException(player, space, heading,
          "Player: " + player.getName() + " going in direction: " +
              player.getHeading() + " at: " + "(" + player.getSpace().x + ", " + player.getSpace().y
              + ")" +
              " hit wall at " + "(" + space.x + ", " + space.y + ")");
    }
    Player other = space.getPlayer();
    if (other != null) {
      Space target = board.getNeighbour(space, heading);
      if (target != null) {
        // XXX Note that there might be additional problems with
        //     infinite recursion here (in some special cases)!
        //     We will come back to that!
        moveToSpace(other, target, heading);

        // Note that we do NOT embed the above statement in a try catch block, since
        // the thrown exception is supposed to be passed on to the caller

        assert target.getPlayer() == null : target; // make sure target is free now
      } else {
        throw new ImpossibleMoveException(player, space, heading);
      }
    }
    player.setSpace(space);
  }


  public void move1Forward(@NotNull Player player) {
    if (player.board == board) {
      Space space = player.getSpace();
      Heading heading = player.getHeading();

      Space target = board.getNeighbour(space, heading);
      if (target != null) {
        try {
          moveToSpace(player, target, heading);
        } catch (ImpossibleMoveException e) {
            if (!player.getIsAI()) // disable printing for AI players to avoid bloat
            {
                e.printStackTrace();
            }
        }
      }
    }

  }

  public void move2Forward(@NotNull Player player) {
    move1Forward(player);
    move1Forward(player);
  }

  public void move3Forward(@NotNull Player player) {
    move1Forward(player);
    move1Forward(player);
    move1Forward(player);
  }

  public void turnRight(@NotNull Player player) {
    player.setHeading(player.getHeading().next());
  }

  public void turnLeft(@NotNull Player player) {
    player.setHeading(player.getHeading().prev());
  }

  public void uTurn(@NotNull Player player) {
    turnLeft(player);
    turnLeft(player);
  }

  public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
    CommandCard sourceCard = source.getCard();
    CommandCard targetCard = target.getCard();
    if (sourceCard != null && targetCard == null) {
      target.setCard(sourceCard);
      source.setCard(null);
      return true;
    } else {
      return false;
    }
  }

  public void executeCommandOptionAndContinue(Command command) {
    board.setPhase(Phase.ACTIVATION);
    executeCommand(board.getCurrentPlayer(), command);

    int nextPlayerNumber = board.getPlayerNumber(board.getCurrentPlayer()) + 1;
    if (nextPlayerNumber < board.getPlayersNumber()) {
      board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
    } else {
      int step = board.getStep();
      step++;
      if (step < Player.NO_REGISTERS) {
        makeProgramFieldsVisible(step);
        board.setStep(step);
        board.setCurrentPlayer(board.getPlayer(0));
      } else {
        startProgrammingPhase(true);
      }
    }

    if (board.isStepMode()) {
      executeStep();
    } else {
      executePrograms();
    }
  }

  /**
   * A method called when no corresponding controller operation is implemented yet. This should
   * eventually be removed.
   */
  public void notImplemented() {
    // XXX just for now to indicate that the actual method is not yet implemented
    assert false;
  }

}
