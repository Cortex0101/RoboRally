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
package com.roborally.view;

import com.roborally.controller.BlueConveyorBelt;
import com.roborally.controller.CheckPoint;
import com.roborally.controller.FieldAction;
import com.roborally.controller.Gear;
import com.roborally.controller.GreenConveyorBelt;
import com.roborally.controller.PriorityAntenna;
import com.roborally.controller.PushPanel;
import com.roborally.controller.PushPanel.Type;
import com.roborally.controller.SingleBoardLaser;
import com.roborally.controller.SingleBoardLaserNonOrigin;
import com.roborally.fileaccess.LoadBoard;
import com.roborally.model.Board;
import com.roborally.model.Heading;
import com.roborally.model.Player;
import com.roborally.model.Space;
import com.roborally.model.SpriteSheetSingleton;
import designpatterns.observer.Subject;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.scene.CacheHint;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class SpaceView extends StackPane implements ViewObserver {

  final public static int SPACE_HEIGHT = 60; // 60; // 75;
  final public static int SPACE_WIDTH = 60;  // 60; // 75;

  final public Space space;

  final public static Paint WALL_COLOR = Color.RED;
  final public static int WALL_THICKNESS = 5;

  final public static Paint GREEN_CONVEYOR_BELT_COLOR = Color.LIGHTGREEN;
  final public static Paint BLUE_CONVEYOR_BELT_COLOR = Color.LIGHTBLUE;


  public SpaceView(@NotNull Space space) {
    this.space = space;

    this.setPrefWidth(SPACE_WIDTH);
    this.setMinWidth(SPACE_WIDTH);
    this.setMaxWidth(SPACE_WIDTH);

    this.setPrefHeight(SPACE_HEIGHT);
    this.setMinHeight(SPACE_HEIGHT);
    this.setMaxHeight(SPACE_HEIGHT);

    space.attach(this);
    SpriteSheetSingleton.getInstance().spriteSheet.setSpaceSize(this.getPrefWidth(),
        this.getPrefHeight());
    update(space);
  }

  /**
   * reColor the given InputImage to the given color
   * inspired by https://stackoverflow.com/a/12945629/1497139
   * @param inputImage
   * @param sourceColor
   * @param finalColor
   * @return reColored Image
   *
   */
  private static Image reColor(Image inputImage, Color sourceColor, Color finalColor) {
    int            W           = (int) inputImage.getWidth();
    int            H           = (int) inputImage.getHeight();
    WritableImage outputImage = new WritableImage(W, H);
    PixelReader reader      = inputImage.getPixelReader();
    PixelWriter writer      = outputImage.getPixelWriter();
    float          ocR         = (float) sourceColor.getRed();
    float          ocG         = (float) sourceColor.getGreen();
    float          ocB         = (float) sourceColor.getBlue();
    float          ncR         = (float) finalColor.getRed();
    float          ncG         = (float) finalColor.getGreen();
    float          ncB         = (float) finalColor.getBlue();
    java.awt.Color oldColor    = new java.awt.Color(ocR, ocG, ocB);
    java.awt.Color newColor    = new java.awt.Color(ncR, ncG, ncB);
    for (int y = 0; y < H; y++) {
      for (int x = 0; x < W; x++) {
        int            argb       = reader.getArgb(x, y);
        java.awt.Color pixelColor = new java.awt.Color(argb, true);
        writer.setArgb(x, y,
            pixelColor.equals(oldColor) ?
                newColor.getRGB() :
                pixelColor.getRGB());
      }
    }
    return outputImage;
  }

  private ImageView playerView = new ImageView(new Image( System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\images\\player.png"));

  private void updatePlayer() {
    Player player = space.getPlayer();
    if (player == null) 
      return;

    playerView = new ImageView(reColor(playerView.getImage(), Color.WHITE, Color.valueOf(player.getColor())));

    playerView.setFitWidth(SPACE_WIDTH);
    playerView.setFitHeight(SPACE_HEIGHT);
    this.playerView.setRotate((90 * player.getHeading().ordinal() % 360) + 90);

    this.getChildren().add(playerView);
    
    /*
      Polygon arrow = new Polygon(0.0, 0.0,
          10.0, 20.0,
          20.0, 0.0);
      try {
        arrow.setFill(Color.valueOf(player.getColor()));
      } catch (Exception e) {
        arrow.setFill(Color.MEDIUMPURPLE);
      }

      arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
      this.getChildren().add(arrow);

      Canvas cvs = new Canvas();
      cvs.setWidth(SPACE_WIDTH);
      cvs.setHeight(SPACE_HEIGHT);
      cvs.setLayoutX(0);
      cvs.setLayoutY(0);
      this.getChildren().add(cvs);

      GraphicsContext gc = cvs.getGraphicsContext2D();
      double x = ((cvs.getWidth() - 20) / 2) + 7;
      double y = ((cvs.getHeight() - 20) / 2);
      StackPane sPane = new StackPane();
      sPane.setPrefSize(20, 20);

      Text txtNum = new Text(player.getLastCheckpoint() + "");
      sPane.getChildren().add(txtNum);
      SnapshotParameters parameters = new SnapshotParameters();
      parameters.setFill(Color.TRANSPARENT);
      gc.drawImage(sPane.snapshot(parameters, null), x, y);
     */
  }

  private void updateWall() {
    if (space.getWalls().isEmpty()) {
      return;
    }

    List<Heading> wallHeading = space.getWalls();
    if (wallHeading.size() == 1) {
      switch (wallHeading.get(0)) {
        case NORTH -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("north wall"));
        case EAST -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("east wall"));
        case SOUTH -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("south wall"));
        case WEST -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("west wall"));
      }
    } else {
      switch (wallHeading.get(0).toString() + wallHeading.get(1).toString()) {
        case "EASTNORTH", "NORTHEAST" -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("northeast wall"));
        case "EASTSOUTH", "SOUTHEAST" -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("southeast wall"));
        case "WESTNORTH", "NORTHWEST" -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("northwest wall"));
        case "WESTSOUTH", "SOUTHWEST" -> this.getChildren()
            .add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("southwest wall"));
      }
    }
  }

  private enum ConveyorBeltConnections {
    NONE,
    LEFT,
    RIGHT,
    LEFT_AND_RIGHT,
    LEFT_AND_BOTTOM,
    RIGHT_AND_BOTTOM,
    LEFT_AND_RIGHT_AND_BOTTOM
  }

  private ConveyorBeltConnections getGreenConveyorBeltConnections(Heading heading, Space space) {
    final Board board = space.board;

    boolean leftConnection = false;
    boolean rightConnection = false;
    boolean bottomConnection = false;

    try {
      switch (heading) {
        case NORTH:
          rightConnection = board.getNeighbour(space, Heading.EAST).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.WEST));
          leftConnection = board.getNeighbour(space, Heading.WEST).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.EAST));
          bottomConnection = board.getNeighbour(space, Heading.SOUTH).getActions().stream()
              .anyMatch(a -> a.getClass() == GreenConveyorBelt.class
                  && ((GreenConveyorBelt) a).getHeading().equals(Heading.NORTH));
          break;
        case EAST:
          rightConnection = board.getNeighbour(space, Heading.SOUTH).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.NORTH));
          leftConnection = board.getNeighbour(space, Heading.NORTH).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.SOUTH));
          bottomConnection = board.getNeighbour(space, Heading.EAST).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.WEST));
          break;
        case SOUTH:
          rightConnection = board.getNeighbour(space, Heading.WEST).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.EAST));
          leftConnection = board.getNeighbour(space, Heading.EAST).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.WEST));
          bottomConnection = board.getNeighbour(space, Heading.NORTH).getActions().stream()
              .anyMatch(a -> a.getClass() == GreenConveyorBelt.class
                  && ((GreenConveyorBelt) a).getHeading().equals(Heading.SOUTH));
          break;
        case WEST:
          rightConnection = board.getNeighbour(space, Heading.NORTH).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.SOUTH));
          leftConnection = board.getNeighbour(space, Heading.SOUTH).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.NORTH));
          bottomConnection = board.getNeighbour(space, Heading.WEST).getActions().stream().anyMatch(
              a -> a.getClass() == GreenConveyorBelt.class && ((GreenConveyorBelt) a).getHeading()
                  .equals(Heading.EAST));
          break;
      }
    } catch (NullPointerException ignored) {
    }

    if (rightConnection && leftConnection && bottomConnection)
      return ConveyorBeltConnections.LEFT_AND_RIGHT_AND_BOTTOM;
    else if (leftConnection && rightConnection)
      return ConveyorBeltConnections.LEFT_AND_RIGHT;
    else if (leftConnection && bottomConnection)
      return ConveyorBeltConnections.LEFT_AND_BOTTOM;
    else if (rightConnection && bottomConnection)
      return ConveyorBeltConnections.RIGHT_AND_BOTTOM;
    else if (leftConnection)
      return ConveyorBeltConnections.LEFT;
    else if (rightConnection)
      return ConveyorBeltConnections.RIGHT;
    else
      return ConveyorBeltConnections.NONE;
  }

  private void updateGreenConveyorBelt() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.GreenConveyorBelt")) {
        GreenConveyorBelt conveyorBelt = (GreenConveyorBelt) fieldAction;
        ImageView view2 = null;
        ConveyorBeltConnections connections = getGreenConveyorBeltConnections(
            conveyorBelt.getHeading(), space);
        view2 = switch (connections) {
          case NONE -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "green conveyor belt north");
          case LEFT -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "green conveyor belt left connection");
          case RIGHT -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "green conveyor belt right connection");
          case LEFT_AND_RIGHT, LEFT_AND_RIGHT_AND_BOTTOM -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "green conveyor belt left and right connection");
          case LEFT_AND_BOTTOM -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "green conveyor belt left and bottom connection");
          case RIGHT_AND_BOTTOM -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "green conveyor belt right and bottom connection");
        };

        switch (conveyorBelt.getHeading()) {
          case NORTH -> {
            view2.setRotate(0);
          }
          case EAST -> {
            view2.setRotate(90);
          }
          case SOUTH -> {
            view2.setRotate(180);
          }
          case WEST -> {
            view2.setRotate(270);
          }
        }

        this.getChildren().add(view2);
      }
    }
  }

  private ConveyorBeltConnections getBlueConveyorBeltConnections(Heading heading, Space space) {
    final Board board = space.board;

    boolean leftConnection = false;
    boolean rightConnection = false;
    boolean bottomConnection = false;

    try {
      switch (heading) {
        case NORTH:
          rightConnection = board.getNeighbour(space, Heading.EAST).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.WEST));
          leftConnection = board.getNeighbour(space, Heading.WEST).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.EAST));
          bottomConnection = board.getNeighbour(space, Heading.SOUTH).getActions().stream()
              .anyMatch(a -> a.getClass() == BlueConveyorBelt.class
                  && ((BlueConveyorBelt) a).getHeading().equals(Heading.NORTH));
          break;
        case EAST:
          rightConnection = board.getNeighbour(space, Heading.SOUTH).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.NORTH));
          leftConnection = board.getNeighbour(space, Heading.NORTH).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.SOUTH));
          bottomConnection = board.getNeighbour(space, Heading.EAST).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.WEST));
          break;
        case SOUTH:
          rightConnection = board.getNeighbour(space, Heading.WEST).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.EAST));
          leftConnection = board.getNeighbour(space, Heading.EAST).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.WEST));
          bottomConnection = board.getNeighbour(space, Heading.NORTH).getActions().stream()
              .anyMatch(a -> a.getClass() == BlueConveyorBelt.class
                  && ((BlueConveyorBelt) a).getHeading().equals(Heading.SOUTH));
          break;
        case WEST:
          rightConnection = board.getNeighbour(space, Heading.NORTH).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.SOUTH));
          leftConnection = board.getNeighbour(space, Heading.SOUTH).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.NORTH));
          bottomConnection = board.getNeighbour(space, Heading.WEST).getActions().stream().anyMatch(
              a -> a.getClass() == BlueConveyorBelt.class && ((BlueConveyorBelt) a).getHeading()
                  .equals(Heading.EAST));
          break;
      }
    } catch (NullPointerException ignored) {
    }

    if (rightConnection && leftConnection && bottomConnection)
      return ConveyorBeltConnections.LEFT_AND_RIGHT_AND_BOTTOM;
    else if (leftConnection && rightConnection)
      return ConveyorBeltConnections.LEFT_AND_RIGHT;
    else if (leftConnection && bottomConnection)
      return ConveyorBeltConnections.LEFT_AND_BOTTOM;
    else if (rightConnection && bottomConnection)
      return ConveyorBeltConnections.RIGHT_AND_BOTTOM;
    else if (leftConnection)
      return ConveyorBeltConnections.LEFT;
    else if (rightConnection)
      return ConveyorBeltConnections.RIGHT;
    else
      return ConveyorBeltConnections.NONE;
  }

  private void updateBlueConveyorBelt() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.BlueConveyorBelt")) {
        BlueConveyorBelt conveyorBelt = (BlueConveyorBelt) fieldAction;

        ImageView view2 = null;
        ConveyorBeltConnections connections = getBlueConveyorBeltConnections(conveyorBelt.getHeading(), space);
        view2 = switch (connections) {
          case NONE -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "blue conveyor belt north");
          case LEFT -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "blue conveyor belt left connection");
          case RIGHT -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "blue conveyor belt right connection");
          case LEFT_AND_RIGHT, LEFT_AND_RIGHT_AND_BOTTOM -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "blue conveyor belt left and right connection");
          case LEFT_AND_BOTTOM -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "blue conveyor belt left and bottom connection");
          case RIGHT_AND_BOTTOM -> SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "blue conveyor belt right and bottom connection");
        };

        switch (conveyorBelt.getHeading()) {
          case NORTH -> view2.setRotate(0);
          case EAST -> view2.setRotate(90);
          case SOUTH -> view2.setRotate(180);
          case WEST -> view2.setRotate(270);
        }

        this.getChildren().add(view2);
      }
    }
  }

  private void updateGears() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.Gear")) {
        Gear gear = (Gear) fieldAction;
        if (gear.getDirection().equals(Gear.Direction.LEFT)) {
          ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("left gear");
          view.toFront();
          this.getChildren().add(view);
        } else {
          ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("right gear");
          view.toFront();
          this.getChildren().add(view);
        }
      }
    }
  }

  private void updatePits() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.Pit")) {
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("pit");
        view.toFront();
        this.getChildren().add(view);
      }
    }
  }

  private void updateCheckPoints() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.CheckPoint")) {
        CheckPoint checkPoint = (CheckPoint) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
            "checkpoint " + checkPoint.getCheckpointNum());
        view.toFront();
        this.getChildren().add(view);
      }
    }
  }

  private void updateSingleLasers() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.SingleBoardLaser")) {
        SingleBoardLaser singleBoardLaser = (SingleBoardLaser) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
            "single laser wall");
        switch (singleBoardLaser.getHeading()) {
          case NORTH -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser wall north");
          case EAST -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser wall east");
          case SOUTH -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser wall south");
          case WEST -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser wall west");
        }
        this.getChildren().add(view);
        switch (singleBoardLaser.getHeading()) {
          case NORTH, SOUTH -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser vertical");
          case EAST, WEST -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser horizontal");
        }
        this.getChildren().add(view);
      }
    }
  }

  //Above method updates the actual wall with the laser shooter, while this one updates individual lasers
  private void updateSingleLaserNonOrigin() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName()
          .equals("com.roborally.controller.SingleBoardLaserNonOrigin")) {
        SingleBoardLaserNonOrigin singleBoardLaser = (SingleBoardLaserNonOrigin) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
            "single laser wall");
        switch (singleBoardLaser.getHeading()) {
          case NORTH, SOUTH -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser vertical");
          case EAST, WEST -> view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
              "single laser horizontal");
        }
        this.getChildren().add(view);
      }
    }
  }

  private void updatePriorityAntenna() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName()
          .equals("com.roborally.controller.PriorityAntenna")) {
        PriorityAntenna priorityAntenna = (PriorityAntenna) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
            "priority antenna");
        this.getChildren().add(view);
      }
    }
  }

  private void updateStartingPositions() {
    if (LoadBoard.playerStartingPositions[0] == null) {
      return;
    }

    int i = 1;
    for (var pos : LoadBoard.playerStartingPositions) {
      if (space.x == pos[0] && space.y == pos[1]) {
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("start " + i);
        this.getChildren().add(view);
      }
      ++i;
    }
  }

  private void updatePushPanels() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName()
          .equals("com.roborally.controller.PushPanel")) {
        PushPanel pushPanel = (PushPanel) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame(
            pushPanel.getType() == Type.PUSH_PANEL_1 ? "push panel 1" : "push panel 2");

        switch (pushPanel.getHeading()) {
          case NORTH -> view.setRotate(0);
          case EAST -> view.setRotate(90);
          case SOUTH -> view.setRotate(180);
          case WEST -> view.setRotate(270);
        }
        this.getChildren().add(view);
      }
    }
  }

  /**
   * @author August Hjortholm
   * @author Lucas Eiruff
   * <p>
   * updates all fields view
   */
  @Override
  public void updateView(Subject subject) {
    if (subject == this.space) {
      this.getChildren().clear();
      this.getChildren().add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("blank"));
      updateWall();
      updatePriorityAntenna();
      updateStartingPositions();
      updateGreenConveyorBelt();
      updateBlueConveyorBelt();
      updateGears();
      updatePits();
      updateCheckPoints();
      updateSingleLaserNonOrigin();
      updateSingleLasers();
      updatePushPanels();
      updatePlayer();
    }
  }
}
