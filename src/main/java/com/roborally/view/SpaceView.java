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

import com.roborally.controller.*;
import com.roborally.model.SpriteSheetSingleton;
import designpatterns.observer.Subject;
import com.roborally.model.Player;
import com.roborally.model.Space;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import org.intellij.lang.annotations.JdkConstants.InputEventMask;
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

    /*
    if ((space.x + space.y) % 2 == 0) {
      this.setStyle("-fx-background-color: white;");
    } else {
      this.setStyle("-fx-background-color: black;");
    }
     */
    space.attach(this);
    SpriteSheetSingleton.getInstance().spriteSheet.setSpaceSize(this.getPrefWidth(), this.getPrefHeight());
    update(space);
  }

  private void updatePlayer() {
    Player player = space.getPlayer();
    if (player != null) {
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
    }
  }

  // TODO: When multiple walls are drawn on the same space, use the custom textures
  private void updateWall() {
      if (space.getWalls().isEmpty()) {
          return;
      }

    for (var wallHeading : space.getWalls()) {
      switch (wallHeading) {
        case NORTH -> this.getChildren().add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("wall"));
        case EAST -> {
          ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("wall");
          view.setRotate(90.0);
          view.toFront();
          this.getChildren().add(view);
        }
        case SOUTH -> {
          ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("wall");
          view.setRotate(180.0);
          view.toFront();
          this.getChildren().add(view);
        }
        case WEST -> {
          ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("wall");
          view.setRotate(270.0);
          view.toFront();
          this.getChildren().add(view);
        }
      }
    }
  }

  // Todo: update to use custom textures
  private void updateGreenConveyorBelt() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.GreenConveyorBelt")) {
        GreenConveyorBelt conveyorBelt = (GreenConveyorBelt) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("green conveyor belt");
        switch (conveyorBelt.getHeading()) {
          case NORTH -> view.setRotate(0.0);
          case EAST -> view.setRotate(90.0);
          case SOUTH -> view.setRotate(180.0);
          case WEST -> view.setRotate(270.0);
        }
        this.getChildren().add(view);
      }
    }
  }

  private void updateBlueConveyorBelt() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.BlueConveyorBelt")) {
        BlueConveyorBelt conveyorBelt = (BlueConveyorBelt) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("blue conveyor belt");
        switch (conveyorBelt.getHeading()) {
          case NORTH -> view.setRotate(0.0);
          case EAST -> view.setRotate(90.0);
          case SOUTH -> view.setRotate(180.0);
          case WEST -> view.setRotate(270.0);
        }
        this.getChildren().add(view);
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
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("checkpoint " + checkPoint.getCheckpointNum());
        view.toFront();
        this.getChildren().add(view);
      }
    }
  }

  private void updateSingleLasers() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.SingleBoardLaser")) {
        SingleBoardLaser singleBoardLaser = (SingleBoardLaser) fieldAction;
        ImageView view = SpriteSheetSingleton.getInstance().spriteSheet.getFrame("single laser wall");
        switch (singleBoardLaser.getHeading()) {
          case NORTH -> view.setRotate(0.0);
          case EAST -> view.setRotate(90.0);
          case SOUTH -> view.setRotate(180.0);
          case WEST -> view.setRotate(270.0);
        }
        this.getChildren().add(view);
      }
    }
  }

  @Override
  public void updateView(Subject subject) {
    if (subject == this.space) {
      this.getChildren().clear();
      this.getChildren().add(SpriteSheetSingleton.getInstance().spriteSheet.getFrame("blank"));
      updateWall();
      updateGreenConveyorBelt();
      updateBlueConveyorBelt();
      updateGears();
      updatePits();
      updateCheckPoints();
      updateSingleLasers();
      updatePlayer();
    }
  }

}
