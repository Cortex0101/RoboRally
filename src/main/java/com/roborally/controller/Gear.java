package com.roborally.controller;

import com.roborally.model.Player;
import com.roborally.model.Space;

public class Gear extends FieldAction {

  public enum Direction {
    LEFT,
    RIGHT
  }

  private Direction direction;

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public Direction getDirection() {
    return direction;
  }

  /**
   * @author Lucas Eiruff
   *
   * Rotates the player based on the direction of the gear, if the player lands on it
   *
   * @param gameController the gameController of the respective game
   * @param space          the space this action should be executed for
   * @return returns true if the player landed on the field, false otherwise
   */
  @Override
  public boolean doAction(GameController gameController, Space space) {
    Player player = space.getPlayer();
    if (player == null) {
      return false;
    }

    switch (direction) {
      case LEFT -> gameController.turnLeft(player);
      case RIGHT -> gameController.turnRight(player);
    }
    return true;
  }
}
