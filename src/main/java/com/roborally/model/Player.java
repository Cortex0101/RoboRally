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
package com.roborally.model;

import static com.roborally.model.Heading.SOUTH;

import com.roborally.controller.GameController;
import designpatterns.observer.Subject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the properties of a player
 */
public class Player extends Subject {

  private final Space startingSpace;
  private boolean rebooting = false;

  private boolean isAI;

  private int lastCheckpoint;

  final public static int NO_REGISTERS = 5;
  final public static int NO_CARDS = 8;

  final public Board board;

  private String name;
  private String color;

  private Space space;
  private Heading heading = SOUTH;

  private final CommandCardField[] program;
  private final CommandCardField[] cards;

  public boolean movedByBlueConveyorThisTurn = false;
  public boolean movedByGreenConveyorThisTurn = false;

  private CommandCardDeck commandCardDeck = new CommandCardDeck();

  public Player(@NotNull Board board, String color, @NotNull String name, Space startingSpace) {
    this.startingSpace = startingSpace;
    this.board = board;
    this.name = name;
    this.color = color;
    this.space = null;
    this.lastCheckpoint = 0;

    program = new CommandCardField[NO_REGISTERS];
    for (int i = 0; i < program.length; i++) {
      program[i] = new CommandCardField(this);
    }

    cards = new CommandCardField[NO_CARDS];
    for (int i = 0; i < cards.length; i++) {
      cards[i] = new CommandCardField(this);
    }
  }

  public CommandCardField[] getCards() {
    return cards;
  }

  public CommandCardField[] getProgram() {
    return program;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name != null && !name.equals(this.name)) {
      this.name = name;
      notifyChange();
      if (space != null) {
        space.playerChanged();
      }
    }
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
    notifyChange();
    if (space != null) {
      space.playerChanged();
    }
  }

  public Space getSpace() {
    return space;
  }

  public void setSpace(Space space) {
    Space oldSpace = this.space;
    if (space != oldSpace &&
        (space == null || space.board == this.board)) {
      this.space = space;
      if (oldSpace != null) {
        oldSpace.setPlayer(null);
      }
      if (space != null) {
        space.setPlayer(this);
      }
      notifyChange();
    }
  }

  public Heading getHeading() {
    return heading;
  }

  public void setHeading(@NotNull Heading heading) {
    if (heading != this.heading) {
      this.heading = heading;
      notifyChange();
      if (space != null) {
        space.playerChanged();
      }
    }
  }

  public CommandCardField getProgramField(int i) {
    return program[i];
  }

  public CommandCardField getCardField(int i) {
    return cards[i];
  }

  @Override
  public String toString() {
    return "Player{" +
        "board=" + board +
        ", name='" + name + '\'' +
        ", color='" + color + '\'' +
        ", space=" + space +
        ", heading=" + heading +
        ", program=" + Arrays.toString(program) +
        ", cards=" + Arrays.toString(cards) +
        '}';
  }

  public boolean isRebooting() {
    return rebooting;
  }

  public void setRebooting(boolean rebooting) {
    this.rebooting = rebooting;
    this.setSpace(null);
  }

  public void reboot(GameController gameController) {
    setRebooting(false);
    setSpace(getStartingSpace());
  }

  public Space getStartingSpace() {
    return startingSpace;
  }

  public int getLastCheckpoint() {
    return lastCheckpoint;
  }

  public void setLastCheckpoint(int lastCheckpoint) {
    this.lastCheckpoint = lastCheckpoint;
  }

  public boolean isAI() {
    return isAI;
  }

  public void setIsAI(boolean AI) {
    isAI = AI;
  }

  public Position getPosition() {
    return new Position(space.x, space.y);
  }

  public CommandCardDeck getCommandCardDeck() {
    return commandCardDeck;
  }

  public void drawCards() {
    int i = 0;
    commandCardDeck.drawCards(NO_CARDS);
    for (CommandCard commandCard : commandCardDeck.getDrawnCards()) {
      getCards()[i].setCard(commandCard);
      getCards()[i].setVisible(true);
      ++i;
      if (i == 8) break;
    }
  }
}
