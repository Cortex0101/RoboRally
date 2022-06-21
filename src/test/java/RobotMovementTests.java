import com.roborally.controller.AppController;
import com.roborally.controller.GameController;
import com.roborally.model.Board;
import com.roborally.model.Command;
import com.roborally.model.CommandCard;
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

  private void performMoves(List<CommandCard> commandCards, Player player) {
    assert commandCards.size() <= 5;
    for (int i = 0; i < commandCards.size(); i++) {
      player.getProgramField(i).setCard(commandCards.get(i));
    }
    game.finishProgrammingPhase();
    game.executePrograms();
  }

  @Test
  void move1Test() {
    Assertions.assertTrue(player1.getSpace().x == 0 && player1.getSpace().y == 2);
    performMoves(List.of(new CommandCard(Command.MOVE1)), player1);
    Assertions.assertTrue(player1.getSpace().x == 0 && player1.getSpace().y == 3);
  }
}