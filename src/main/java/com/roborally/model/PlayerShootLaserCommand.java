package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.List;

public class PlayerShootLaserCommand extends PlayerMovementCommand {

  public PlayerShootLaserCommand(List<Player> players, Player initiator) {
    super(players, initiator);
  }

  @Override
  public void execute(GameController gameController) {
    if (initiator.isRebooting() || initiator.getSpace() == null) {
      return;
    }
    backup();
    gameController.shootLaser(initiator);
  }
}
