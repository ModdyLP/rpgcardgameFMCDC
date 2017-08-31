package utils;

import controller.LobbyController;
import controller.MainController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import loader.GameLoader;
import org.bson.Document;
import org.bson.types.ObjectId;
import storage.FileDriver;
import storage.MongoDBConnector;

import java.util.ArrayList;
import java.util.Optional;

public class LoginDialog {

    private static Dialog<Pair<String, String>> logindialog = null;
    private static Dialog<Pair<String, String>> registerdialog = null;
    private static Optional<Pair<String, String>> loginresult = Optional.empty();
    private static Optional<Pair<String, String>> registerresult = Optional.empty();


    public static void createLoginDialog() {
        if (logindialog == null) {
            Platform.runLater(() -> {
                // Create the custom dialog.
                logindialog = new Dialog<>();
                logindialog.setTitle("Login Dialog");
                logindialog.setHeaderText("Bitte melde dich an");

                // Set the icon (must be included in the project).
                logindialog.setGraphic(new ImageView(MainController.class.getResource("/logo.png").toString()));

                // Set the button types.
                ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
                logindialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField username = new TextField();
                username.setText(FileDriver.getInstance().getProperty("username", "").toString());
                PasswordField password = new PasswordField();
                password.setPromptText("Password");

                grid.add(new Label("Username:"), 0, 0);
                grid.add(username, 1, 0);
                grid.add(new Label("Password:"), 0, 1);
                grid.add(password, 1, 1);

                // Enable/Disable login button depending on whether a username was entered.
                Node loginButton = logindialog.getDialogPane().lookupButton(loginButtonType);

                logindialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(username::requestFocus);

                // Convert the result to a username-password-pair when the login button is clicked.
                logindialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(username.getText(), password.getText());
                    } else {
                        LobbyController.getInstance().disableLobby(true);
                    }
                    return null;
                });

                loginresult = logindialog.showAndWait();
                loginauswerten();

            });
        } else {
            Platform.runLater(() -> {
                if (!logindialog.isShowing()) {
                    loginresult = logindialog.showAndWait();
                    loginauswerten();
                }
            });

        }
    }

    public static void createRegisterDialog() {
        if (registerdialog == null) {
            Platform.runLater(() -> {
                // Create the custom dialog.
                registerdialog = new Dialog<>();
                registerdialog.setTitle("Registrierung");
                registerdialog.setHeaderText("Bitte registriere einen Account");
                ImageView imageView = new ImageView(MainController.class.getResource("/logo.png").toString());
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                // Set the icon (must be included in the project).
                registerdialog.setGraphic(imageView);

                // Set the button types.
                ButtonType Register = new ButtonType("Login", ButtonBar.ButtonData.APPLY);
                ButtonType loginButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
                registerdialog.getDialogPane().getButtonTypes().addAll(Register, loginButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField username = new TextField();
                username.setPromptText("Username");
                PasswordField password = new PasswordField();
                password.setPromptText("Password");
                PasswordField passwordretry = new PasswordField();
                passwordretry.setPromptText("Password wiederholen");

                grid.add(new Label("Username:"), 0, 0);
                grid.add(username, 1, 0);
                grid.add(new Label("Password:"), 0, 1);
                grid.add(password, 1, 1);
                grid.add(new Label("Password Wiederholung:"), 0, 2);
                grid.add(passwordretry, 1, 2);

                // Enable/Disable login button depending on whether a username was entered.
                Node loginButton = registerdialog.getDialogPane().lookupButton(loginButtonType);
                loginButton.setDisable(true);

                passwordretry.textProperty().addListener(((observable, oldValue, newValue) -> {
                    loginButton.setDisable(!password.getText().equals(passwordretry.getText()));
                }));

                registerdialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(username::requestFocus);

                // Convert the result to a username-password-pair when the login button is clicked.
                registerdialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(username.getText(), password.getText());
                    } else if (dialogButton == Register) {
                        registerdialog.close();
                        createLoginDialog();
                    } else {
                        LobbyController.getInstance().disableLobby(true);
                    }
                    return null;
                });

                registerresult = registerdialog.showAndWait();

                registerauswerten();
            });
        } else {
            Platform.runLater(() -> {
                if (!registerdialog.isShowing()) {
                    registerresult = registerdialog.showAndWait();
                    registerauswerten();
                }
            });
        }
    }

    private static void loginauswerten() {
        loginresult.ifPresent(usernamePassword -> {
            try {
                Document document = new Document("name", usernamePassword.getKey());
                ArrayList<Document> list = MongoDBConnector.getInstance().getMongoDatabase().getCollection("Players").find(document).into(new ArrayList<>());
                if (list != null && list.size() > 0) {
                    Document proof = list.get(0);
                    if (Crypter.check(usernamePassword.getValue(), proof.getString("password"))) {
                        FileDriver.getInstance().setProperty("username", usernamePassword.getKey());
                        FileDriver.getInstance().setProperty("userid", proof.get("_id").toString());
                        FileDriver.getInstance().saveJson();
                        GeneralDialog.littleInfoDialog("Login war erfolgreich", "Erfolg");
                        GameLoader.getInstance().spielername = proof.getString("name");
                        GameLoader.getInstance().spielerid = proof.get("_id").toString();
                        GameLoader.getInstance().authorized = true;
                        LobbyController.getInstance().disableLobby(false);
                    } else {
                        GeneralDialog.littleInfoDialog("Login fehlgeschlagen", "Fehlschlag");
                        LobbyController.getInstance().disableLobby(true);
                    }
                } else {
                    GeneralDialog.littleInfoDialog("Login fehlgeschlagen", "Fehlschlag");
                    LobbyController.getInstance().disableLobby(true);
                }
            } catch (Exception ex) {
                LobbyController.getInstance().disableLobby(true);
                ex.printStackTrace();
            }
        });
    }

    public static void registerauswerten() {
        registerresult.ifPresent(usernamePassword -> {
            try {
                FileDriver.getInstance().setProperty("username", usernamePassword.getKey());
                ObjectId id = new ObjectId();
                FileDriver.getInstance().setProperty("userid", id.toString());
                FileDriver.getInstance().saveJson();
                Document document = new Document("_id", id)
                        .append("name", usernamePassword.getKey())
                        .append("password", Crypter.getSaltedHash(usernamePassword.getValue()))
                        .append("logintimes", 0)
                        .append("playedgames", 0);
                MongoDBConnector.getInstance().getMongoDatabase().getCollection("Players").insertOne(document);
                GameLoader.getInstance().authorized = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                LobbyController.getInstance().disableLobby(true);
            }
        });
    }
}
