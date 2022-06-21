import com.roborally.controller.AppController;
import com.roborally.controller.GameController;
import com.roborally.model.Board;
import com.roborally.model.Command;
import com.roborally.model.CommandCard;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

  private void performMoves(List<CommandCard> commandCards, Player player) {
    assert commandCards.size() <= 5;
    for (int i = 0; i < commandCards.size(); i++) {
      player.getProgramField(i).setCard(commandCards.get(i));
    }
    game.finishProgrammingPhase();
    game.executePrograms();
  }

  private class Position {
    Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public boolean is(int x, int y) {
      return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Position position = (Position) o;
      return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    public int x;
    public int y;
  }

  private Position getPos(Player player) {
    return new Position(player.getSpace().x, player.getSpace().y);
  }

  @Test
  void turnLeftTest() {
    performMoves(List.of(new CommandCard(Command.LEFT)), player1);

    Position expectedPosition = new Position(0, 2);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(actualPosition, expectedPosition);
    Assertions.assertSame(actualHeading, expectedHeading);
  }

  @Test
  void turnRightTest() {
    performMoves(List.of(new CommandCard(Command.RIGHT)), player1);

    Position expectedPosition = new Position(0, 2);
    Heading expectedHeading = Heading.WEST;
    Position actualPosition = getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(actualPosition, expectedPosition);
    Assertions.assertSame(actualHeading, expectedHeading);
  }

  void uTurnTest() {
    performMoves(List.of(new CommandCard(Command.LEFT)), player1);

    Position expectedPosition = new Position(0, 2);
    Heading expectedHeading = Heading.NORTH;
    Position actualPosition = getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(actualPosition, expectedPosition);
    Assertions.assertSame(actualHeading, expectedHeading);
  }

  @Test
  void move1Test() {
    performMoves(List.of(new CommandCard(Command.MOVE1)), player1);

    Position expectedPosition = new Position(0,3);
    Heading expectedHeading = Heading.SOUTH;
    Position actualPosition = getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(actualPosition, expectedPosition);
    Assertions.assertSame(actualHeading, expectedHeading);
  }

  @Test
  void move2Test() {
    performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.LEFT), new CommandCard(Command.MOVE2)), player1);

    Position expectedPosition = new Position(2,3);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(actualPosition, expectedPosition);
    Assertions.assertSame(actualHeading, expectedHeading);
  }

  @Test
  void move3Test() {
    performMoves(List.of(new CommandCard(Command.MOVE1), new CommandCard(Command.LEFT), new CommandCard(Command.MOVE3)), player1);

    Position expectedPosition = new Position(3,3);
    Heading expectedHeading = Heading.EAST;
    Position actualPosition = getPos(player1);
    Heading actualHeading = player1.getHeading();

    Assertions.assertEquals(actualPosition, expectedPosition);
    Assertions.assertSame(actualHeading, expectedHeading);
  }
}