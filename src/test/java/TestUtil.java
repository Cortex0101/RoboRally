import com.roborally.controller.GameController;
import com.roborally.model.CommandCard;
import com.roborally.model.Player;
import java.util.List;
import java.util.Objects;

public class TestUtil {
  public static void performMoves(List<CommandCard> commandCards, Player player, GameController game) {
    assert commandCards.size() <= 5;
    for (int i = 0; i < commandCards.size(); i++) {
      player.getProgramField(i).setCard(commandCards.get(i));
    }
    game.finishProgrammingPhase();
    game.executePrograms();
  }

  public static Position getPos(Player player) {
    return new Position(player.getSpace().x, player.getSpace().y);
  }
}
