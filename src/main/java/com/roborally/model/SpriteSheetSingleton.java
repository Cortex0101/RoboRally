package com.roborally.model;

public class SpriteSheetSingleton {
  private static SpriteSheetSingleton instance = null;

  public SpriteSheet spriteSheet;

  private SpriteSheetSingleton() {
    spriteSheet = new SpriteSheet( System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\images\\TileMap.png", 8, 17);
  }

  public static SpriteSheetSingleton getInstance() {
    if (instance == null) {
      instance = new SpriteSheetSingleton();
    }

    return instance;
  }
}
