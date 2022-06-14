package com.roborally.view;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogFacade {
  public static boolean newConfirmationAlert(String title, String text) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(text);
    alert.setContentText(title);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }
}
