package com.roborally.controller;

import com.roborally.model.Player;
import com.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Pit extends FieldAction {

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
