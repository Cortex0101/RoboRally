package com.roborally.model;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public enum Heading {

    SOUTH, WEST, NORTH, EAST;

    public Heading next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Heading prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }
}
