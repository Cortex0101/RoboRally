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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * loads the board from a .json file
 */
public class LoadBoard {
  private static final String RESOURCE_FOLDER_PATH = System.getProperty("user.dir") + "\\src\\main\\resources";
  private static final String SAVED_BOARDS_PATH = RESOURCE_FOLDER_PATH + "\\com\\roborally\\boards";
  private static final String DEFAULT_BOARDS_PATH = RESOURCE_FOLDER_PATH + "\\com\\roborally\\defaultBoards";
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

  /**
   * @author Lucas Eiruff
   *
   * loads a default board. called when no board has been chosen as an alternative
   *
   * @param boardConfig the settings of the board, like the player amount
   * @param defaultBoard the default board used
   * @return the board which was loaded from the default board file
   */
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
      inputStream = new FileInputStream(DEFAULT_BOARDS_PATH + "\\" + boardname + "." + JSON_EXT);
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

  public static String getBoardContent() {
    return getBoardContent("tempSave");
  }

  public static String getBoardContent(String boardgames) {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(SAVED_BOARDS_PATH + "\\" + boardgames + "." + JSON_EXT);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  /**
   * @author Lucas Eiruff
   *
   * generates a board from a string, which has the format of the .json files
   *
   * @param json the information of the .json file
   * @return the board which was generated from the .json string
   */
  public static Board loadBoardFromJson(String json) {
    BoardTemplate template = loadBoardTemplate(json);
    Board result = new Board(template.width, template.height, "temp");

    for (SpaceTemplate spaceTemplate : template.spaces) {
      Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
      copyWallsAndFieldActions(spaceTemplate, space);

      if (spaceTemplate.player == null)
        continue;

      Player player = createPlayerFromTemplate(space, spaceTemplate.player);
      setCardFields(spaceTemplate.player.commandCards, player, result);
      setCommandFields(spaceTemplate.player.commandCardsInRegisters, player);
      result.addPlayer(player);
    }
    return result;
  }

  /**
   * @author Lucas Eiruff
   *
   * loads a .json file, then convert it into a playable board
   *
   * We dont check if the board name is valid, as this has been done before calling this method.
   *
   * @param boardname the name of the file
   * @return the board which was generated from the .json file
   */
  public static Board loadBoardFromFile(String boardname) {
    BoardTemplate template = loadBoardTemplate(getBoardAsInputStream(boardname));

    Board result = new Board(template.width, template.height, boardname);

    for (SpaceTemplate spaceTemplate : template.spaces) {
      Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
      copyWallsAndFieldActions(spaceTemplate, space);

      if (spaceTemplate.player == null)
        continue;

      Player player = createPlayerFromTemplate(space, spaceTemplate.player);
      setCardFields(spaceTemplate.player.commandCards, player, result);
      setCommandFields(spaceTemplate.player.commandCardsInRegisters, player);
      result.addPlayer(player);
    }
    return result;
  }

  private static void setCardFields(List<Command> commands, Player player, Board board) {
    for (int i = 0; i < commands.size(); i++) {
      if (commands.get(i) == null) {
        player.getCardField(i).setCard(null);
        continue;
      }

      player.getCardField(i).setCard(new CommandCard(commands.get(i)));
      board.resetRegisters = false; // don't reset registers if the robot has been loaded with a program.
    }
  }

  private static void setCommandFields(List<Command> commands, Player player) {
    for (int i = 0; i < commands.size(); i++) {
      if (commands.get(i) == null) {
        player.getProgramField(i).setCard(null);
        continue;
      }

      player.getProgramField(i).setCard(new CommandCard(commands.get(i)));
    }
  }

  private static InputStreamReader getBoardAsInputStream(String boardname) {
    try {
      InputStream inputStream = new FileInputStream(SAVED_BOARDS_PATH + "\\" + boardname + "." + JSON_EXT);
      return new InputStreamReader(Objects.requireNonNull(inputStream));
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return null;
    }
  }

  private static BoardTemplate loadBoardTemplate(InputStreamReader inputStreamReader) {
    GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
    Gson gson = simpleBuilder.create();
    JsonReader reader = gson.newJsonReader(Objects.requireNonNull(inputStreamReader));
    return gson.fromJson(reader, BoardTemplate.class);
  }

  private static BoardTemplate loadBoardTemplate(String json) {
    GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
    Gson gson = simpleBuilder.create();
    return gson.fromJson(json, BoardTemplate.class);
  }

  private static void copyWallsAndFieldActions(SpaceTemplate from, Space to) {
    to.getActions().addAll(from.actions);
    to.getWalls().addAll(from.walls);
    for (FieldAction fieldAction : to.getActions()) {
      if (fieldAction.getClass().getName().equals("com.roborally.controller.CheckPoint")) {
        to.board.addCheckPoint(to);
      }
    }
  }

  private static Player createPlayerFromTemplate(Space startingSpace, PlayerTemplate playerTemplate) {
    Player player = new Player(startingSpace.board, playerTemplate.color, playerTemplate.name, startingSpace);
    player.setHeading(playerTemplate.heading);
    player.setIsAI(playerTemplate.AI);
    startingSpace.setPlayer(player);
    return player;
  }

  /**
   * @author Lucas Eiruff
   *
   * Saves the game state in a .json file
   *
   * @param board The board which is to be stored in the file
   * @param name The name of the file
   */
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
            spaceTemplate.player.AI = space.getPlayer().isAI();

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

    String filename = SAVED_BOARDS_PATH + "\\" + name + "." + JSON_EXT;

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
