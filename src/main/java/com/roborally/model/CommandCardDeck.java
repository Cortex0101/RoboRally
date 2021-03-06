package com.roborally.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class represents a deck of command cards.
 * A deck contains 20 command cards namely:
 *
 * <ul>
 *   <li>3 move 1 commands</li>
 *   <li>3 move 2 commands</li>
 *   <li>2 move 3 commands</li>
 *   <li>2 turn left command/li>
 *   <li>2 turn right commands</li>
 *   <li>2 turn left or right commands</li>
 *   <li>2 u-turn commands</li>
 *   <li>2 again command</li>
 *   <li>2 back-up commands</li>
 * </ul>
 *
 * One can draw up to 9 command cards from the deck at once.
 * After a player has used the command cards they should be placed back into the deck.
 *
 * If the deck does not have enough cards left when a player asks to draw a card, the deck is
 * reshuffled and the cards are drawn again.
 *
 * @author Lucas Eiruff
 */
public class CommandCardDeck {
  // Field contains the cards that this deck should contain when no cards have been drawn.
  private static final CommandCard[] originalCards = new CommandCard[] {
      new CommandCard(Command.MOVE1),
      new CommandCard(Command.MOVE1),
      new CommandCard(Command.MOVE1),
      new CommandCard(Command.MOVE2),
      new CommandCard(Command.MOVE2),
      new CommandCard(Command.MOVE2),
      new CommandCard(Command.MOVE3),
      new CommandCard(Command.MOVE3),
      new CommandCard(Command.LEFT),
      new CommandCard(Command.LEFT),
      new CommandCard(Command.RIGHT),
      new CommandCard(Command.RIGHT),
      new CommandCard(Command.OPTION_LEFT_RIGHT),
      new CommandCard(Command.OPTION_LEFT_RIGHT),
      new CommandCard(Command.U_TURN),
      new CommandCard(Command.U_TURN),
      new CommandCard(Command.AGAIN),
      new CommandCard(Command.AGAIN),
      new CommandCard(Command.BACK_UP),
      new CommandCard(Command.BACK_UP)
  };

  // Field contains the cards that this deck currently contains.
  private List<CommandCard> programmingDeck = new ArrayList<>(originalCards.length);

  // Field contains the 8 cards that have been drawn from this deck and are availble to program.
  private List<CommandCard> drawnCards = new ArrayList<>(8);

  public CommandCardDeck() {
    reshuffle();
  }

  /**
   * Draws n cards from the deck
   *
   * @return The n cards drawn.
   */
  public void drawCards(int numberOfCards) {
    drawnCards.clear();
    if (programmingDeck.size() < numberOfCards) {
      drawnCards.addAll(new ArrayList<>(programmingDeck.subList(0, programmingDeck.size())));
      programmingDeck.subList(0, programmingDeck.size()).clear();
      numberOfCards -= programmingDeck.size();
      reshuffle();
    }
    drawnCards.addAll(new ArrayList<>(programmingDeck.subList(0, numberOfCards)));
    programmingDeck.subList(0, numberOfCards).clear();
  }

  public List<CommandCard> getDrawnCards() {
    return drawnCards;
  }

  /**
   * Places the given cards back into the deck.
   *
   * @param cards The cards to be placed back into the deck.
   */
  public void addCardsToDeck(List<CommandCard> cards) {
    this.programmingDeck.addAll(cards);
  }

  /**
   * Reshuffles the deck.
   */
  private void reshuffle() {
    programmingDeck.clear();
    programmingDeck.addAll(Arrays.asList(originalCards));
    Collections.shuffle(programmingDeck);
  }

  /**
   * Returns the number of cards left in the deck.
   *
   * @return The number of cards left in the deck.
   */
  public int getNumberOfCardsLeft() {
    return programmingDeck.size();
  }
}




/*
public void generatePlayerCards() {
  for (Player player : board.getPlayers()) {
    for (CommandCardField field : player.getCards()) {
      field.setCard(generateRandomCommandCard());
      field.setVisible(true);
    }
  }
}

  private CommandCard generateRandomCommandCard() {
    Command[] commands = Command.values();
    int random = (int) (Math.random() * commands.length);
    return new CommandCard(commands[random]);
  }

 */