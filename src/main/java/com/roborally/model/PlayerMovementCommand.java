package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Eiruff
 * <p>
 * Base class for all player movement commands.
 * <p>
 * This class has a single abstract method 'execute' which will have to be implemented by its
 * subclasses. The subclasses implementation of execute is expected to class backup at some point,
 * to store the previous state of the player, such that the actions performed can be undone via the
 * undo method later.
 * <p>
 * We store lists of each player and their previous headings and spaces as many of the commands may
 * alter the positions / direction of other players.
 * <p>
 * For example when a player MOVE2's into another player, he will move that player two spaces aswell
 * without the other player itself having executed any command.
 */
public abstract class PlayerMovementCommand {

  protected final List<Player> players;
  protected final Player initiator; // player that is executing the move

  private final List<Heading> prevPlayerHeadings = new ArrayList<>();
  private final List<Space> prevPlayerSpaces = new ArrayList<>();

  PlayerMovementCommand(List<Player> players, Player initiator) {
    this.players = players;
    this.initiator = initiator;
  }

  void backup() {
    for (Player player : players) {
      this.prevPlayerHeadings.add(player.getHeading());
      this.prevPlayerSpaces.add(player.getSpace());
    }
  }

  public void undo() {
    for (int i = 0; i < players.size(); i++) {
      players.get(i).setHeading(prevPlayerHeadings.get(i));
      players.get(i).setSpace(prevPlayerSpaces.get(i));
    }
  }

  public abstract void execute(GameController gameController);
}
