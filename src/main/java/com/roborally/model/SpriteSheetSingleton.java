package com.roborally.model;

public class SpriteSheetSingleton {

  private static SpriteSheetSingleton instance = null;

  public final SpriteSheet spriteSheet;

  private SpriteSheetSingleton() {
    spriteSheet = new SpriteSheet( "file:" + System.getProperty("user.home") + "/IdeaProjects/RoboRally/src/main/resources/com/roborally/images/TileMap.png", 8, 17);
  }

  /**
   * @return The spritesheet to be
   * @author Lucas Eiruff
   * <p>
   * Generates a single spritesheet from the spritesheet file
   */
  public static SpriteSheetSingleton getInstance() {
    if (instance == null) {
      instance = new SpriteSheetSingleton();
    }

    return instance;
  }
}
