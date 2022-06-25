package com.roborally.controller;

import com.roborally.model.Board;
import com.roborally.model.Command;
import com.roborally.model.CommandCard;
import com.roborally.model.CommandCardField;
import com.roborally.model.Player;
import com.roborally.model.Space;
import designpatterns.observer.Itertools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RoboAI {

  private final GameController gameOrig;
  private final Player aiPlayerOrig;
  private final GameController gameCopy;
  private final Board boardCopy;
  private final Player aiPlayerCopy;


  /**
   * @param AIplayerOrig The player this class should generate moves for
   * @author Lucas Eiruff
   * <p>
   * Sets a player as an AI
   */
  public RoboAI(AppController originalAppController, Player AIplayerOrig, boolean fromNew) {
    gameOrig = originalAppController.getGameController();
    Board boardOrig = gameOrig.board;
    aiPlayerOrig = AIplayerOrig;

    // Below fields are used to store a copy of the actual app and game, so we can move the robots around a copy of the board,
    // without affecting the board visible to the player
    AppController appCopy = new AppController(
        null); // RoboRally is only used for GUI so we just pass null
    appCopy.newGameWithoutUI(boardOrig.boardName, fromNew);
    gameCopy = appCopy.getGameController();
    boardCopy = gameCopy.board;
    //aiPlayerCopy = new Player(boardCopy, "red", "AIPlayer", boardCopy.getSpace(AIplayerOrig.getStartingSpace().x, AIplayerOrig.getSpace().y)); // Create a new player, no copy - as this would make changes to the actual visible AI player
    boardCopy.addSinglePlayer(boardCopy.getPlayer(boardOrig.getPlayerNumber(AIplayerOrig)));
    aiPlayerCopy = boardCopy.getPlayer(0);
    boardCopy.setCurrentPlayer(aiPlayerCopy);
    gameCopy.moveCurrentPlayerToSpace(aiPlayerCopy.getStartingSpace());
  }

  public Player getAiPlayerOrig() {
    return aiPlayerOrig;
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Since the originals and the copies arent linked, changes to the AI's positions in the original
   * dont update in the copy. Therefor call this method before doing any calculations to make sure
   * the AI's proper positions are set.
   */
  public void updateAIPosition() {
    boardCopy.setCurrentPlayer(aiPlayerCopy);
    gameCopy.moveCurrentPlayerToSpace(
        boardCopy.getSpace(aiPlayerOrig.getSpace().x, aiPlayerOrig.getSpace().y));
    aiPlayerCopy.setHeading(gameOrig.board.getPlayer(1).getHeading());
  }

  /**
   * @param commandCards The commands to be executed.
   * @author Lucas Eiruff
   * <p>
   * Programs the robot with the command cards specified and executes the program on the boardCopy.
   */
  public void performMoves(List<CommandCard> commandCards) {
    for (int i = 0; i < Player.NO_REGISTERS; i++) {
      CommandCardField field = aiPlayerCopy.getProgramField(i);
      field.setCard(commandCards.get(i));
    }
    gameCopy.finishProgrammingPhase();
    gameCopy.executePrograms();
  }

  /**
   * @param indexes The indexes
   * @return Returns a list of command cards drawn from the command card deck at the indexes
   * @author Lucas Eiruff
   * <p>
   * Get command cards based on indexes corresponding to the command card in the programming deck
   * at
   */
  private List<CommandCard> getCommandCardsBasedOnIndexes(List<Integer> indexes) {
    List<CommandCard> commandCards = new ArrayList<>();
    for (int index : indexes) {
      CommandCard card = aiPlayerOrig.getCardField(index)
          .getCard(); // We use the original player as this is the one that has the proper cards.
      // For now a LEFT_OR_RIGHT command will randomly select one or the other.
      if (card.getName().equals("Left OR Right")) {
        if (new Random().nextBoolean()) {
          card = new CommandCard(Command.RIGHT);
        } else {
          card = new CommandCard(Command.LEFT);
        }
      }
      commandCards.add(card);
    }
    return commandCards;
  }

  /**
   * @param desiredSpace The space to try to get closest to
   * @return The programming cards selected from the programming deck, that gets the robot closest
   * to the desired space
   * @author Lucas Eiruff
   * <p>
   * Find the most optimal program to get closest to space
   */
  public List<CommandCard> findBestProgramToGetTo(Space desiredSpace) {
    List<Integer> commandCardIndexes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
    List<CommandCard> bestProgrammingDeckFound = null;
    int shortestDistanceFound = Integer.MAX_VALUE;

    for (List<Integer> commandCardPermutations : Itertools.permutations(commandCardIndexes, 5)) {
      List<CommandCard> cards = getCommandCardsBasedOnIndexes(commandCardPermutations);
      updateAIPosition();
      performMoves(cards);
      int distance = boardCopy.getDistanceBetweenSpaces(aiPlayerCopy.getSpace(), desiredSpace);
      if (distance < shortestDistanceFound) {
        shortestDistanceFound = distance;
        bestProgrammingDeckFound = cards;
      }
    }
    return bestProgrammingDeckFound;
  }
}
