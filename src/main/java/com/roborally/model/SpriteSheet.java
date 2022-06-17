package com.roborally.model;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

public class SpriteSheet {
  private final Image spriteSheet;
  private final int rows;
  private final int cols;
  private final int frameWidth;
  private final int frameHeight;
  private double spaceWidth = 0.0;
  private double spaceHeight = 0.0;
  private final Rectangle2D[][] frames;
  private final Map<String, Pair<Integer, Integer>> frameMap = new HashMap<>();

  /**
   * @author Lucas Eiruff
   * @author August Hjortholm
   *
   * Separates the spritesheet into smaller frames the size of a field, for use in generating the board view
   *
   * @param URL The name of the spritesheet
   * @param numRows The number of rows in the spritesheet
   * @param numCols The number of columns in the spritesheet
   */
  public SpriteSheet(final String URL, final int numRows, final int numCols) {
    this.spriteSheet = new Image(URL);
    this.rows = numRows;
    this.cols = numCols;
    this.frameWidth = (int) (spriteSheet.getWidth() / rows);
    this.frameHeight = (int) (spriteSheet.getHeight() / cols);
    this.frames = new Rectangle2D[rows][cols];
    setFrames();
    setFrameMap();
  }

  public void setSpaceSize(double width, double height) {
    this.spaceWidth = width;
    this.spaceHeight = height;
  }

  private void setFrames() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        frames[i][j] = new Rectangle2D(frameWidth * i, frameHeight * j, frameWidth, frameHeight);
      }
    }
  }

  private void setFrameMap() {
    frameMap.put("2-4 pushpanel", new Pair<>(0, 0));
    frameMap.put("1-3-5 pushpanel", new Pair<>(0, 1));
    frameMap.put("blank", new Pair<>(4, 0));
    frameMap.put("pit", new Pair<>(5, 0));
    frameMap.put("green conveyor belt north", new Pair<>(0, 6));
    frameMap.put("green conveyor belt south", new Pair<>(1, 6));
    frameMap.put("green conveyor belt west", new Pair<>(2, 6));
    frameMap.put("green conveyor belt east", new Pair<>(3, 6));
    frameMap.put("green conveyor belt right turn", new Pair<>(2, 4));
    frameMap.put("green conveyor belt left turn", new Pair<>(1, 4));
    frameMap.put("blue conveyor belt north", new Pair<>(4, 1));
    frameMap.put("blue conveyor belt south", new Pair<>(4, 2));
    frameMap.put("blue conveyor belt east", new Pair<>(5, 1));
    frameMap.put("blue conveyor belt west", new Pair<>(5, 2));
    frameMap.put("blue conveyor belt right turn", new Pair<>(2, 2));
    frameMap.put("blue conveyor belt left turn", new Pair<>(1, 2));
    frameMap.put("checkpoint 1", new Pair<>(0, 15));
    frameMap.put("checkpoint 2", new Pair<>(1, 15));
    frameMap.put("checkpoint 3", new Pair<>(2, 15));
    frameMap.put("checkpoint 4", new Pair<>(3, 15));
    frameMap.put("checkpoint 5", new Pair<>(0, 16));
    frameMap.put("checkpoint 6", new Pair<>(1, 16));
    frameMap.put("checkpoint 7", new Pair<>(2, 16));
    frameMap.put("checkpoint 8", new Pair<>(3, 16));
    frameMap.put("south wall", new Pair<>(4, 3));
    frameMap.put("west wall", new Pair<>(5, 3));
    frameMap.put("north wall", new Pair<>(6, 3));
    frameMap.put("east wall", new Pair<>(6, 2));
    frameMap.put("northwest wall", new Pair<>(7, 2));
    frameMap.put("southeast wall", new Pair<>(7, 0));
    frameMap.put("southwest wall", new Pair<>(7, 3));
    frameMap.put("northeast wall", new Pair<>(7, 1));
    frameMap.put("left gear", new Pair<>(4, 6));
    frameMap.put("right gear", new Pair<>(5, 6));
    frameMap.put("single laser wall north", new Pair<>(4, 5));
    frameMap.put("single laser wall south", new Pair<>(4, 4));
    frameMap.put("single laser wall east", new Pair<>(5, 5));
    frameMap.put("single laser wall west", new Pair<>(5, 4));
    frameMap.put("single laser horizontal", new Pair<>(6, 4));
    frameMap.put("single laser vertical", new Pair<>(6, 5));
    frameMap.put("single laser cross", new Pair<>(7, 5));
  }

  public ImageView getFrame(int row, int col) {
    ImageView view = new ImageView(spriteSheet);
    view.setViewport(frames[row][col]);
    view.setFitWidth(spaceWidth);
    view.setFitHeight(spaceHeight);
    return view;
  }

  public ImageView getFrame(String str) {
    Pair<Integer, Integer> frame = frameMap.getOrDefault(str, new Pair<>(4, 0));
    return getFrame(frame.getKey(), frame.getValue()); // returns blank by default
  }
}
