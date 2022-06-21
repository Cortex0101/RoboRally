package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.List;

public class BlueConveyorBeltCommand extends PlayerMovementCommand {

  private final Heading beltHeading;

  public BlueConveyorBeltCommand(List<Player> players, Player initiator, Heading heading) {
    super(players, initiator);
    beltHeading = heading;
  }

  @Override
  public void execute(GameController gameController) {
    backup();
    Heading originalPlayerHeading = initiator.getHeading();
    initiator.setHeading(beltHeading);
    gameController.move2Forward(initiator);
    initiator.setHeading(originalPlayerHeading);
  }
}
