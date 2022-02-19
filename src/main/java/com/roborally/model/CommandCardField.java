package com.roborally.model;

import designpatterns.observer.Subject;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CommandCardField extends Subject {

    final public Player player;

    private CommandCard card;

    private boolean visible;

    public CommandCardField(Player player) {
        this.player = player;
        this. card = null;
        this.visible = true;
    }

    public CommandCard getCard() {
        return card;
    }

    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }
}
