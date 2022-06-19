package com.roborally.model;

import com.roborally.controller.GameController;

public class PlayerTurnRightCommand extends PlayerMovementCommand {
  public PlayerTurnRightCommand(Player player) {
    super(player);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.turnRight(player);
  }
}
