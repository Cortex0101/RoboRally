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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FieldActionsTest {
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
  void testBlueConveyorBelt() {
    player2.setRebooting(true);
    TestUtil.performMoves(List.of(new CommandCard(Command.U_TURN), new CommandCard(Command.MOVE2)), player1, game);

    Position expectedPosition = new Position(2, 0);
    Heading expectedHeading = Heading.NORTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void testGreenConveyorBelt() {
    player2.setRebooting(true);
    TestUtil.performMoves(List.of(new CommandCard(Command.U_TURN), new CommandCard(Command.MOVE1)), player1, game);

    Position expectedPosition = new Position(2, 0);
    Heading expectedHeading = Heading.NORTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void testGear() {
    player2.setRebooting(true);
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.LEFT), new CommandCard(Command.MOVE2), new CommandCard(Command.MOVE1), new CommandCard(Command.MOVE1)), player1, game);

    Position expectedPosition = new Position(4, 3);
    Heading expectedHeading = Heading.NORTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void testSingleBoardLaser() {
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1)), player2, game);

    Position expectedPosition = new Position(1, 0);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player2);
    Heading actualHeading = player2.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void testSingleBoardLaserNonOrigin() {
    TestUtil.performMoves(List.of(new CommandCard(Command.LEFT), new CommandCard(Command.MOVE1), new CommandCard(Command.RIGHT), new CommandCard(Command.MOVE1)), player2, game);

    Position expectedPosition = new Position(1, 0);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player2);
    Heading actualHeading = player2.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void testPit() {
    TestUtil.performMoves(List.of(new CommandCard(Command.LEFT), new CommandCard(Command.MOVE2), new CommandCard(Command.MOVE2)), player2, game);

    Position expectedPosition = new Position(1, 0);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player2);
    Heading actualHeading = player2.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void testCheckPoint() {
    TestUtil.performMoves(List.of(new CommandCard(Command.LEFT), new CommandCard(Command.MOVE1)), player1, game);

    Position expectedPosition = new Position(1, 2);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();
    int expectedCheckpoint = 1;
    int actualCheckpoint = player1.getLastCheckpoint();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
    Assertions.assertEquals(expectedCheckpoint, actualCheckpoint);
  }
}
