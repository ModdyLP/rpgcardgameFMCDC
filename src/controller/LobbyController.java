package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import loader.GameLoader;
import main.Client;
import objects.Lobby;
import org.bson.Document;
import org.bson.types.ObjectId;
import storage.FileDriver;
import storage.MongoDBConnector;
import utils.LoginDialog;

import java.lang.annotation.Documented;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    @FXML
    public ListView<Lobby> lobbylist;

    @FXML
    public Label serverstatus;

    @FXML
    public Button neueLobby;


    private static LobbyController instance;

    public static LobbyController getInstance() {
        return instance;
    }
    public void disableLobby(boolean disable) {
        if(!disable) {
            Platform.runLater(() -> neueLobby.setDisable(false));
            load();
        } else {
            Platform.runLater(() -> neueLobby.setDisable(true));
            Platform.runLater(() -> lobbylist.getItems().clear());
            GameLoader.getInstance().login = false;
            GameLoader.getInstance().loaded = false;
        }
    }

    public void setOffline(boolean offline) {
        if (offline) {
            Platform.runLater(() -> serverstatus.setText("OFFLINE"));
            Platform.runLater(() -> lobbylist.getItems().clear());
            neueLobby.setDisable(true);
        } else {
            Platform.runLater(() -> serverstatus.setText("ONLINE"));
            load();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        setOffline(true);
        Client.getInstance().createClient();
    }
    public void load() {
        if(!GameLoader.getInstance().login) {
            if (FileDriver.getInstance().getPropertyOnly("username") != null) {
                LoginDialog.createLoginDialog();
                GameLoader.getInstance().login = true;
            } else {
                LoginDialog.createRegisterDialog();
                GameLoader.getInstance().login = true;
            }
        }
        if (!GameLoader.getInstance().loaded && GameLoader.getInstance().authorized) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        ObservableList<Lobby> items = FXCollections.observableArrayList();
                        ArrayList<Document> list = MongoDBConnector.getInstance().getCollectionAsList("Game");
                        for (Document document: list) {
                            items.add(new Lobby(document.getString("lobbyname"), document.get("_id").toString() ,document.getString("password")));
                        }
                        lobbylist.setItems(items);
                        lobbylist.setEditable(false);
                        lobbylist.setCellFactory(param -> new ListCell<Lobby>() {
                            @Override
                            protected void updateItem(Lobby item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    setText(item.getName());
                                }
                            }
                        });
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Bestätigung");
                        alert.setContentText("Wollen sie wirklich der Lobby beitreten?");
                        lobbylist.setOnMouseClicked(event -> {
                            if (lobbylist.getSelectionModel().getSelectedItem() != null) {
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent()) {
                                    if (result.get().equals(ButtonType.OK) && lobbylist.getSelectionModel().getSelectedItem() != null) {
                                        GameLoader.getInstance().selectedlobby = lobbylist.getSelectionModel().getSelectedItem();
                                        System.out.println("Lobby: "+GameLoader.getInstance().selectedlobby.getName());
                                        MainController.getInstance().start();
                                    }
                                }
                            }
                        });
                    });


                }
            });
            thread.start();
            GameLoader.getInstance().loaded = true;
        }
    }
}
