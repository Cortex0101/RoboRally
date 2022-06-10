package com.roborally.controller;

import com.roborally.model.Player;
import com.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Pit extends FieldAction {

  /**
   * @author Lucas Eiruff
   *
   * Removes all cards from the players register, and moves the player to the field which they started on
   *
   * @param gameController The gameController of the respective game
   * @param space          The space this action should be executed for
   * @return Returns true if the player landed on the field, false otherwise
   */
  @Override
  public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
    Player player = space.getPlayer();
    if (player == null) {
      return false;
    }

    player.setRebooting(true);
    return true;
  }

}
