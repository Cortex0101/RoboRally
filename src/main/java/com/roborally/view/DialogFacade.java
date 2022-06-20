package com.roborally.view;

import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

public class DialogFacade {

  private static Alert newAlert(AlertType type, String title, String text) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(text);
    alert.setContentText(title);
    return alert;
  }

  /**
   * @param title the dialog's title
   * @param text  the dialog's text
   * @return true if the user pressed ok, false otherwise.
   * @author Lucas Eiruff
   * <p>
   * Prompts the user with a confirmation alert
   */
  public static boolean newConfirmationAlert(String title, String text) {
    Alert alert = newAlert(AlertType.CONFIRMATION, title, text);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }

  /**
   * @param title the dialog's title
   * @param text  the dialog's text
   * @return true if the user pressed ok, false otherwise.
   * @author Lucas Eiruff
   * <p>
   * Prompts the user with an information alert
   */
  public static boolean newInformationAlert(String title, String text) {
    Alert alert = newAlert(AlertType.INFORMATION, title, text);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isEmpty() || !result.get().equals(ButtonType.OK);
  }

  /**
   * @param title the dialog's title
   * @param text  the dialog's text
   * @return the text the user entered or an empty string if the user entered nothing
   * @auhor Lucas Eiruff
   * <p>
   * Prompts the user with a text dialog
   */
  public static String newTextInputDialog(String title, String text) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(text);
    return dialog.showAndWait().orElse("");
  }

  /**
   * @param choices the choices present in the dialog
   * @param title   the dialog's title
   * @param text    the dialog's text
   * @return the opotion the user chose
   * @author Lucas Eiruff
   * <p>
   * Prompts the user with a choice dialog.
   */
  public static String newChoiceDialog(List<String> choices, String title, String text) {
    assert !choices.isEmpty();
    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
    dialog.setTitle(title);
    dialog.setHeaderText(text);
    return dialog.showAndWait().orElseThrow();
  }
}
