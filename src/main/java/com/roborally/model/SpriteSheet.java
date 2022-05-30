package com.roborally.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteSheet {
  private final Image spriteSheet;
  private final int rows;
  private final int cols;
  private final int frameWidth;
  private final int frameHeight;
  private Rectangle2D[][] frames;

  public SpriteSheet(final String URL, final int numRows, final int numCols) {
    this.spriteSheet = new Image(URL);
    this.rows = numRows;
    this.cols = numCols;
    this.frameWidth = (int) (spriteSheet.getWidth() / rows);
    this.frameHeight = (int) (spriteSheet.getHeight() / cols);
    this.frames = new Rectangle2D[rows][cols];
    setFrames();
  }

  private void setFrames() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        frames[i][j] = new Rectangle2D(frameWidth * i, frameHeight * j, frameWidth, frameHeight);
      }
    }
  }

  public ImageView getFrame(int row, int col) {
    ImageView view = new ImageView(spriteSheet);
    view.setViewport(frames[row][col]);
    return view;
  }

  public ImageView getFrame(int row, int col, int width, int height) {
    ImageView view = getFrame(row, col);
    view.setFitWidth(width);
    view.setFitHeight(height);
    return view;
  }
}
