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
import com.roborally.model.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameController {

  final public Board board;
  private final RoboAI[] ai;
  public RoboRally roboRally;

  public GameController(@NotNull Board board) {
    this.board = board;
    this.ai = new RoboAI[board.getPlayersNumber()];
  }

  public void setWinner(Player player) {
    System.out.println("Player '" + player.getName() + "' wins!" );
    ExitGameAfterVictory(roboRally.getAppController(), player.getName());
  }

  public void setAI(RoboAI roboAI, int i) {
    this.ai[i] = roboAI;
  }

  /**
   * @author Lucas Eiruff
   *
   * Move current player to space, without accounting for movement rules.
   *
   * @param space the space to which the current player should move
   */
  public void moveCurrentPlayerToSpace(@NotNull Space space) {
    if (space.getPlayer() != null) {
      return;
    }
    final Player currentPlayer = board.getCurrentPlayer();
    currentPlayer.getSpace().setPlayer(null);
    space.setPlayer(currentPlayer);
    board.setStep(board.getStep() + 1);
  }

  /**
   * @author Lucas Eiruff
   *
   * initiates the programming phase, and refills/replaces the command card registers if needed
   *
   * @param resetRegisters used to decide if the registers need to be reset
   */
  public void startProgrammingPhase(boolean resetRegisters) {
    board.setPhase(Phase.PROGRAMMING);
    board.setCurrentPlayer(board.getPlayer(0));
    board.setStep(0);
    if (resetRegisters) {
      clearPlayerPrograms();
      generatePlayerCards();
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * Clears every players program
   */
  public void clearPlayerPrograms() {
    for (Player player : board.getPlayers()) {
      for (CommandCardField field : player.getProgram()) {
        field.setCard(null);
        field.setVisible(true); // TODO: We set this to true a bunch of places, but only make it invisible in makeProgramFieldsInvisible(). This bloats
      }
    }
  }

  private void setPlayerProgram(Player player, List<CommandCard> cards) {
    for (int i = 0; i < Player.NO_REGISTERS; i++) {
      CommandCardField field = player.getProgramField(i);
      field.setCard(cards.get(i));
      field.setVisible(true);
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * Fills the 8 commandcards for each player with random command cards
   */
  public void generatePlayerCards() {
    for (Player player : board.getPlayers()) {
      for (CommandCardField field : player.getCards()) {
        field.setCard(generateRandomCommandCard());
        field.setVisible(true);
      }
    }
  }

  /**
   * Generates and returns a random command card
   *
   * @return a random command card
   */
  private CommandCard generateRandomCommandCard() {
    Command[] commands = Command.values();
    int random = (int) (Math.random() * commands.length);
    return new CommandCard(commands[random]);
  }

  /**
   * @author Lucas Eiruff
   *
   * Calculates and sets the program for each AI player
   */
  private void setAIPrograms() {
    int i = 0;
    for (Player player : board.getPlayers()) {
      if (player.isAI()) {
        setPlayerProgram(player, ai[i].findBestProgramToGetTo(board.getCheckPoint(player.getLastCheckpoint() + 1)));
      }
      ++i;
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * ends the programming phase, and disables the users ability to move cards
   */
  public void finishProgrammingPhase() {
    makeProgramFieldsInvisible();
    makeProgramFieldsVisible(0);
    if (board.getPlayersNumber() > 1) // AI boards consists of only 1 player, and their program has already been set.
      setAIPrograms();
    board.setPhase(Phase.ACTIVATION);
    board.setCurrentPlayer(board.getPlayer(0));
    board.setStep(0);
  }

  /**
   * @author Lucas Eiruff
   *
   * sets a card as visible, for when the card is in use
   */
  private void makeProgramFieldsVisible(int register) {
    if (register < 0 || register >= Player.NO_REGISTERS) {
      return;
    }

    for (Player player : board.getPlayers()) {
      player.getProgramField(register).setVisible(true);
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * sets a card as invisible, to avoid other players to see the card
   */
  private void makeProgramFieldsInvisible() {
    for (Player player : board.getPlayers()) {
      for (CommandCardField field : player.getProgram()) {
        field.setVisible(false);
      }
    }
  }

  /**
   * @author Lucas Eiruff
   *
   * Executes all registers for all players
   */
  public void executePrograms() {
    board.setStepMode(false);
    continuePrograms();
  }

  /**
   * @author Lucas Eiruff
   *
   * Executes a registers for a players
   */
  public void executeStep() {
    board.setStepMode(true);
    continuePrograms();
  }

  private void continuePrograms() {
    do {
      executeNextStep();
    } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
  }

  private void executeRegister(Player player, int register) {
    if (!player.isRebooting()) {
      CommandCard card = board.getCurrentPlayer().getProgramField(register).getCard();
      if (card != null) {
        Command command = card.command;
        if (command.isInteractive()) {
          board.setPhase(Phase.PLAYER_INTERACTION);
          return;
        }
        executeCommand(board.getCurrentPlayer(), command);
      }
    }
  }

  private void executeNextStep() {
    int step = board.getStep();
    Player currentPlayer = board.getCurrentPlayer();

    executeRegister(currentPlayer, board.getStep());

    if (!board.lastPlayerIsCurrent()) {
      board.setNextPlayerAsCurrent();
      return;
    }

    if (++step < Player.NO_REGISTERS) {
      makeProgramFieldsVisible(step);
      board.setStep(step);
      board.setCurrentPlayer(board.getPlayer(0));
      activateBoardElements();
    } else {
      startProgrammingPhase(true);
      activateBoardElements();
      for (Player player : board.getPlayers()) {
        if (player.isRebooting()) {
          player.reboot(this);
        }
      }
    }
  }

  private void activateBoardElements() {
    for (int i = 0; i < board.width; i++) {
      for (int j = 0; j < board.height; j++) {
        Space space = board.getSpace(i, j);
        for (FieldAction action : space.getActions()) {
          action.doAction(this, space);
        }
      }
    }
  }

  public Stack<PlayerMovementCommand> commandHistory = new Stack<>();

  public PlayerCommandManager playerCommandManager = new PlayerCommandManager(this);

  private void executeCommand(@NotNull Player player, Command command) {
    if (player.board == board && command != null) {
      switch (command) {
        case MOVE1 -> {
          playerCommandManager.executeCommand(new PlayerMove1Command(board.getPlayers(), player));
        }
        case MOVE2 -> {
          playerCommandManager.executeCommand(new PlayerMove2Command(board.getPlayers(), player));
        }
        case MOVE3 -> {
          playerCommandManager.executeCommand(new PlayerMove3Command(board.getPlayers(), player));
        }
        case RIGHT -> {
          playerCommandManager.executeCommand(new PlayerTurnRightCommand(board.getPlayers(), player));
        }
        case LEFT -> {
          playerCommandManager.executeCommand(new PlayerTurnLeftCommand(board.getPlayers(), player));
        }
        case U_TURN -> {
          playerCommandManager.executeCommand(new PlayerUTurnCommand(board.getPlayers(), player));
        }
        default -> {}
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

  private void throwIfPlayerCollidesWithWall(@NotNull Player player, @NotNull Space space, @NotNull Heading heading)
      throws ImpossibleMoveException {
    if (player.getSpace().getWalls().contains(player.getHeading()) ||
        space.getWalls().contains(player.getHeading().next().next())) {
      throw new ImpossibleMoveException(player, space, heading,
          "Player: " + player.getName() + " going in direction: " +
              player.getHeading() + " at: " + "(" + player.getSpace().x + ", " + player.getSpace().y
              + ")" +
              " hit wall at " + "(" + space.x + ", " + space.y + ")");
    }
  }

  public void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading)
      throws ImpossibleMoveException {
    assert board.getNeighbour(player.getSpace(), heading) == space;
    throwIfPlayerCollidesWithWall(player, space, heading);

    Player other = space.getPlayer();
    if (other != null) {
      Space target = board.getNeighbour(space, heading);
      if (target != null) {
        moveToSpace(other, target, heading);
        assert target.getPlayer() == null : target; // make sure target is free now
      } else {
        throw new ImpossibleMoveException(player, space, heading);
      }
    }
    player.setSpace(space);
  }

  /**
   * @author Lucas Eiruff
   *
   * Moves player one field in the direction the player is facing.
   *
   * @param player the player using the card
   */
  public void move1Forward(@NotNull Player player) {
    if (player.board != board)
      return;

    Space target = board.getNeighbour(player.getSpace(), player.getHeading());
    if (target == null)
      return;

    try {
      moveToSpace(player, target, player.getHeading());
    } catch (ImpossibleMoveException ignore) {}
  }

  /**
   * @author Lucas Eiruff
   *
   * Moves player two field in the direction the player is facing.
   *
   * @param player the player using the card
   */
  public void move2Forward(@NotNull Player player) {
    move1Forward(player);
    move1Forward(player);
  }

  /**
   * @author Lucas Eiruff
   *
   * Moves player three field in the direction the player is facing.
   *
   * @param player the player using the card
   */
  public void move3Forward(@NotNull Player player) {
    move1Forward(player);
    move1Forward(player);
    move1Forward(player);
  }

  /**
   * @author Lucas Eiruff
   *
   * Turns the player to the right
   *
   * @param player the player using the card
   */
  public void turnRight(@NotNull Player player) {
    player.setHeading(player.getHeading().next());
  }

  /**
   * @author Lucas Eiruff
   *
   * Turns the player to the left
   *
   * @param player the player using the card
   */
  public void turnLeft(@NotNull Player player) {
    player.setHeading(player.getHeading().prev());
  }

  /**
   * @author Lucas Eiruff
   *
   * Turns the player to the around
   *
   * @param player the player using the card
   */
  public void uTurn(@NotNull Player player) {
    turnLeft(player);
    turnLeft(player);
  }

  /**
   * @author Lucas Eiruff
   *
   * @param source The register where the card originated from
   * @param target The register which the cards is being moved to
   * @return True if the card can be moved to the target, return false otherwise
   */
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

  /**
   * @author Lucas Eiruff
   *
   * Executes a command with multiple choice in how to execute the command card.
   *
   * @param command The choice made by the user in which command to use.
   */
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
  public void ExitGameAfterVictory(AppController appController, String player) {
    appController.endGame(player);
  }
}
