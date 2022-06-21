package com.roborally;

import com.roborally.controller.AppController;
import com.roborally.controller.GameController;
import com.roborally.model.Board;
import com.roborally.model.Command;
import com.roborally.model.CommandCard;
import com.roborally.model.CommandCardField;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RobotMovementTest {

  private GameController game;
  private Board board;

  private final int move3 = 2;
  private final int turnLR = 6;


  @BeforeEach
  void setup() {
    AppController app = new AppController(null);
    app.newGameWithoutUI("testboard", false);
    game = app.getGameController();
    board = game.board;

    resetPlayerRegisters();
  }

  /**
   * 0 1 2 3 4 5 6 7 0 S x x x x x x x      x x x x x x x x      x x x x x x x x      x x x x x x x
   * x      x x x x x x x x      x x x x x x x x 1 x 2 x x x x x x      x 2 x x x x x x      x 2 x x
   * x x x x      x 2 x x x x x x      x 2 x x x x x x      x 2 x x x x x x 2 x x x x x x x x      S
   * x x x x x x x      E x x x x x x x      x E x x x x x x      x S x x x x x x      x N x x x x x
   * x 3 x x x x x x x x      x x x x x x x x      x x x x x x x x      x x x x x x x x      x x x x
   * x x x x      x x x x x x x x 4 x x x x x x x x      x x x x x x x x      x x x x x x x x      x
   * x x x x x x x      x x x x x x x x      x x x x x x x x 5 x x x x x x x x      x x x x x x x x
   *     x x x x x x x x      x x x x x x x x      x x x x x x x x      x x x x x x x x 6 x x x x x
   * x x x      x x x x x x x x      x x x x x x x x      x x x x x x x x      x x x x x x x x
   * x x x x x x x x 7 x x x x x x x x      x x x x x x x x      x x x x x x x x      x x x x x x x
   * x      x x x x x x x x      x x x x x x x x
   */
  @Test
  void basicMovement() {
    Player player = board.getPlayer(0);

    Command[] commands = Command.values();
    int move1 = 0;
    int move2 = 1;
    int turnR = 3;
    int turnL = 4;
    int turnU = 5;
    setPlayerProgram(player, List.of(
        new CommandCard(commands[move2]),
        new CommandCard(commands[turnL]),
        new CommandCard(commands[move1]),
        new CommandCard(commands[turnR]),
        new CommandCard(commands[turnU])));

    game.finishProgrammingPhase();

    int[] expectedSpace = new int[]{0, 0};
    int[] actualSpace = new int[]{player.getSpace().x, player.getSpace().y};
    Heading expectedHeading = Heading.SOUTH;
    Heading actualHeading = player.getHeading();
    Assertions.assertArrayEquals(expectedSpace, actualSpace);
    Assertions.assertEquals(expectedHeading, actualHeading);

    game.executeStep();
    game.executeStep();
    expectedSpace = new int[]{0, 2};
    actualSpace = new int[]{player.getSpace().x, player.getSpace().y};
    actualHeading = player.getHeading();
    Assertions.assertArrayEquals(expectedSpace, actualSpace);
    Assertions.assertEquals(expectedHeading, actualHeading);

    game.executeStep();
    game.executeStep();
    expectedSpace = new int[]{0, 2};
    actualSpace = new int[]{player.getSpace().x, player.getSpace().y};
    expectedHeading = Heading.EAST;
    actualHeading = player.getHeading();
    Assertions.assertArrayEquals(expectedSpace, actualSpace);
    Assertions.assertEquals(expectedHeading, actualHeading);

    game.executeStep();
    game.executeStep();
    expectedSpace = new int[]{1, 2};
    actualSpace = new int[]{player.getSpace().x, player.getSpace().y};
    actualHeading = player.getHeading();
    Assertions.assertArrayEquals(expectedSpace, actualSpace);
    Assertions.assertEquals(expectedHeading, actualHeading);

    game.executeStep();
    game.executeStep();
    expectedSpace = new int[]{1, 2};
    actualSpace = new int[]{player.getSpace().x, player.getSpace().y};
    expectedHeading = Heading.SOUTH;
    actualHeading = player.getHeading();
    Assertions.assertArrayEquals(expectedSpace, actualSpace);
    Assertions.assertEquals(expectedHeading, actualHeading);

    game.executeStep();
    game.executeStep();
    expectedSpace = new int[]{1, 2};
    actualSpace = new int[]{player.getSpace().x, player.getSpace().y};
    expectedHeading = Heading.NORTH;
    actualHeading = player.getHeading();
    Assertions.assertArrayEquals(expectedSpace, actualSpace);
    Assertions.assertEquals(expectedHeading, actualHeading);
  }

  private void setPlayerProgram(Player player, List<CommandCard> cards) {
    for (int i = 0; i < Player.NO_REGISTERS; i++) {
      CommandCardField field = player.getProgramField(i);
      field.setCard(cards.get(i));
      field.setVisible(true);
    }
  }

  private void resetPlayerRegisters() {
    game.startProgrammingPhase(false);
    for (int i = 0; i < board.getPlayersNumber(); i++) {
      Player player = board.getPlayer(i);
      if (player != null) {
        for (int j = 0; j < Player.NO_REGISTERS; j++) {
          CommandCardField field = player.getProgramField(j);
          field.setCard(null);
          field.setVisible(true);
        }
      }
    }
  }
}
