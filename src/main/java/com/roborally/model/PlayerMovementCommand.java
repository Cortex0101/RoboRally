package com.roborally.model;

import com.roborally.controller.GameController;

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
