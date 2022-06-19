package com.roborally.model;

import com.roborally.controller.GameController;

public class PlayerTurnLeftCommand extends PlayerMovementCommand {
  public PlayerTurnLeftCommand(Player player) {
    super(player);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.turnLeft(player);
  }
}
