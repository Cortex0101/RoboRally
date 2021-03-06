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

import static com.roborally.model.Phase.INITIALISATION;

import com.roborally.controller.CheckPoint;
import com.roborally.controller.FieldAction;
import designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the properties of the board
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {

  public final int width;

  public final int height;

  public final String boardName;

  private Integer gameId;

  private final Space[][] spaces;

  private final List<Space> checkPointSpaces = new ArrayList<>();

  private List<Player> players = new ArrayList<>();

  private Player current;

  private Phase phase = INITIALISATION;

  private int step = 0;

  private boolean stepMode;

  public boolean resetRegisters = true;

  public Board(int width, int height, @NotNull String boardName) {
    this.boardName = boardName;
    this.width = width;
    this.height = height;
    spaces = new Space[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Space space = new Space(this, x, y);
        spaces[x][y] = space;
      }
    }
    this.stepMode = false;
  }

  /**
   * Method sets the order of players in execution of the game.
   *
   * @param playerOrder list of players in their proper order
   */
  public void setPlayerOrder(List<Player> playerOrder) {
    this.players = playerOrder;
  }

  public void addCheckPoint(Space space) {
    this.checkPointSpaces.add(space);
  }

  public int getNumCheckpoints() {
    return this.checkPointSpaces.size();
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Space getCheckPoint(int num) {
    if (num <= getNumCheckpoints()) {
      for (Space space : checkPointSpaces) {
        for (FieldAction fieldAction : space.getActions()) {
          if (fieldAction instanceof CheckPoint checkPoint) {
            if (checkPoint.getCheckpointNum() == num) {
              return space;
            }
          }
        }
      }
    }
    return null;
  }

  public Board(int width, int height) {
    this(width, height, "defaultboard");
  }

  public Integer getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    if (this.gameId == null) {
      this.gameId = gameId;
    } else {
      if (!this.gameId.equals(gameId)) {
        throw new IllegalStateException("A game with a set id may not be assigned a new id!");
      }
    }
  }

  public Space getSpace(int x, int y) {
    if (x >= 0 && x < width &&
        y >= 0 && y < height) {
      return spaces[x][y];
    } else {
      return null;
    }
  }

  public int getPlayersNumber() {
    return players.size();
  }

  public void addPlayer(@NotNull Player player) {
    if (player.board == this && !players.contains(player)) {
      players.add(player);
      notifyChange();
    }
  }

  /**
   * @param player the only player that should be on the board
   * @author Lucas Eiruff
   * <p>
   * This method is used for the AI to generate a copy of the board with itself only
   */
  public void addSinglePlayer(@NotNull Player player) {
    if (player.board == this) {
      players.clear();
      players.add(player);
      notifyChange();
    }
  }

  public Player getPlayer(int i) {
    if (i >= 0 && i < players.size()) {
      return players.get(i);
    } else {
      return null;
    }
  }

  public Player getCurrentPlayer() {
    return current;
  }

  public void setCurrentPlayer(Player player) {
    if (player != this.current && players.contains(player)) {
      this.current = player;
      notifyChange();
    }
  }

  public boolean lastPlayerIsCurrent() {
    return this.players.indexOf(current) == this.players.size() - 1;
  }

  public void setNextPlayerAsCurrent() {
    setCurrentPlayer(getPlayer(getPlayerNumber(current) + 1));
  }

  public Phase getPhase() {
    return phase;
  }

  public void setPhase(Phase phase) {
    if (phase != this.phase) {
      this.phase = phase;
      notifyChange();
    }
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    if (step != this.step) {
      this.step = step;
      notifyChange();
    }
  }

  public boolean isStepMode() {
    return stepMode;
  }

  public void setStepMode(boolean stepMode) {
    if (stepMode != this.stepMode) {
      this.stepMode = stepMode;
      notifyChange();
    }
  }

  public int getPlayerNumber(@NotNull Player player) {
    if (player.board == this) {
      return players.indexOf(player);
    } else {
      return -1;
    }
  }

  /**
   * @param space   the space for which the neighbour should be computed
   * @param heading the heading of the neighbour
   * @return the space in the given direction; null if there is no (reachable) neighbour
   * @author Lucas Eiruff
   * <p>
   * Returns the neighbour of the given space of the board in the given heading. The neighbour is
   * returned only, if it can be reached from the given space (no walls or obstacles in either of
   * the involved spaces); otherwise, null will be returned.
   */
  public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
    int x = space.x;
    int y = space.y;
    switch (heading) {
      case SOUTH -> ++y;
      case WEST -> --x;
      case NORTH -> --y;
      case EAST -> ++x;
    }

    return getSpace(x, y);
  }

  /**
   * @param space1 the first space
   * @param space2 the second space
   * @return the sum of the vertical and horizontal difference between the spaces
   * @author Lucas Eiruff
   * <p>
   * Calculates the distance between two spaces on the board. The distance is calculated not as a
   * diagonal, but the horizontal + vertical distance.
   */
  public int getDistanceBetweenSpaces(@NotNull Space space1, @NotNull Space space2) {
    return Math.abs(space1.x - space2.x) + Math.abs(space1.y - space2.y);
  }

  public String getStatusMessage() {
    return "Phase: " + getPhase().name() +
        ", Player = " + getCurrentPlayer().getName() +
        ", Step: " + getStep();
  }
}
