package com.roborally.view;

import java.util.Arrays;
import java.util.List;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class SetupScreen {
  final String IMAGES_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\images\\";

  HBox twoColumn = new HBox();

  VBox leftColumn = new VBox();
  Label playersLabel = new Label("Players");
  Slider playerSlider = new Slider(2, 6, 4);
  Separator leftSliderSeparator = new Separator();
  Label namesLabel = new Label("Names");
  VBox namesVBox = new VBox();
  HBox name1HBox = new HBox();
  ColorPicker name1ColorPicker = new ColorPicker();
  TextField name1TextField = new TextField();
  HBox name2HBox = new HBox();
  ColorPicker name2ColorPicker = new ColorPicker();
  TextField name2TextField = new TextField();
  HBox name3HBox = new HBox();
  ColorPicker name3ColorPicker = new ColorPicker();
  TextField name3TextField = new TextField();
  HBox name4HBox = new HBox();
  ColorPicker name4ColorPicker = new ColorPicker();
  TextField name4TextField = new TextField();
  HBox name5HBox = new HBox();
  ColorPicker name5ColorPicker = new ColorPicker();
  TextField name5TextField = new TextField();
  HBox name6HBox = new HBox();
  ColorPicker name6ColorPicker = new ColorPicker();
  TextField name6TextField = new TextField();
  Separator nameSeparator = new Separator();
  Button backButton = new Button("Back");

  Separator twoColumnSeparator = new Separator(Orientation.VERTICAL);

  VBox rightColumn = new VBox();
  Label AILabel = new Label("AI");
  Slider AISlider = new Slider(0, 5, 3);
  Separator rightSliderSeparator = new Separator();
  Label levelLabel = new Label("Level");
  VBox levelVBox = new VBox();
  HBox level1HBox = new HBox();
  RadioButton level1Button = new RadioButton("Easy");
  ImageView level1ImageView = new ImageView();
  Image level1Image = new Image(IMAGES_PATH + "rallyTestImage.jpg");
  HBox level2HBox = new HBox();
  RadioButton level2Button = new RadioButton("Medium");
  ImageView level2ImageView = new ImageView();
  Image level2Image = new Image(IMAGES_PATH + "rallyTestImage.jpg");
  HBox level3HBox = new HBox();
  RadioButton level3Button = new RadioButton("Hard");
  ImageView level3ImageView = new ImageView();
  Image level3Image = new Image(IMAGES_PATH + "rallyTestImage.jpg");
  Separator levelSeparator = new Separator();
  Button playButton = new Button("Play");

  ToggleGroup toggleGroup = new ToggleGroup();

  Scene scene;

  public SetupScreen() {
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

    twoColumn.getChildren().addAll(leftColumn, twoColumnSeparator, rightColumn);

    leftColumn.getChildren().addAll(playersLabel, playerSlider, leftSliderSeparator, namesLabel, namesVBox, nameSeparator, backButton);
    namesVBox.getChildren().addAll(name1HBox, name2HBox, name3HBox, name4HBox, name5HBox, name6HBox);
    name1HBox.getChildren().addAll(name1ColorPicker, name1TextField);
    name2HBox.getChildren().addAll(name2ColorPicker, name2TextField);
    name3HBox.getChildren().addAll(name3ColorPicker, name3TextField);
    name4HBox.getChildren().addAll(name4ColorPicker, name4TextField);
    name5HBox.getChildren().addAll(name5ColorPicker, name5TextField);
    name6HBox.getChildren().addAll(name6ColorPicker, name6TextField);

    rightColumn.getChildren().addAll(AILabel, AISlider, rightSliderSeparator, levelLabel, levelVBox, levelSeparator, playButton);
    levelVBox.getChildren().addAll(level1HBox, level2HBox, level3HBox);
    level1HBox.getChildren().addAll(level1Button, level1ImageView);
    level2HBox.getChildren().addAll(level2Button, level2ImageView);
    level3HBox.getChildren().addAll(level3Button, level3ImageView);

    scene = new Scene(twoColumn);

    setSliderCallbacks();
    updatePlayerMenu((int) playerSlider.getValue());
    setRadioButtonCallbacks();
  }

  private void setRadioButtonCallbacks() {
  }

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
    List<Pair<ColorPicker, TextField>> playerMenuItems = List.of(
        new Pair<>(name1ColorPicker, name1TextField),
        new Pair<>(name2ColorPicker, name2TextField),
        new Pair<>(name3ColorPicker, name3TextField),
        new Pair<>(name4ColorPicker, name4TextField),
        new Pair<>(name5ColorPicker, name5TextField),
        new Pair<>(name6ColorPicker, name6TextField));

    for (int i = 0; i < 6; i++) {
      if (playerCount > i) {
        playerMenuItems.get(i).getKey().setDisable(false);
        playerMenuItems.get(i).getValue().setDisable(false);
      } else {
        playerMenuItems.get(i).getKey().setDisable(true);
        playerMenuItems.get(i).getValue().setDisable(true);
      }
    }

    final int aiBegin = (int)(playerSlider.getValue() - AISlider.getValue());
    int playerNum = 1;
    int aiNum = 1;
    for (int i = 0; i < 6; i++) {
      if (i >= playerCount ) {
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
