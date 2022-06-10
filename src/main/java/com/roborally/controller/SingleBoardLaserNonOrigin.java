package com.roborally.controller;

import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;

public class SingleBoardLaserNonOrigin extends FieldAction {
    private Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player == null) {
            return false;
        }

        player.setRebooting(true);
        return true;
    }
}
