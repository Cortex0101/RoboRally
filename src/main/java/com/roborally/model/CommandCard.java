package com.roborally.model;

import designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CommandCard extends Subject {

    final public Command command;

    public CommandCard(@NotNull Command command) {
        this.command = command;
    }

    public String getName() {
        return command.displayName;
    }


}
