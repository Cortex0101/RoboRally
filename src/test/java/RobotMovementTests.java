import Util.Position;
import Util.TestUtil;
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
import org.junit.jupiter.api.Test;

public class RobotMovementTests {
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
  }

  @Test
  void turnLeftTest() {
    TestUtil.performMoves(List.of(new CommandCard(Command.LEFT)), player1, game);

    Position expectedPosition = new Position(0, 2);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void turnRightTest() {
    TestUtil.performMoves(List.of(new CommandCard(Command.RIGHT)), player1, game);

    Position expectedPosition = new Position(0, 2);
    Heading expectedHeading = Heading.WEST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void uTurnTest() {
    TestUtil.performMoves(List.of(new CommandCard(Command.U_TURN)), player1, game);

    Position expectedPosition = new Position(0, 2);
    Heading expectedHeading = Heading.NORTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void move1Test() {
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1)), player1, game);

    Position expectedPosition = new Position(0,3);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void move2Test() {
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.LEFT), new CommandCard(Command.MOVE2)), player1, game);

    Position expectedPosition = new Position(2,3);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void move3Test() {
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.LEFT), new CommandCard(Command.MOVE3)), player1, game);

    Position expectedPosition = new Position(3,3);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void backUpTest() {
    TestUtil.performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.BACK_UP)), player1, game);

    Position expectedPosition = new Position(0,2);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void againTest() {
    TestUtil.performMoves(List.of(new CommandCard(Command.RIGHT), new CommandCard(Command.LEFT), new CommandCard(Command.AGAIN)), player1, game);

    Position expectedPosition = new Position(0,2);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }

  @Test
  void playersCantMovePastBordEdgesTest() {
    TestUtil.performMoves(List.of(new CommandCard(Command.RIGHT), new CommandCard(Command.MOVE2)), player1, game);

    Position expectedPosition = new Position(0,2);
    Heading expectedHeading = Heading.WEST;
    Position actualPosition = TestUtil.getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(expectedPosition, actualPosition);
    Assertions.assertSame(expectedHeading, actualHeading);
  }
}