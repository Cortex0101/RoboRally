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

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {
    private static final String BOARDSFOLDER = System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(BOARDSFOLDER + "\\" + boardname + "." + JSON_EXT);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return new Board(8,8); // Returns a default 8x8 board - do something else in the future
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
            for (SpaceTemplate spaceTemplate: template.spaces) {
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
                        Player player = new Player(result, spaceTemplate.player.color, spaceTemplate.player.name, space);
                        player.setHeading(spaceTemplate.player.heading);
                        player.setIsAI(spaceTemplate.player.AI);
                        space.setPlayer(player);
                        if (spaceTemplate.player.commandCards != null) {
                            for (int i = 0; i < spaceTemplate.player.commandCards.size(); i++) {
                                if (spaceTemplate.player.commandCards.get(i) != null) {
                                    player.getCardField(i).setCard(new CommandCard(spaceTemplate.player.commandCards.get(i)));
                                    result.resetRegisters = false;
                                } else
                                    player.getCardField(i).setCard(null);
                            }
                        }
                        if (spaceTemplate.player.commandCardsInRegisters != null) {
                            for (int i = 0; i < spaceTemplate.player.commandCardsInRegisters.size(); i++) {
                                if (spaceTemplate.player.commandCardsInRegisters.get(i) != null)
                                    player.getProgramField(i).setCard(new CommandCard(spaceTemplate.player.commandCardsInRegisters.get(i)));
                                else
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
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {}
            }
        }
        return null;
    }

    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate();
        template.width = board.width;
        template.height = board.height;

        for (int i=0; i<board.width; i++) {
            for (int j=0; j<board.height; j++) {
                Space space = board.getSpace(i,j);
                SpaceTemplate spaceTemplate = new SpaceTemplate();
                boolean anythingOnSpace = false;
                if (!space.getWalls().isEmpty() || !space.getActions().isEmpty() || space.getPlayer() != null) {
                    spaceTemplate.x = space.x;
                    spaceTemplate.y = space.y;
                    spaceTemplate.actions.addAll(space.getActions());
                    spaceTemplate.walls.addAll(space.getWalls());
                    if (space.getPlayer() != null) {
                        spaceTemplate.player = new PlayerTemplate();
                        spaceTemplate.player.name = space.getPlayer().getName();
                        spaceTemplate.player.color = space.getPlayer().getColor();
                        spaceTemplate.player.heading = space.getPlayer().getHeading();
                        spaceTemplate.player.AI = space.getPlayer().getIsAI();

                        for (int k = 0; k < 8; k++) {
                            CommandCard card = space.getPlayer().getCardField(k).getCard();
                            if (card != null)
                                spaceTemplate.player.commandCards.add(card.command);
                            else
                                spaceTemplate.player.commandCards.add(null);
                        }
                        for (int l = 0; l < 5; l++) {
                            CommandCard card = space.getPlayer().getProgramField(l).getCard();
                            if (card != null)
                                spaceTemplate.player.commandCardsInRegisters.add(card.command);
                            else
                                spaceTemplate.player.commandCardsInRegisters.add(null);
                        }
                    }
                    template.spaces.add(spaceTemplate);
                }
            }
        }

        //ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        //String filename =
        //        classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;

       //String filename = LoadBoard.class.getClassLoader().getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;
        String filename = BOARDSFOLDER + "\\" + name + "." + JSON_EXT;

        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
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
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }
    }

}
