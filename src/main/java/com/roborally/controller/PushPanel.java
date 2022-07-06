package com.roborally.controller;

import com.roborally.model.GreenConveyorBeltCommand;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.PushPanelCommand;
import com.roborally.model.Space;

/**
 * Push panels push any robots resting on them into the next space in the direction the push
 * panel faces. They activate only in the register that corresponds to the number on them. For
 * example, if you end register two on a push panel labeled “2, 4” you will be pushed. If you end
 * register three on the same push panel, you won’t be pushed.
 */
public class PushPanel extends FieldAction {

  private Heading heading;
  private Type type;

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public enum Type {
    PUSH_PANEL_1,
    PUSH_PANEL_2
  }

  public Heading getHeading() {
    return heading;
  }

  public void setHeading(Heading heading) {
    this.heading = heading;
  }



  @Override
  public boolean doAction(GameController gameController, Space space) {
    Player player = space.getPlayer();
    if (player == null) {
      return false;
    }

    gameController.playerCommandManager.executeCommand(
        new PushPanelCommand(gameController.board.getPlayers(), player, getHeading(), type));
    return true;
  }
}
