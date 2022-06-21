package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.Deque;
import java.util.LinkedList;

public class PlayerCommandManager {

  private final Deque<PlayerMovementCommand> undoStack = new LinkedList<>();
  private final Deque<PlayerMovementCommand> redoStack = new LinkedList<>();
  private final GameController gameController;

  public PlayerCommandManager(GameController gameController) {
    this.gameController = gameController;
  }

  public void executeCommand(PlayerMovementCommand command) {
    command.execute(gameController);
    undoStack.offerLast(command);
  }

  public void undoLast() {
    if (undoStack.isEmpty()) {
      return;
    }

    PlayerMovementCommand prevCommand = undoStack.pollLast();
    redoStack.offerLast(prevCommand);
    prevCommand.undo();
  }

  public void redoLast() {
    if (redoStack.isEmpty()) {
      return;
    }

    PlayerMovementCommand prevCommand = redoStack.pollLast();
    undoStack.offerLast(prevCommand);
    prevCommand.execute(gameController);
  }

  public boolean hasRedoesLeft() {
    return !redoStack.isEmpty();
  }

  public boolean hasUndoesLeft() {
    return !undoStack.isEmpty();
  }
}
