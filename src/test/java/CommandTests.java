import com.roborally.controller.AppController;
import com.roborally.controller.GameController;
import com.roborally.model.Board;
import com.roborally.model.Command;
import com.roborally.model.CommandCard;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class CommandTests {
  AppController app;
  GameController game;
  Board board;
  Player player1;
  Player player2;

  @BeforeEach
  void reset() {
    app = new AppController(null);
    app.newGameWithTestBoard();
    game = app.getGameController();
    board = game.board;
    player1 = board.getPlayer(0);
    player2 = board.getPlayer(1);
    player2.setRebooting(true);
  }

  @Test
  @Order(1)
  void testUndoingRobotMovements() {
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.LEFT), new CommandCard(Command.MOVE3), new CommandCard(Command.U_TURN), new CommandCard(Command.MOVE2)), player1, game);

    Position expectedPosition = new Position(1,3);
    Heading expectedHeading = Heading.WEST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);

    for (int i = 0; i < 4; i++) { // undo 4 of the 5 moves
      game.playerCommandManager.undoLast();
    }

    expectedPosition = new Position(0, 3);
    expectedHeading = Heading.SOUTH;
    actualPosition = TestUtil.getPos(player1);
    actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  @Order(2)
  void testRedoingRobotMovements() {
    for (int i = 0; i < 4; i++) { // undo 4 of the 5 moves
      game.playerCommandManager.redoLast();
    }

    Position expectedPosition = new Position(0,2);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  @Order(3)
  void testUndoingFieldActions() {
    player2.setRebooting(true);
    TestUtil.performMoves(List.of(new CommandCard(Command.U_TURN), new CommandCard(Command.MOVE1)), player1, game);

    Position expectedPosition = new Position(2,0);
    Heading expectedHeading = Heading.NORTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);

    while (game.playerCommandManager.hasUndoesLeft()) {
      game.playerCommandManager.undoLast();
    }

    expectedPosition = new Position(0, 2);
    expectedHeading = Heading.SOUTH;
    actualPosition = TestUtil.getPos(player1);
    actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  @Order(4)
  void testRedoingFieldActions() {
    for (int i = 0; i < 4; i++) {
      game.playerCommandManager.redoLast();
    }

    Position expectedPosition = new Position(0,2);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }
}
