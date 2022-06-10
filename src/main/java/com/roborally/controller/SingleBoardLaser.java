package com.roborally.controller;

import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;

public class SingleBoardLaser extends FieldAction {
  private Heading heading;

  public Heading getHeading() {
    return heading;
  }

  public void setHeading(Heading heading) {
    this.heading = heading;
  }

  /**
   * @author August Hjortholm
   *
   * removes all cards from the players register, and moves the player to the field which they started on
   *
   * @param gameController the gameController of the respective game
   * @param space          the space this action should be executed for
   * @return returns true if the player landed on the field, false otherwise
   */
  @Override
  public boolean doAction(GameController gameController, Space space) {
    Player player = space.getPlayer();
    if (player == null) {
      return false;
    }

    player.setRebooting(true);
    return true;
  }
}
