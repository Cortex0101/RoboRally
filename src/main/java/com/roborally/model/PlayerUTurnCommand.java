package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.List;

public class PlayerUTurnCommand extends PlayerMovementCommand {
  public PlayerUTurnCommand(List<Player> players, Player initiator) {
    super(players, initiator);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.uTurn(initiator);
  }
}
