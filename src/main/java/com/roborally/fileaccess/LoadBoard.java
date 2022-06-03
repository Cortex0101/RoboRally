package com.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.roborally.fileaccess.model.BoardTemplate;
import com.roborally.fileaccess.model.PlayerTemplate;
import com.roborally.fileaccess.model.SpaceTemplate;
import com.roborally.controller.FieldAction;
import com.roborally.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

  private static final String BOARDSFOLDER =
      System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards";
  private static final String DEFAULTBOARDSFOLDER = System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\defaultBoards";
  private static final String DEFAULTBOARD = "defaultboard";
  private static final String JSON_EXT = "json";

  public static class BoardConfig {
    public int playerNumber;
    public int AINumber;
    public String[] playerNames = new String[playerNumber];
    public String[] playerColors = new String[playerNumber];

    public BoardConfig(int playerNumber, int AINumber) {
      this.playerNumber = playerNumber;
      this.AINumber = AINumber;
      this.playerNames = new String[playerNumber];
      this.playerColors = new String[playerNumber];
    }
  }

  public enum DefaultBoard {
    easy,
    medium,
    hard
  }

  public static Board loadDefaultBoard(BoardConfig boardConfig, DefaultBoard defaultBoard) {
    String boardname = null;
    int[][] playerStartingPositions = new int[6][2];

    switch (defaultBoard) {
      case easy -> {
        boardname = "easy";
        playerStartingPositions[0] = new int[]{1, 0};
        playerStartingPositions[1] = new int[]{0, 2};
        playerStartingPositions[2] = new int[]{1, 4};
        playerStartingPositions[3] = new int[]{1, 5};
        playerStartingPositions[4] = new int[]{0, 7};
        playerStartingPositions[5] = new int[]{1, 9};
      }
      case medium -> {
        boardname = "medium";
        playerStartingPositions[0] = new int[]{1, 0};
        playerStartingPositions[1] = new int[]{0, 2};
        playerStartingPositions[2] = new int[]{1, 4};
        playerStartingPositions[3] = new int[]{1, 5};
        playerStartingPositions[4] = new int[]{0, 7};
        playerStartingPositions[5] = new int[]{1, 9};
      }
      case hard -> {
        boardname = "hard";
        playerStartingPositions[0] = new int[]{11, 1};
        playerStartingPositions[1] = new int[]{12, 3};
        playerStartingPositions[2] = new int[]{11, 4};
        playerStartingPositions[3] = new int[]{11, 5};
        playerStartingPositions[4] = new int[]{12, 6};
        playerStartingPositions[5] = new int[]{11, 8};
      }
    }

    InputStream inputStream;
    try {
      inputStream = new FileInputStream(DEFAULTBOARDSFOLDER + "\\" + boardname + "." + JSON_EXT);
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return new Board(8, 8); // Returns a default 8x8 board - do something else in the future
    }

    GsonBuilder simpleBuilder = new GsonBuilder().
        registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
    Gson gson = simpleBuilder.create();

    Board result;
    JsonReader reader = null;
    try {
      reader = gson.newJsonReader(new InputStreamReader(inputStream));
      BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

      result = new Board(template.width, template.height, boardname);
      for (SpaceTemplate spaceTemplate : template.spaces) {
        Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
        if (space != null) {
          space.getActions().addAll(spaceTemplate.actions);
          space.getWalls().addAll(spaceTemplate.walls);

          for (FieldAction fieldAction : space.getActions()) {
            if (fieldAction.getClass().getName().equals("com.roborally.controller.CheckPoint")) {
              result.addCheckPoint(space);
            }
          }
        }
      }
      for (int i = 0; i < boardConfig.playerNumber; ++i) {
        Player player = new Player(result, boardConfig.playerColors[i], boardConfig.playerNames[i], result.getSpace(playerStartingPositions[i][0], playerStartingPositions[i][1]));
        player.setIsAI(i >= (boardConfig.playerNumber - boardConfig.AINumber));
        result.getSpace(playerStartingPositions[i][0], playerStartingPositions[i][1]).setPlayer(player);
        switch (boardname) {
          case "easy" -> player.setHeading(Heading.EAST);
          case "medium" -> player.setHeading(Heading.EAST);
          case "hard" -> player.setHeading(Heading.WEST);
        }
        result.addPlayer(player);
      }
      saveBoard(result, "tempBoard");
      reader.close();
      return result;
    } catch (IOException e1) {
      try {
        reader.close();
        inputStream = null;
      } catch (IOException ignored) {
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException ignored) {

        }
      }
    }
    return null;
  }

  public static Board loadBoard(String boardname) {
    if (boardname == null) {
      boardname = DEFAULTBOARD;
    }

    InputStream inputStream;
    try {
      inputStream = new FileInputStream(BOARDSFOLDER + "\\" + boardname + "." + JSON_EXT);
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return new Board(8, 8); // Returns a default 8x8 board - do something else in the future
    }

    // In simple cases, we can create a Gson object with new Gson():
    GsonBuilder simpleBuilder = new GsonBuilder().
        registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
    Gson gson = simpleBuilder.create();

    Board result;
    // FileReader fileReader = null;
    JsonReader reader = null;
    try {
      // fileReader = new FileReader(filename);
      reader = gson.newJsonReader(new InputStreamReader(inputStream));
      BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

      result = new Board(template.width, template.height, boardname);
      for (SpaceTemplate spaceTemplate : template.spaces) {
        Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
        if (space != null) {
          space.getActions().addAll(spaceTemplate.actions);
          space.getWalls().addAll(spaceTemplate.walls);

          for (FieldAction fieldAction : space.getActions()) {
            if (fieldAction.getClass().getName().equals("com.roborally.controller.CheckPoint")) {
              result.addCheckPoint(space);
            }
          }

          if (spaceTemplate.player != null) {
            Player player = new Player(result, spaceTemplate.player.color,
                spaceTemplate.player.name, space);
            player.setHeading(spaceTemplate.player.heading);
            player.setIsAI(spaceTemplate.player.AI);
            space.setPlayer(player);
            for (int i = 0; i < spaceTemplate.player.commandCards.size(); i++) {
                if (spaceTemplate.player.commandCards.get(i) != null) {
                    player.getCardField(i)
                            .setCard(new CommandCard(spaceTemplate.player.commandCards.get(i)));
                    result.resetRegisters = false;
                } else {
                    player.getCardField(i).setCard(null);
                }
            }
            for (int i = 0; i < spaceTemplate.player.commandCardsInRegisters.size(); i++) {
                if (spaceTemplate.player.commandCardsInRegisters.get(i) != null) {
                    player.getProgramField(i)
                            .setCard(new CommandCard(
                                    spaceTemplate.player.commandCardsInRegisters.get(i)));
                } else {
                    player.getProgramField(i).setCard(null);
                }
            }
            result.addPlayer(player);
          }
        }
      }
      reader.close();
      return result;
    } catch (IOException e1) {
      try {
        reader.close();
        inputStream = null;
      } catch (IOException ignored) {
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException ignored) {
        }
      }
    }
    return null;
  }

  public static void saveBoard(Board board, String name) {
    BoardTemplate template = new BoardTemplate();
    template.width = board.width;
    template.height = board.height;

    List<SpaceTemplate> playerSpaces = new ArrayList<SpaceTemplate>();

    for (int i = 0; i < board.width; i++) {
      for (int j = 0; j < board.height; j++) {
        Space space = board.getSpace(i, j);
        SpaceTemplate spaceTemplate = new SpaceTemplate();
        boolean anythingOnSpace = false;
        if (!space.getWalls().isEmpty() || !space.getActions().isEmpty()
            || space.getPlayer() != null) {
          spaceTemplate.x = space.x;
          spaceTemplate.y = space.y;
          spaceTemplate.actions.addAll(space.getActions());
          spaceTemplate.walls.addAll(space.getWalls());
          boolean isPlayerSpace = false;
          if (space.getPlayer() != null) {
            isPlayerSpace = true;
            spaceTemplate.player = new PlayerTemplate();
            spaceTemplate.player.name = space.getPlayer().getName();
            spaceTemplate.player.color = space.getPlayer().getColor();
            spaceTemplate.player.heading = space.getPlayer().getHeading();
            spaceTemplate.player.AI = space.getPlayer().getIsAI();

            for (int k = 0; k < 8; k++) {
              CommandCard card = space.getPlayer().getCardField(k).getCard();
                if (card != null) {
                    spaceTemplate.player.commandCards.add(card.command);
                } else {
                    spaceTemplate.player.commandCards.add(null);
                }
            }
            for (int l = 0; l < 5; l++) {
              CommandCard card = space.getPlayer().getProgramField(l).getCard();
                if (card != null) {
                    spaceTemplate.player.commandCardsInRegisters.add(card.command);
                } else {
                    spaceTemplate.player.commandCardsInRegisters.add(null);
                }
            }
          }
          if (!isPlayerSpace) {
            template.spaces.add(spaceTemplate);
          }
          else {
            playerSpaces.add(spaceTemplate);
            isPlayerSpace = false;
          }
        }
      }
    }

    for (int k = 0; k < board.getPlayersNumber(); k++) {
      String playerName = board.getPlayer(k).getName();
      for (SpaceTemplate spaceTemplate1 : playerSpaces) {
        if (spaceTemplate1.player.name.equals(playerName)) {
          template.spaces.add(spaceTemplate1);
          break;
        }
      }
    }

    String filename = BOARDSFOLDER + "\\" + name + "." + JSON_EXT;

    GsonBuilder simpleBuilder = new GsonBuilder().
        registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
        setPrettyPrinting();
    Gson gson = simpleBuilder.create();

    FileWriter fileWriter = null;
    JsonWriter writer = null;
    try {
      fileWriter = new FileWriter(filename);
      writer = gson.newJsonWriter(fileWriter);
      gson.toJson(template, template.getClass(), writer);
      writer.close();
    } catch (IOException e1) {
      if (writer != null) {
        try {
          writer.close();
          fileWriter = null;
        } catch (IOException ignored) {
        }
      }
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

}
