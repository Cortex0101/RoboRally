package com.roborally.controller;

import com.roborally.model.Space;

public abstract class FieldAction {

  /**
   * Executes the field action for a given space. In order to be able to do that the GameController
   * associated with the game is passed to this method.
   *
   * @param gameController The gameController of the respective game
   * @param space          The space this action should be executed for
   * @return Whether the action was successfully executed
   */
  public abstract boolean doAction(GameController gameController, Space space);
}
