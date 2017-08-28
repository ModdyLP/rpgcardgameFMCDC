package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.Client;
import objects.Lobby;
import storage.FileDriver;
import utils.LoginDialog;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    @FXML
    public ListView<Lobby> lobbylist;

    public boolean loaded = false;
    public boolean login = false;

    public boolean authorized = false;

    public Lobby selectedlobby = null;

    @FXML
    public Label serverstatus;

    @FXML
    public Button neueLobby;


    private static LobbyController instance;
    private Client client;

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
            login = false;
            loaded = false;
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
        client = new Client();
        client.createClient();
    }
    public void load() {
        if(!login) {
            if (FileDriver.getInstance().getPropertyOnly("username") != null) {
                LoginDialog.createLoginDialog();
                login = true;
            } else {
                LoginDialog.createRegisterDialog();
                login = true;
            }
        }
        if (!loaded && authorized) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        ObservableList<Lobby> items = FXCollections.observableArrayList(new Lobby("Lobby1", "test", ""));
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
                                        System.out.println(lobbylist.getSelectionModel().getSelectedItem().getName());
                                        selectedlobby = lobbylist.getSelectionModel().getSelectedItem();
                                        MainController.getInstance().start();
                                    }
                                }
                            }
                        });
                    });


                }
            });
            thread.start();
            loaded = true;
        }
    }
}
