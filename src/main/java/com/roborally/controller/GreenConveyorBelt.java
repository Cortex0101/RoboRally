package com.roborally.controller;

import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.PlayerMove1Command;
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
   * @author Lucas Eiruff
   *
   * Moves the player 1 space in the conveyorbelts direction if the player lands on the field
   *
   * @param gameController The gameController of the respective game
   * @param space          The space this action should be executed for
   * @return Returns true if the player landed on the field, false otherwise
   */
  @Override
  public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
    Player player = space.getPlayer();
    if (player == null || player.movedByGreenConveyorThisTurn) {
      return false;
    }

    Heading originalPlayerHeading = player.getHeading();
    player.setHeading(heading);
    gameController.playerCommandManager.executeCommand(new PlayerMove1Command(gameController.board.getPlayers(), player));
    //gameController.move1Forward(player);
    player.setHeading(originalPlayerHeading);
    player.movedByGreenConveyorThisTurn = true;
    return true;
  }

}

