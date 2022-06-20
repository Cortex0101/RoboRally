package com.roborally.controller;

import com.roborally.model.GreenConveyorBeltCommand;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;
import org.jetbrains.annotations.NotNull;


public class GreenConveyorBelt extends FieldAction {

  private Heading heading;

  public Heading getHeading() {
    return heading;
  }

  public void setHeading(Heading heading) {
    this.heading = heading;
  }

  /**
   * @param gameController The gameController of the respective game
   * @param space          The space this action should be executed for
   * @return Returns true if the player landed on the field, false otherwise
   * @author Lucas Eiruff
   * <p>
   * Moves the player 1 space in the conveyorbelts direction if the player lands on the field
   */
  @Override
  public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
    Player player = space.getPlayer();
    if (player == null || player.movedByGreenConveyorThisTurn) {
      return false;
    }

    gameController.playerCommandManager.executeCommand(
        new GreenConveyorBeltCommand(gameController.board.getPlayers(), player, getHeading()));
    player.movedByGreenConveyorThisTurn = true;
    return true;
  }

}

