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
import designpatterns.observer.Subject;
import com.roborally.model.Player;
import com.roborally.model.Space;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
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

    // XXX the following styling should better be done with styles
    this.setPrefWidth(SPACE_WIDTH);
    this.setMinWidth(SPACE_WIDTH);
    this.setMaxWidth(SPACE_WIDTH);

    this.setPrefHeight(SPACE_HEIGHT);
    this.setMinHeight(SPACE_HEIGHT);
    this.setMaxHeight(SPACE_HEIGHT);

    if ((space.x + space.y) % 2 == 0) {
      this.setStyle("-fx-background-color: white;");
    } else {
      this.setStyle("-fx-background-color: black;");
    }

    // updatePlayer();

    // This space view should listen to changes of the space
    space.attach(this);
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

  private void updateWall() {
      if (space.getWalls().isEmpty()) {
          return;
      }

    for (var wallHeading : space.getWalls()) {
      Pane pane = new Pane();
      Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
      rectangle.setFill(Color.TRANSPARENT);
      pane.getChildren().add(rectangle);

      Line line = null;
      switch (wallHeading) {
        case NORTH -> line = new Line(2, 2, SPACE_WIDTH - 2, 2);
        case EAST -> line = new Line(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
        case SOUTH -> line = new Line(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
        case WEST -> line = new Line(2, 2, 2, SPACE_HEIGHT - 2);
      }
      line.setStroke(WALL_COLOR);
      line.setStrokeWidth(WALL_THICKNESS);
      pane.getChildren().add(line);
      this.getChildren().add(pane);
    }
  }


  private void updateGreenConveyorBelt() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.GreenConveyorBelt")) {
        GreenConveyorBelt conveyorBelt = (GreenConveyorBelt) fieldAction;

        Pane pane = new Pane();

        Polygon arrow = new Polygon(SPACE_WIDTH / 2.0, 10,
            SPACE_WIDTH - 10, 20,
            SPACE_WIDTH - 20, 20,
            SPACE_WIDTH - 20, SPACE_HEIGHT - 10,
            20, SPACE_HEIGHT - 10,
            20, 20,
            10, 20);

        arrow.setFill(GREEN_CONVEYOR_BELT_COLOR);

        arrow.setRotate(((90 * conveyorBelt.getHeading().ordinal()) % 360) - 180);

        pane.getChildren().add(arrow);
        this.getChildren().add(pane);
      }
    }
  }

  private void updateBlueConveyorBelt() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.BlueConveyorBelt")) {
        BlueConveyorBelt conveyorBelt = (BlueConveyorBelt) fieldAction;

        Pane pane = new Pane();

        Polygon arrow1 = new Polygon(SPACE_WIDTH / 2.0, 10,
            SPACE_WIDTH - 10, 20,
            SPACE_WIDTH - 20, 20,
            SPACE_WIDTH - 20, SPACE_HEIGHT / 2.0,
            20, SPACE_HEIGHT / 2.0,
            20, 20,
            10, 20);

        Polygon arrow2 = new Polygon(SPACE_WIDTH / 2.0, SPACE_HEIGHT / 2.0,
            SPACE_WIDTH - 10, (SPACE_HEIGHT / 2.0) + 20,
            SPACE_WIDTH - 20, (SPACE_HEIGHT / 2.0) + 20,
            SPACE_WIDTH - 20, SPACE_HEIGHT - 10,
            20, SPACE_HEIGHT - 10,
            20, (SPACE_HEIGHT / 2.0) + 20,
            10, (SPACE_HEIGHT / 2.0) + 20);

        arrow1.setFill(BLUE_CONVEYOR_BELT_COLOR);
        arrow2.setFill(BLUE_CONVEYOR_BELT_COLOR);

        pane.setRotate(((90 * conveyorBelt.getHeading().ordinal()) % 360) - 180);
        pane.getChildren().addAll(arrow1, arrow2);
        this.getChildren().add(pane);
      }
    }
  }

  /**
   * From stack overflow:
   */
  private Path drawSemiRing(double centerX, double centerY, double radius, double innerRadius,
      Color bgColor, Color strkColor) {
    Path path = new Path();
    path.setFill(bgColor);
    path.setStroke(strkColor);
    path.setFillRule(FillRule.EVEN_ODD);

    MoveTo moveTo = new MoveTo();
    moveTo.setX(centerX + innerRadius);
    moveTo.setY(centerY);

    ArcTo arcToInner = new ArcTo();
    arcToInner.setX(centerX - innerRadius);
    arcToInner.setY(centerY);
    arcToInner.setRadiusX(innerRadius);
    arcToInner.setRadiusY(innerRadius);

    MoveTo moveTo2 = new MoveTo();
    moveTo2.setX(centerX + innerRadius);
    moveTo2.setY(centerY);

    HLineTo hLineToRightLeg = new HLineTo();
    hLineToRightLeg.setX(centerX + radius);

    ArcTo arcTo = new ArcTo();
    arcTo.setX(centerX - radius);
    arcTo.setY(centerY);
    arcTo.setRadiusX(radius);
    arcTo.setRadiusY(radius);

    HLineTo hLineToLeftLeg = new HLineTo();
    hLineToLeftLeg.setX(centerX - innerRadius);

    path.getElements().add(moveTo);
    path.getElements().add(arcToInner);
    path.getElements().add(moveTo2);
    path.getElements().add(hLineToRightLeg);
    path.getElements().add(arcTo);
    path.getElements().add(hLineToLeftLeg);

    return path;
  }

  private void updateGears() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.Gear")) {
        Gear gear = (Gear) fieldAction;

        Pane pane = new Pane();
        pane.getChildren().add(
            drawSemiRing(SPACE_WIDTH / 2.0, SPACE_HEIGHT / 2.0, SPACE_WIDTH / 2.0,
                SPACE_WIDTH / 4.0, Color.ORANGERED, Color.DARKRED));
        Polygon triangle = new Polygon(0.0, SPACE_HEIGHT / 2.0,
            SPACE_WIDTH / 4.0 /* inner radius */, SPACE_HEIGHT / 2.0,
            ((SPACE_WIDTH / 2.0) - (SPACE_WIDTH / 4.0)) / 2.0, (SPACE_HEIGHT / 2.0) + 20);
        triangle.setFill(Color.ORANGERED);
        pane.getChildren().add(triangle);
          if (gear.getDirection().equals(Gear.Direction.RIGHT)) {
              pane.setScaleY(-1);
          }
        this.getChildren().add(pane);
      }
    }
  }

  private void updatePits() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.Pit")) {
        Pit pit = (Pit) fieldAction;

        Pane pane = new Pane();

        final double spacing = 4.0;
        Polygon square = new Polygon(
            spacing, spacing,
            SPACE_WIDTH - spacing, spacing,
            SPACE_WIDTH - spacing, SPACE_HEIGHT - spacing,
            spacing, SPACE_HEIGHT - spacing);
        square.setFill(Color.LIGHTGRAY);
        pane.getChildren().add(square);
        this.getChildren().add(pane);
      }
    }
  }

  /**
   * Taken from https://stackoverflow.com/questions/41746511/how-to-put-some-text-right-center-of-a-circle-that-have-been-drawn-on-javafx-can
   */
  private WritableImage createCircledNumber(int number) {
    //createCircledNumber() method always returns 26px X 26px sized image
    StackPane sPane = new StackPane();
    sPane.setPrefSize(26, 26);

    Circle c = new Circle(26 / 2.0);
    c.setStroke(Color.BLACK);
    c.setFill(Color.WHITE);
    c.setStrokeWidth(3);
    sPane.getChildren().add(c);

    Text txtNum = new Text(number + "");
    sPane.getChildren().add(txtNum);
    SnapshotParameters parameters = new SnapshotParameters();
    parameters.setFill(Color.TRANSPARENT);
    return sPane.snapshot(parameters, null);
  }

  private void updateCheckPoints() {
    for (FieldAction fieldAction : space.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.CheckPoint")) {
        CheckPoint checkPoint = (CheckPoint) fieldAction;

        Pane pane = new Pane();

        final double spacing = 4.0;
        Polygon square = new Polygon(
            spacing, spacing,
            SPACE_WIDTH - spacing, spacing,
            SPACE_WIDTH - spacing, SPACE_HEIGHT - spacing,
            spacing, SPACE_HEIGHT - spacing);
        square.setFill(Color.LIGHTGREEN);
        pane.getChildren().add(square);

        Canvas cvs = new Canvas();
        cvs.setWidth(SPACE_WIDTH);
        cvs.setHeight(SPACE_HEIGHT);
        cvs.setLayoutX(0);
        cvs.setLayoutY(0);
        pane.getChildren().add(cvs);

        GraphicsContext gc = cvs.getGraphicsContext2D();
        double x = (cvs.getWidth() - 26) / 2;
        double y = (cvs.getHeight() - 26) / 2;
        gc.drawImage(createCircledNumber(checkPoint.getCheckpointNum()), x, y);

        this.getChildren().add(pane);
      }
    }
  }

  @Override
  public void updateView(Subject subject) {
    if (subject == this.space) {
      this.getChildren().clear();
      updateWall();
      updateGreenConveyorBelt();
      updateBlueConveyorBelt();
      updateGears();
      updatePits();
      updateCheckPoints();
      updatePlayer();
    }
  }

}
