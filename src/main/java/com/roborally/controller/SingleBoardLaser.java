package com.roborally.controller;

import com.roborally.model.Board;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SingleBoardLaser extends FieldAction {
  private Heading heading;
  GameController gameController;

  public Heading getHeading() {
    return heading;
  }

  public void setHeading(Heading heading) {
    this.heading = heading;
  }

  /**
   * Find all spaces that the laser hits.
   * A laser goes from the origin, in the heading, until it hits a wall, the priority antenna or another robot
   * @param board the board to which the space belongs
   * @param origin the space containing the board laser
   * @return a list of all the spaces that the laser will hit
   */
  private List<Space> getSpacesInPath(Board board, Space origin) {
    ArrayList<Space> spaces = new ArrayList<>();
    spaces.add(origin);
    while (true) {
      Space space = board.getNeighbour(origin, heading);

      if (space == null) {
        break;
      }

      if (space.getPlayer() != null) {
        spaces.add(space);
        break;
      }

      if (space.getWalls().stream().noneMatch(heading1 -> heading1 == getHeading().prev().prev())) {
        spaces.add(space);
      } else {
        break;
      }
    }
    return spaces;
  }

  @Override
  public boolean doAction(GameController gameController, Space space) {
    List<Space> spaces = getSpacesInPath(gameController.board, space);

    Space lastSpace = spaces.get(spaces.size() - 1);
    if (lastSpace.getPlayer() != null) {
      System.out.println("Hit player: " + lastSpace.getPlayer().getName());
      return true;
    }

    return false;
  }
}
