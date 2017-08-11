package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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


    public void initialize() {
        setLivepoints(100);
        setAttackpoints(0);
        setDefendpoints(0);
    }

    public void setImage(Image image) {
        cardimage.setImage(image);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setCardname(String name) {
        this.cardname.setText(name);
    }
    public void setLivepoints(int progress) {
        livepoints.progressProperty().set(progress);
    }
    public void setAttackpoints(int attackpoints) {
        attackpointstxt.setText(String.valueOf(attackpoints));
    }
    public void setDefendpoints(int defendpoints) {
        defendpointstxt.setText(String.valueOf(defendpoints));
    }

}
