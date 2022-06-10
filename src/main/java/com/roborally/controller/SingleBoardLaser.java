package com.roborally.controller;

import com.roborally.model.Board;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;
import com.roborally.model.Space.Laser;
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

  public void updateLaserSpaces(List<Space> spaces) {
    for (Space space : spaces) {
      if (heading == Heading.NORTH || heading == Heading.SOUTH) {
        if (space.getLaser() == Laser.horizontal) {
          space.setLaser(Laser.cross);
        } else {
          space.setLaser(Laser.vertical);
        }
      }
      else {
        if (space.getLaser() == Laser.vertical) {
          space.setLaser(Laser.cross);
        } else {
          space.setLaser(Laser.horizontal);
        }
      }
    }
  }

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
