package com.roborally.controller;

import com.roborally.model.Board;
import com.roborally.model.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoboAI {
    // Below fields are used to store a copy of the actual app and game, so we can move the robots around a copy of the board,
    // without affecting the board visible to the player
    private AppController appCopy;
    private GameController gameCopy;
    private Board boardCopy;

    /**
     *
     * @param boardName name of the board in which the original game is being played.
     */
    public RoboAI(String boardName) {
        appCopy = new AppController(null); // RoboRally is only used for GUI so we just pass null
        appCopy.newGameWithoutUI("testboard");
        gameCopy = appCopy.getGameController();
        boardCopy = gameCopy.board;
    }

    /**
     * Since the originals and the copies arent linked, changes to the player positions in the original dont update
     * in the copy. Therefor call this method before doing any calculations to make sure the players proper positions
     * are set.
     * @param originalBoard the actual board being played on
     */
    public void updatePlayerPositions(Board originalBoard) {
        for (int i = 0; i < originalBoard.getPlayersNumber(); i++) {
            final Player player = originalBoard.getPlayer(i);
            boardCopy.setCurrentPlayer(player);
            gameCopy.moveCurrentPlayerToSpace(player.getSpace());
        }
    }
}
