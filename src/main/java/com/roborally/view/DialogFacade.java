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

  /**
   * @author Lucas Eiruff
   *
   * Prompts the user with a confirmation alert
   *
   * @param title the dialog's title
   * @param text the dialog's text
   * @return true if the user pressed ok, false otherwise.
   */
  public static boolean newConfirmationAlert(String title, String text) {
    Alert alert = newAlert(AlertType.CONFIRMATION, title, text);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }

  /**
   * @author Lucas Eiruff
   *
   * Prompts the user with an information alert
   *
   * @param title the dialog's title
   * @param text the dialog's text
   * @return true if the user pressed ok, false otherwise.
   */
  public static boolean newInformationAlert(String title, String text) {
    Alert alert = newAlert(AlertType.INFORMATION, title, text);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }

  /**
   * @auhor Lucas Eiruff
   *
   * Prompts the user with a text dialog
   *
   * @param title the dialog's title
   * @param text the dialog's text
   * @return the text the user entered or an empty string if the user entered nothing
   */
  public static String newTextInputDialog(String title, String text) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(text);
    return dialog.showAndWait().orElse("");
  }

  /**
   * @author Lucas Eiruff
   *
   * Prompts the user with a choice dialog.
   *
   * @param choices the choices present in the dialog
   * @param title the dialog's title
   * @param text the dialog's text
   * @return the opotion the user chose
   */
  public static String newChoiceDialog(List<String> choices, String title, String text) {
    assert !choices.isEmpty();
    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
    dialog.setTitle(title);
    dialog.setHeaderText(text);
    return dialog.showAndWait().orElseThrow();
  }
}
