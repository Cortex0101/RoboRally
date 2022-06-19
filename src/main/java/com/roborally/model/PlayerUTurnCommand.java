package com.roborally.model;

import com.roborally.controller.GameController;

public class PlayerUTurnCommand extends PlayerMovementCommand {
  public PlayerUTurnCommand(Player player) {
    super(player);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.uTurn(player);
  }
}
