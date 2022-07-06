package com.roborally.model;

import com.roborally.controller.GameController;
import com.roborally.controller.PushPanel;
import com.roborally.controller.PushPanel.Type;
import java.util.List;

public class PushPanelCommand extends PlayerMovementCommand {

  private static final List<Integer> labels1 = List.of(1, 3, 5);
  private static final List<Integer> labels2 = List.of(2, 4);

  private List<Integer> labels;
  private final Heading heading;

  private PushPanel.Type type;

  public Type getType() {
    return type;
  }

  public Heading getHeading() {
    return heading;
  }

  public PushPanelCommand(List<Player> players, Player initiator, Heading heading, PushPanel.Type type) {
    super(players, initiator);
    this.heading = heading;
    this.type = type;
    if (type == Type.PUSH_PANEL_1) {
      labels = labels1;
    } else {
      labels = labels2;
    }
  }

  @Override
  public void execute(GameController gameController) {
    if (!labels.contains(initiator.board.getStep()))
      return;

    backup();
    Heading originalPlayerHeading = initiator.getHeading();
    initiator.setHeading(heading);
    gameController.move1Forward(initiator);
    initiator.setHeading(originalPlayerHeading);
  }
}
