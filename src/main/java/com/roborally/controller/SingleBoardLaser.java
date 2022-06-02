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
  public List<Space> getSpacesInPath(Board board, Space origin) {
    ArrayList<Space> spaces = new ArrayList<>();
    spaces.add(origin);
    if (origin.getPlayer() != null) {
      return spaces;
    }

    Space space = origin;
    while (true) {
      space = board.getNeighbour(space, heading);

      if (space == null) {
        break;
      }

      if (space.getPlayer() != null) {
        spaces.add(space);
        return spaces;
      }

      if (space.getWalls().stream().noneMatch(heading1 -> heading1 == getHeading().prev().prev())) {
        spaces.add(space);
      } else {
        return spaces;
      }
    }
    return spaces;
  }

  @Override
  public boolean doAction(GameController gameController, Space space) {
    if (gameController.board.getPlayersNumber() == 1)
      return false;
    List<Space> spaces = getSpacesInPath(gameController.board, space);

    Space lastSpace = spaces.get(spaces.size() - 1);
    if (lastSpace.getPlayer() != null) {
      // Only print if this is not a test board
        System.out.println("Hit player: " + lastSpace.getPlayer().getName());
      return true;
    }

    return false;
  }
}
