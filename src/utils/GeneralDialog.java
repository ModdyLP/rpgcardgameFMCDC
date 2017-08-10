package utils;

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
}
