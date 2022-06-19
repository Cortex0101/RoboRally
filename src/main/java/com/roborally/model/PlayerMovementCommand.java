package com.roborally.model;

import com.roborally.controller.GameController;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Eiruff
 *
 * Base class for all player movement commands.
 *
 * This class has a single abstract method 'execute' which will have to be implemented by
 * its subclasses. The subclasses implementation of execute is expected to class backup at some
 * point, to store the previous state of the player, such that the actions performed can be
 * undone via the undo method later.
 *
 * We store lists of each player and their previous headings and spaces as many of the commands
 * may alter the positions / direction of other players.
 *
 * For example when a player MOVE2's into another player, he will move that player two spaces aswell
 * without the other player itself having executed any command.
 */
public abstract class PlayerMovementCommand {
  protected List<Player> players;
  protected Player initiator; // player that is executing the move

  private List<Heading> prevPlayerHeadings = new ArrayList<>();
  private List<Space> prevPlayerSpaces = new ArrayList<>();

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
