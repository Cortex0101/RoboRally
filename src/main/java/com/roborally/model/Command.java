/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.roborally.model;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public enum Command {
    MOVE1("Move 1"),
    MOVE2("Move 2"),
    MOVE3("Move 3"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    U_TURN("U-turn"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);

    final public String displayName;

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = List.of(options);
    }

    final private List<Command> options;

    public boolean isInteractive() {
        return !options.isEmpty();
    }

    public List<Command> getOptions() {
        return options;
    }

}
