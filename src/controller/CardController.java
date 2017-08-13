package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import loader.AllCards;
import loader.HeroLoader;
import objects.Card;
import objects.HeroCard;
import storage.MySQLConnector;

import java.sql.ResultSet;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class CardController {
    @FXML
    public Label cardname;

    @FXML
    public Label livepointstxt;

    @FXML
    public Label attackpointstxt;

    @FXML
    public Label defendpointstxt;

    @FXML
    public ProgressBar livepoints;

    @FXML
    public ImageView cardimage;

    @FXML
    public Label description;

    private int maxleben = 0;


    public void initialize() {
        setLiveListener();

    }

    public void setLiveListener() {
        livepoints.progressProperty().addListener((observable, oldValue, newValue) -> System.out.println("Damage: " + oldValue + "  " + newValue));
    }

    public void setImage(Image image) {
        cardimage.setImage(image);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setCardname(String name, int id) {
        this.cardname.setText(name);
    }

    public void setLivepoints(int progress, int cardid) {
        maxleben = ((HeroCard) AllCards.getInstance().getCardbyID(cardid)).getMaxleben();
        double value = ((double)progress) / ((double)maxleben);
        System.out.println("Leben: " + value + "  " + maxleben + "  " + progress);
        Platform.runLater(() -> livepoints.setProgress(value));
    }

    public int getLivepoints() {
        return (int) (livepoints.progressProperty().get() * 100.0);
    }

    public void setAttackpoints(int attackpoints) {
        attackpointstxt.setText(String.valueOf(attackpoints));
    }

    public void setDefendpoints(int defendpoints) {
        defendpointstxt.setText(String.valueOf(defendpoints));
    }

}
