package com.roborally.model;

public class SpriteSheetSingleton {
  private static SpriteSheetSingleton instance = null;

  public SpriteSheet spriteSheet;

  private SpriteSheetSingleton() {
    spriteSheet = new SpriteSheet( System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\images\\TileMap.png", 8, 17);
  }

  /**
   * @author Lucas Eiruff
   *
   * Generates a single spritesheet from the spritesheet file
   *
   * @return The spritesheet to be
   */
  public static SpriteSheetSingleton getInstance() {
    if (instance == null) {
      instance = new SpriteSheetSingleton();
    }

    return instance;
  }
}
