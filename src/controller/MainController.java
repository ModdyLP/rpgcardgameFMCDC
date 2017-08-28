package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import loader.GameLoader;
import loader.GeneralCardLoader;
import main.Client;
import objects.Lobby;
import storage.MongoDBConnector;
import utils.GeneralDialog;

public class MainController {

    private static MainController instance;

    private boolean gameisrunning = false;

    @FXML
    private Label status;

    @FXML
    private Menu spielMenu;

    @FXML
    private MenuItem verlassen;

    @FXML
    private MenuItem neustart;

    @FXML
    public BorderPane mainLayout;
    private boolean offline = false;

    public void setPrimarystage(Stage primarystage) {
        this.primarystage = primarystage;
        this.primarystage.getIcons().add(new Image(MainController.class.getResourceAsStream("/logo.png")));
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    private Stage primarystage;
    public GridPane pane;


    public static MainController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        setDEFStatus();
        GeneralDialog.littleInfoDialog("Willkommen zu unserem kleinen Kartenspiel (Proelignis). \n" +
                "Programmiert und designet beim Code & Design Camp in Frankfurt a.M. von \n" +
                "Niklas H. \n" +
                "Maren G. \n" +
                "Alexander D. \n" +
                "Jessie L. \n" +
                "Joshua H.", "Willkommen");
        reset();
    }

    //Utils for Layout
    public void setStatus(String status) {
        Platform.runLater(()-> this.status.setText(status));
    }
    //Utils for Layout
    public void setDEFStatus() {
        Platform.runLater(()-> this.status.setText("Bereit..."));
    }

    //Listener
    public void exit() {
        //GeneralDialog.littleInfoDialog("Danke für das Ausprobieren unseres Spiels.", "Danke");
        Platform.runLater(() -> {
            GameLoader.getInstance().setStart(false);
            GameLoader.getInstance().logout();
            MongoDBConnector.getInstance().close();
            System.exit(0);
        });
    }
    public void reset() {
        setDEFStatus();
        Platform.runLater(() -> {
            try {
                mainLayout.setCenter(null);
                FXMLLoader lobbyloader = new FXMLLoader(getClass().getResource("/lobby.fxml"));
                AnchorPane pane = lobbyloader.load();
                LobbyController controller = lobbyloader.getController();
                mainLayout.setCenter(pane);
                setDEFStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void start() {
        System.out.println("Start init");
        if (LobbyController.getInstance().selectedlobby != null) {
            setStatus("Lade Kartendeck...");
            Platform.runLater(() -> {
                try {
                    mainLayout.setCenter(null);
                    FXMLLoader hubloader = new FXMLLoader(getClass().getResource("/hub.fxml"));
                    pane = hubloader.load();
                    pane.setMinWidth(mainLayout.getWidth() - 80);
                    pane.setMinHeight(mainLayout.getHeight() - 80);
                    HubController controller = hubloader.getController();
                    mainLayout.setCenter(pane);
                    setDEFStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            GameLoader.getInstance().setStart(true);
            GameLoader.getInstance().gameloop();
            LobbyController.getInstance().selectedlobby = null;
        }
    }

}
