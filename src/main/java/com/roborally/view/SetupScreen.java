package com.roborally.view;

import com.roborally.RoboRally;
import com.roborally.controller.GameController;
import com.roborally.fileaccess.LoadBoard;
import com.roborally.fileaccess.LoadBoard.BoardConfig;
import com.roborally.fileaccess.LoadBoard.DefaultBoard;
import com.roborally.model.Board;
import com.roborally.server.Client;
import com.roborally.server.Server;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class SetupScreen {

  //final String IMAGES_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\images\\";
  final String IMAGES_PATH = "file:" + System.getProperty("user.home") + "/IdeaProjects/RoboRally/src/main/resources/com/roborally/images/";

  final HBox twoColumn = new HBox();

  final VBox leftColumn = new VBox();
  final Label playersLabel = new Label("Players");
  final Slider playerSlider = new Slider(2, 6, 4);
  final Separator leftSliderSeparator = new Separator();
  final Label namesLabel = new Label("Names");
  final VBox namesVBox = new VBox();
  final HBox name1HBox = new HBox();
  final ColorPicker name1ColorPicker = new ColorPicker();
  final TextField name1TextField = new TextField();
  final HBox name2HBox = new HBox();
  final ColorPicker name2ColorPicker = new ColorPicker();
  final TextField name2TextField = new TextField();
  final HBox name3HBox = new HBox();
  final ColorPicker name3ColorPicker = new ColorPicker();
  final TextField name3TextField = new TextField();
  final HBox name4HBox = new HBox();
  final ColorPicker name4ColorPicker = new ColorPicker();
  final TextField name4TextField = new TextField();
  final HBox name5HBox = new HBox();
  final ColorPicker name5ColorPicker = new ColorPicker();
  final TextField name5TextField = new TextField();
  final HBox name6HBox = new HBox();
  final ColorPicker name6ColorPicker = new ColorPicker();
  final TextField name6TextField = new TextField();
  final Separator nameSeparator = new Separator();
  final Button backButton = new Button("Back");

  final Separator twoColumnSeparator = new Separator(Orientation.VERTICAL);

  final VBox rightColumn = new VBox();
  final Label AILabel = new Label("AI");
  final Slider AISlider = new Slider(0, 5, 3);
  final Separator rightSliderSeparator = new Separator();
  final Label levelLabel = new Label("Level");
  final VBox levelVBox = new VBox();
  final HBox level1HBox = new HBox();
  final RadioButton level1Button = new RadioButton("Easy");
  final ImageView level1ImageView = new ImageView();
  final Image level1Image = new Image(IMAGES_PATH + "riskyCrossing.png");
  final HBox level2HBox = new HBox();
  final RadioButton level2Button = new RadioButton("Medium");
  final ImageView level2ImageView = new ImageView();
  final Image level2Image = new Image(IMAGES_PATH + "passingLane.png");
  final HBox level3HBox = new HBox();
  final RadioButton level3Button = new RadioButton("Hard");
  final ImageView level3ImageView = new ImageView();
  final Image level3Image = new Image(IMAGES_PATH + "heavyArea.png");
  final Separator levelSeparator = new Separator();
  final Button playButton = new Button("Play");

  final ToggleGroup toggleGroup = new ToggleGroup();


  final RadioButton singleplayerButton = new RadioButton("Single");
  final RadioButton hostButton = new RadioButton("Host");
  final RadioButton clientButton = new RadioButton("Client");
  final ToggleGroup onlineGroup = new ToggleGroup();
  final HBox onlineHBox = new HBox();

  final TextField hostPortField = new TextField();

  final List<Pair<ColorPicker, TextField>> playerMenuItems;

  boolean isHost = false;
  boolean isClient = false;

  final Scene scene;

  final RoboRally roboRally;

  private void disableAll() {
    playerSlider.setDisable(true);

    name1ColorPicker.setDisable(true);
    name1TextField.setDisable(true);

    name2ColorPicker.setDisable(true);
    name2TextField.setDisable(true);

    name3ColorPicker.setDisable(true);
    name3TextField.setDisable(true);

    name4ColorPicker.setDisable(true);
    name4TextField.setDisable(true);

    name5ColorPicker.setDisable(true);
    name5TextField.setDisable(true);

    name6ColorPicker.setDisable(true);
    name6TextField.setDisable(true);

    AISlider.setDisable(true);

    level1Button.setDisable(true);
    level2Button.setDisable(true);
    level3Button.setDisable(true);
  }

  private void enableAll() {
    playerSlider.setDisable(false);

    if (playerSlider.getValue() >= 1) {
      name1ColorPicker.setDisable(false);
      name1TextField.setDisable(false);
    }

    if (playerSlider.getValue() >= 2) {
      name2ColorPicker.setDisable(false);
      name2TextField.setDisable(false);
    }

    if (playerSlider.getValue() >= 3) {
      name3ColorPicker.setDisable(false);
      name3TextField.setDisable(false);
    }

    if (playerSlider.getValue() >= 4) {
      name4ColorPicker.setDisable(false);
      name4TextField.setDisable(false);
    }

    if (playerSlider.getValue() >= 5) {
      name5ColorPicker.setDisable(false);
      name5TextField.setDisable(false);
    }

    if (playerSlider.getValue() >= 6) {
      name6ColorPicker.setDisable(false);
      name6TextField.setDisable(false);
    }

    AISlider.setDisable(false);

    level1Button.setDisable(false);
    level2Button.setDisable(false);
    level3Button.setDisable(false);
  }

  public SetupScreen(RoboRally roboRally) {
    this.roboRally = roboRally;

    hostPortField.setPromptText("10.209.241.76");

    singleplayerButton.fire();

    singleplayerButton.setOnMousePressed(e -> {
      hostPortField.setDisable(true);
      isHost = false;
      isClient = false;
      enableAll();
    });

    hostButton.setOnMousePressed(e -> {
      hostPortField.setDisable(true);
      isHost = true;
      isClient = false;
      enableAll();
    });

    clientButton.setOnMousePressed(e -> {
      hostPortField.setDisable(false);
      isHost = false;
      isClient = true;
      disableAll();
    });

    twoColumn.setPrefSize(400, 360);
    twoColumn.setAlignment(Pos.TOP_CENTER);

    leftColumn.setPrefSize(200, 360);
    leftColumn.setAlignment(Pos.TOP_CENTER);
    playerSlider.setBlockIncrement(1.0);
    playerSlider.setMajorTickUnit(1.0);
    playerSlider.setMinorTickCount(0);
    playerSlider.setShowTickLabels(true);
    playerSlider.setShowTickMarks(true);
    playerSlider.setSnapToTicks(true);
    leftSliderSeparator.setPrefWidth(200.0);

    namesVBox.setPrefSize(100.0, 200.0);
    name1HBox.setPrefSize(200.0, 100.0);
    name1HBox.setPadding(new Insets(9.0, 0.0, 6.0, 0.0));
    name1TextField.setPromptText("Player1");
    name1ColorPicker.setValue(Color.RED);
    name2HBox.setPrefSize(200.0, 100.0);
    name2HBox.setPadding(new Insets(6.0, 0.0, 6.0, 0.0));
    name2TextField.setPromptText("Player2");
    name2ColorPicker.setValue(Color.GREEN);
    name3HBox.setPrefSize(200.0, 100.0);
    name3HBox.setPadding(new Insets(6.0, 0.0, 6.0, 0.0));
    name3TextField.setPromptText("Player3");
    name3ColorPicker.setValue(Color.BLUE);
    name4HBox.setPrefSize(200.0, 100.0);
    name4HBox.setPadding(new Insets(6.0, 0.0, 6.0, 0.0));
    name4TextField.setPromptText("Player4");
    name4ColorPicker.setValue(Color.ORANGE);
    name5HBox.setPrefSize(200.0, 100.0);
    name5HBox.setPadding(new Insets(6.0, 0.0, 6.0, 0.0));
    name5TextField.setPromptText("Player5");
    name5ColorPicker.setValue(Color.GREY);
    name6HBox.setPrefSize(200.0, 100.0);
    name6HBox.setPadding(new Insets(6.0, 0.0, 6.0, 0.0));
    name6TextField.setPromptText("Player6");
    name6ColorPicker.setValue(Color.WHITE);
    nameSeparator.setPrefWidth(200.0);
    backButton.setPrefSize(360, 140);

    rightColumn.setPrefSize(200, 360);
    rightColumn.setAlignment(Pos.TOP_CENTER);
    AISlider.setBlockIncrement(1.0);
    AISlider.setMajorTickUnit(1.0);
    AISlider.setMinorTickCount(0);
    AISlider.setShowTickLabels(true);
    AISlider.setShowTickMarks(true);
    AISlider.setSnapToTicks(true);
    rightSliderSeparator.setPrefWidth(200.0);
    level1HBox.setAlignment(Pos.TOP_RIGHT);
    level1Button.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));
    level1Button.setSelected(true);
    level1Button.setToggleGroup(toggleGroup);
    level1ImageView.setFitWidth(78.0);
    level1ImageView.setFitHeight(75.0);
    level1ImageView.setPickOnBounds(true);
    level1ImageView.setImage(level1Image);
    level2HBox.setAlignment(Pos.TOP_RIGHT);
    level2Button.setPadding(new Insets(0.0, 38.0, 0.0, 0.0));
    level2Button.setToggleGroup(toggleGroup);
    level2ImageView.setFitWidth(78.0);
    level2ImageView.setFitHeight(75.0);
    level2ImageView.setPickOnBounds(true);
    level2ImageView.setImage(level2Image);
    level3HBox.setAlignment(Pos.TOP_RIGHT);
    level3Button.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));
    level3Button.setToggleGroup(toggleGroup);
    level3ImageView.setFitWidth(78.0);
    level3ImageView.setFitHeight(75.0);
    level3ImageView.setPickOnBounds(true);
    level3ImageView.setImage(level3Image);
    levelSeparator.setPrefWidth(200.0);
    playButton.setPrefSize(360, 140);

    onlineHBox.getChildren().addAll(singleplayerButton, hostButton, clientButton);
    onlineHBox.setMinSize(onlineHBox.getPrefWidth(), 24.0);

    twoColumn.getChildren().addAll(leftColumn, twoColumnSeparator, rightColumn);

    leftColumn.getChildren()
        .addAll(playersLabel, playerSlider, leftSliderSeparator, namesLabel, namesVBox,
            nameSeparator, onlineHBox, backButton);
    namesVBox.getChildren()
        .addAll(name1HBox, name2HBox, name3HBox, name4HBox, name5HBox, name6HBox);
    name1HBox.getChildren().addAll(name1ColorPicker, name1TextField);
    name2HBox.getChildren().addAll(name2ColorPicker, name2TextField);
    name3HBox.getChildren().addAll(name3ColorPicker, name3TextField);
    name4HBox.getChildren().addAll(name4ColorPicker, name4TextField);
    name5HBox.getChildren().addAll(name5ColorPicker, name5TextField);
    name6HBox.getChildren().addAll(name6ColorPicker, name6TextField);

    rightColumn.getChildren()
        .addAll(AILabel, AISlider, rightSliderSeparator, levelLabel, levelVBox, levelSeparator,
            hostPortField, playButton);
    levelVBox.getChildren().addAll(level1HBox, level2HBox, level3HBox);
    level1HBox.getChildren().addAll(level1Button, level1ImageView);
    level2HBox.getChildren().addAll(level2Button, level2ImageView);
    level3HBox.getChildren().addAll(level3Button, level3ImageView);

    scene = new Scene(twoColumn);

    playerMenuItems = List.of(
        new Pair<>(name1ColorPicker, name1TextField),
        new Pair<>(name2ColorPicker, name2TextField),
        new Pair<>(name3ColorPicker, name3TextField),
        new Pair<>(name4ColorPicker, name4TextField),
        new Pair<>(name5ColorPicker, name5TextField),
        new Pair<>(name6ColorPicker, name6TextField));

    setSliderCallbacks();
    updatePlayerMenu((int) playerSlider.getValue());

    singleplayerButton.setToggleGroup(onlineGroup);
    hostButton.setToggleGroup(onlineGroup);
    clientButton.setToggleGroup(onlineGroup);
    singleplayerButton.setMinHeight(hostPortField.getPrefHeight());

    this.getScene().setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.H) {
        roboRally.isHost = true;
        roboRally.isMultiplayer = true;
        roboRally.server = new Server();
        roboRally.server.roboRally = roboRally;
        new Thread(() -> roboRally.server.start(6666)).start();
      }
      if (e.getCode() == KeyCode.C) {
        roboRally.isClient = true;
        roboRally.isMultiplayer = true;
        roboRally.client = new Client();
        roboRally.client.startConnection("127.0.0.1", 6666);
        roboRally.clientNum = Integer.parseInt(roboRally.client.post("GET_CLIENT_NUM"));
      }
    });

    backButton.setOnMouseReleased(mouseEvent -> {
      roboRally.setScene(roboRally.getPrimaryScene());
    });

    playButton.setOnMouseReleased(mouseEvent -> {
      if (isHost) {
        roboRally.isHost = true;
        roboRally.isMultiplayer = true;
        roboRally.server = new Server();
        roboRally.server.roboRally = roboRally;
        new Thread(() -> roboRally.server.start(6666)).start();
      } else if (isClient) {
        roboRally.isClient = true;
        roboRally.isMultiplayer = true;
        roboRally.client = new Client();
        roboRally.client.startConnection(hostPortField.getText(), 6666);
        roboRally.clientNum = Integer.parseInt(roboRally.client.post("GET_CLIENT_NUM"));
      }

      BoardConfig boardConfig = new BoardConfig((int) playerSlider.getValue(),
          (int) AISlider.getValue());
      for (int i = 0; i < playerSlider.getValue(); i++) {
        boardConfig.playerNames[i] =
            playerMenuItems.get(i).getValue().getText().isEmpty() ? playerMenuItems.get(i)
                .getValue()
                .getPromptText() : playerMenuItems.get(i).getValue().getText();
        boardConfig.playerColors[i] = Arrays.asList(name1ColorPicker.getValue().toString(),
            name2ColorPicker.getValue().toString(),
            name3ColorPicker.getValue().toString(),
            name4ColorPicker.getValue().toString(),
            name5ColorPicker.getValue().toString(),
            name6ColorPicker.getValue().toString()).get(i);
      }

      DefaultBoard selectedBoard;
      if (level1Button.isSelected()) {
        selectedBoard = DefaultBoard.easy;
      } else if (level2Button.isSelected()) {
        selectedBoard = DefaultBoard.medium;
      } else if (level3Button.isSelected()) {
        selectedBoard = DefaultBoard.hard;
      } else {
        selectedBoard = DefaultBoard.easy;
      }

      roboRally.setScene(roboRally.getPrimaryScene());

      Board board;
      if (!roboRally.isClient) {
        board = LoadBoard.loadDefaultBoard(boardConfig, selectedBoard);
      } else {
        String jsonBoard = roboRally.client.post("GET_BOARD");
        board = LoadBoard.loadBoardFromJson(jsonBoard);
      }
      roboRally.getAppController()
          .setGameController(new GameController(Objects.requireNonNull(board)));
      roboRally.getAppController().getGameController().roboRally = roboRally;
      roboRally.getAppController().setAIPlayers(true);
      roboRally.getAppController().getGameController().startProgrammingPhase(board.resetRegisters);
      roboRally.createBoardView(roboRally.getAppController().getGameController());
    });
  }

  /**
   * @author Lucas Eiruff
   * <p>
   * Creates a slider to choose amount of players/AI
   */
  private void setSliderCallbacks() {
    // Adjust slider positions if they are put in an invalid state
    playerSlider.setOnMouseReleased(event -> {
      if (AISlider.getValue() >= playerSlider.getValue()) {
        AISlider.setValue(playerSlider.getValue() - 1.0);
      }
      updatePlayerMenu((int) playerSlider.getValue());
    });
    AISlider.setOnMouseReleased(event -> {
      if (AISlider.getValue() >= playerSlider.getValue()) {
        playerSlider.setValue(AISlider.getValue() + 1.0);
      }
      updatePlayerMenu((int) playerSlider.getValue());
    });
  }

  private void updatePlayerMenu(int playerCount) {
    for (int i = 0; i < 6; i++) {
      if (playerCount > i) {
        playerMenuItems.get(i).getKey().setDisable(false);
        playerMenuItems.get(i).getValue().setDisable(false);
      } else {
        playerMenuItems.get(i).getKey().setDisable(true);
        playerMenuItems.get(i).getValue().setDisable(true);
      }
    }

    final int aiBegin = (int) (playerSlider.getValue() - AISlider.getValue());
    int playerNum = 1;
    int aiNum = 1;
    for (int i = 0; i < 6; i++) {
      if (i >= playerCount) {
        playerMenuItems.get(i).getValue().setPromptText("");
      } else {
        if (i < aiBegin) {
          playerMenuItems.get(i).getValue().setPromptText("Player" + playerNum);
          ++playerNum;
        } else {
          playerMenuItems.get(i).getValue().setPromptText("AI" + aiNum);
          ++aiNum;
        }
      }
    }
  }

  public Scene getScene() {
    return this.scene;
  }
}
