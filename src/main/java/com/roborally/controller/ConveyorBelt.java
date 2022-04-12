package com.roborally.controller;

import com.roborally.model.Board;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;
import org.jetbrains.annotations.NotNull;


public class ConveyorBelt extends FieldAction {
    private Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();
        if (player == null) {
            return false;
        }
        Heading playerHeading = player.getHeading();

        player.setHeading(heading);
        gameController.move1Forward(player);
        player.setHeading(playerHeading);

        return true;
    }

}

