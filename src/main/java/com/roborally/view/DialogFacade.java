package com.roborally.view;

import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

public class DialogFacade {
  private static Alert newAlert(AlertType type, String title, String text) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(text);
    alert.setContentText(title);
    return alert;
  }

  public static boolean newConfirmationAlert(String title, String text) {
    Alert alert = newAlert(AlertType.CONFIRMATION, title, text);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }

  public static boolean newInformationAlert(String title, String text) {
    Alert alert = newAlert(AlertType.INFORMATION, title, text);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }

  public static String newTextInputDialog(String title, String text) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(text);
    return dialog.showAndWait().orElseThrow();
  }

  public static String newChoiceDialog(List<String> choices, String title, String text) {
    assert !choices.isEmpty();
    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
    dialog.setTitle(title);
    dialog.setHeaderText(text);
    return dialog.showAndWait().orElseThrow();
  }
}
