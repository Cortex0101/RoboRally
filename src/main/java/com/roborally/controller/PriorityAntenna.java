package com.roborally.controller;

import com.roborally.model.Player;
import com.roborally.model.Space;

import java.util.*;

public class PriorityAntenna extends FieldAction {

  @Override
  public boolean doAction(GameController gameController, Space space) {
    Map<Player, Double> playerDistances = new HashMap<>(); // Associate a player with a distance from the antenna.
    for (int i = 0; i < gameController.board.getPlayersNumber(); i++) {
      Player player = gameController.board.getPlayer(i);
        if (player.getSpace() == null) {
            return false;
        }
      int[] playerPos = new int[]{player.getSpace().x, player.getSpace().y};
      int[] antennaPos = new int[]{space.x, space.y};
      int deltaX = Math.abs(playerPos[0] - antennaPos[0]);
      int deltaY = Math.abs(playerPos[1] - antennaPos[1]);
      Double distance = Math.sqrt((deltaX * deltaY) + (deltaY * deltaY));
      playerDistances.put(player, distance);
    }

    List<Player> playerOrder = new ArrayList<>();
    while (playerDistances.size() != 0) {
      Player player = Collections.max(playerDistances.entrySet(), Map.Entry.comparingByValue())
          .getKey();
      playerDistances.remove(player);
      playerOrder.add(player);
    }
    return true;
  }
}
