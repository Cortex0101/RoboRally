package com.roborally.controller;

import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;

public class CheckPoint extends FieldAction {

  // using the heading here to represent the checkpoints order.
  // NORTH = 1, EAST = 2, SOUTH = 3, WEST = 4, NULL = 5
  // This is just temporary until I figure out why GSON won't load using an integer.
  private Heading heading;

  /**
   * @param gameController The gameController of the respective game
   * @param space          The space this action should be executed for
   * @return Returns true if the player landed on the field, false otherwise
   * @author Lucas Eiruff
   * <p>
   * Checks if the player has landed on the previous checkpoint, then
   */
  @Override
  public boolean doAction(GameController gameController, Space space) {
    Player player = space.getPlayer();
    if (player == null) {
      return false;
    }

    if (player.getLastCheckpoint() + 1 == getCheckpointNum()) {
      player.setLastCheckpoint(getCheckpointNum());
      if (player.getLastCheckpoint() == gameController.board.getNumCheckpoints()) {
        if (gameController.board.getPlayersNumber()
            != 1) { // Only print text if this is the non AI board.
          gameController.setWinner(player);
          return true;
        }
      }
    }

    return true;
  }

  public Heading getHeading() {
    return heading;
  }

  public void setHeading(Heading heading) {
    this.heading = heading;
  }

  public int getCheckpointNum() {
    if (this.heading == null) {
      return 5;
    }
    switch (this.heading) {
      case NORTH -> {
        return 1;
      }
      case EAST -> {
        return 2;
      }
      case SOUTH -> {
        return 3;
      }
      case WEST -> {
        return 4;
      }
      default -> {
        return 5;
      }
    }
  }
}
