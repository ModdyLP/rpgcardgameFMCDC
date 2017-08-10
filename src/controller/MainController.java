package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import utils.GeneralDialog;

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
    private BorderPane mainLayout;

    public static MainController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        setStatus("Bereit...");
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

    //Listener
    public void exit() {
        GeneralDialog.littleInfoDialog("Danke für das Ausprobieren unseres Spiels.", "Danke");
        System.exit(0);
    }

}
