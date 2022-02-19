package com.roborally.controller;

import com.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        if (space.getPlayer() != null)
            return;

        final Player currentPlayer = board.getCurrentPlayer();

        // Move player
        currentPlayer.getSpace().setPlayer(null);
        space.setPlayer(currentPlayer);

        // Set next player as active
        final int playerNumber = board.getPlayerNumber(currentPlayer);
        if (playerNumber < (board.getPlayersNumber() - 1)) {
            board.setCurrentPlayer(board.getPlayer(playerNumber + 1));
        } else {
            board.setCurrentPlayer(board.getPlayer(0));
        }

        // Increment counter
        board.incrementTotalMoves();
    }

    /**
     * A method called when no corresponding controller operation is implemented yet.
     * This method should eventually be removed.
     */
    public void notImplememted() {
        // XXX just for now to indicate that the actual method to be used by a handler
        //     is not yet implemented
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null & targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

}
