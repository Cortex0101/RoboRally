package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.List;

public class PlayerMove3Command extends PlayerMovementCommand {

  public PlayerMove3Command(List<Player> players, Player initiator) {
    super(players, initiator);
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    gameController.move3Forward(initiator);
  }
}
