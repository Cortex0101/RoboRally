package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.List;

public class PlayerTurnLeftCommand extends PlayerMovementCommand {

  public PlayerTurnLeftCommand(List<Player> players, Player initiator) {
    super(players, initiator);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.turnLeft(initiator);
  }
}
