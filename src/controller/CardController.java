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
    public ProgressBar attackpoints;

    @FXML
    public ProgressBar defendpoints;

    @FXML
    public ImageView cardimage;

    @FXML
    public Label description;

    public void setImage(Image image) {
        cardimage.setImage(image);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setCardname(String name) {
        this.cardname.setText(name);
    }

}
