package com.roborally.model;

import com.roborally.controller.GameController;

/**
 * @author Lucas Eiruff
 *
 * Base class for all player movement commands.
 *
 * This class has a single abstract method 'execute' which will have to be implemented by
 * its subclasses. The subclasses implementation of execute is expected to class backup at some
 * point, to store the previous state of the player, such that the actions performed can be
 * undone via the undo method later.
 */
public abstract class PlayerMovementCommand {
  public Player player;

  private Heading prevPlayerHeading;
  private Space prevPlayerSpace;

  PlayerMovementCommand(Player player) {
    this.player = player;
  }

  void backup() {
    this.prevPlayerHeading = player.getHeading();
    this.prevPlayerSpace = player.getSpace();
  }

  public void undo() {
    player.setHeading(prevPlayerHeading);
    player.setSpace(prevPlayerSpace);
  }

  public abstract void execute(GameController gameController);
}
