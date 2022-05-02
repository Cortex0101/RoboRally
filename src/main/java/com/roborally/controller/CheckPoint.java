package com.roborally.controller;

import com.roborally.model.Player;
import com.roborally.model.Space;

public class CheckPoint extends FieldAction {
    private int checkpoint;

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player == null) {
            return false;
        }

        // TODO: Load the nubmer of checkpoint from the json file.
        // Save it to the game controller and check here if the palyers current checkpoint is the last
        // If so, we should call something like gameController.setWinner(player);
        if (player.getLastCheckpoint() + 1 == getCheckpoint())
        player.setLastCheckpoint(getCheckpoint());

        return false;
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }
}
