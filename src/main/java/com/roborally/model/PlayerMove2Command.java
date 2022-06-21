package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.List;

public class PlayerMove2Command extends PlayerMovementCommand {

  public PlayerMove2Command(List<Player> players, Player initiator) {
    super(players, initiator);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.move2Forward(initiator);
  }
}
