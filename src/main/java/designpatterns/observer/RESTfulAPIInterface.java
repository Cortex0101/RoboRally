package designpatterns.observer;

import com.roborally.model.Board;
import com.roborally.model.Player;
import javafx.scene.paint.Color;

public interface RESTfulAPIInterface {
    Board getBoards(); //gets the board
    String createBoard(String saveName); //creates a new save
    void deleteBoard(String saveName); //deletes a save

    String getBoard(String saveName); //gets the board
    String updateBoard(String saveName); //overwrites the previous save

    void createPlayer(String name, Color color, boolean isAI);
    Player getAllPlayers();

    Player getPlayer();
    Player updatePlayer();

}
