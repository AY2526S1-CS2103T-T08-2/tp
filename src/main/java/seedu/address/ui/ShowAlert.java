package seedu.address.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Helper class for showing alert dialogs.
 */
public class ShowAlert {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";
    private static final String DARK_THEME_STYLESHEET = "view/DarkTheme.css";

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     *
     * @param owner       Owner window of this dialog.
     * @param type        Type of alert to be displayed.
     * @param title       Title of the alert dialog.
     * @param headerText  Header text of the alert dialog.
     * @param contentText Content text of the alert dialog.
     */
    public static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                              String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add(DARK_THEME_STYLESHEET);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }
}
