package com.roborally.controller;

import com.roborally.model.Player;
import com.roborally.model.Position;
import com.roborally.model.Space;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriorityAntenna extends FieldAction {

  @Override
  public boolean doAction(GameController gameController, Space space) {
    if (!gameController.activatePriorityAntenna)
      return false;

    Map<Player, Double> playerDistances = new HashMap<>(); // Associate a player with a distance from the antenna.
    for (Player player : gameController.board.getPlayers()) {
      Position playerPos = player.getPosition();
      Position antennaPos = new Position(space.x, space.y);

      double deltaX = Math.abs(playerPos.x - antennaPos.x);
      double deltaY = Math.abs(playerPos.y - antennaPos.y);
      double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
      if (gameController.board.getPlayersNumber() != 1)
        System.out.println("Activated antenna. " + player.getName() + " distance = " + distance);
      playerDistances.put(player, distance);
    }

    List<Player> playerOrder = getPlayerOrderAsList(playerDistances);
    gameController.board.setPlayerOrder(playerOrder);
    return true;
  }

  private List<Player> getPlayerOrderAsList(Map<Player, Double> playerDistances) {
    List<Player> playerOrder = new ArrayList<>();
    while (playerDistances.size() != 0) {
      Player player = Collections.min(playerDistances.entrySet(), Map.Entry.comparingByValue())
          .getKey();
      playerDistances.remove(player);
      playerOrder.add(player);
    }
    return playerOrder;
  }
}
