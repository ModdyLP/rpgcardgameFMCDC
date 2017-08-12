package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import loader.GameLoader;
import loader.GeneralCardLoader;
import utils.GeneralDialog;

import java.io.IOException;

public class MainController {

    private static MainController instance;
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
    private GridPane pane;


    public static MainController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        setDEFStatus();
        GeneralDialog.littleInfoDialog("Willkommen zu unserem kleinen Kartenspiel. \n" +
                "Programmiert und designet beim Code & Design Camp in Frankfurt a.M. von \n" +
                "Niklas H. \n" +
                "Maren G. \n" +
                "Alexander D. \n" +
                "Jessie L. \n" +
                "Joshua H.", "Willkommen");
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
            GeneralDialog.logout();
            System.exit(0);
        });
    }
    public void restart() {
        GameLoader.getInstance().setStart(false);
        setStatus("Lade Hub...");
        GeneralCardLoader.clearall();
        if (pane != null) {
            mainLayout.setCenter(null);
            HubController.destroy();
        }
        MainController.getInstance().setStatus("Starte neu");
        Platform.runLater(() -> {
            try {
                FXMLLoader hubloader = new FXMLLoader(getClass().getResource("/controller/hub.fxml"));
                pane = hubloader.load();
                pane.setMinWidth(mainLayout.getWidth()-80);
                pane.setMinHeight(mainLayout.getHeight()-80);
                HubController controller = hubloader.getController();
                mainLayout.setCenter(pane);
                setDEFStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        GameLoader.getInstance().setStart(true);
        GameLoader.getInstance().gameloop();
    }

}
