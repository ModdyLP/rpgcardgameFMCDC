package utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class GeneralDialog {
    public static void littleInfoDialog(String message, String title) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setHeaderText(title);
        dialog.setTitle(title);
        dialog.setContentText(message);
        dialog.showAndWait();
    }

    public static void logout() {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setHeaderText("Du wurdest ausgeloggt");
            dialog.setTitle("Logout");
            dialog.setContentText("Du wurdest ausgeloggt, entweder durch deinen Mitspieler oder du selbst hast das Spiel verlassen");
            dialog.showAndWait();
        });
    }
}
