package com.roborally.controller;

import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;

public class CheckPoint extends FieldAction {

  // using the heading here to represent the checkpoints order.
  // NORTH = 1, EAST = 2, SOUTH = 3, WEST = 4, NULL = 5
  // This is just temporary until I figure out why GSON won't load using an integer.
  private Heading heading;

  @Override
  public boolean doAction(GameController gameController, Space space) {
    Player player = space.getPlayer();
    if (player == null) {
      return false;
    }

    // TODO: Load the nubmer of checkpoint from the json file.
    // Save it to the game controller and check here if the palyers current checkpoint is the last
    // If so, we should call something like gameController.setWinner(player);
      if (player.getLastCheckpoint() + 1 == getCheckpointNum()) {
          player.setLastCheckpoint(getCheckpointNum());
      }

    return false;
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
