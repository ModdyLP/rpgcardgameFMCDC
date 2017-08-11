package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import objects.HeroCard;

import java.io.File;

public class HubController {

    @FXML
    private GridPane cardbox;

    @FXML
    private BorderPane hero1;

    @FXML
    private BorderPane hero2;

    @FXML
    private BorderPane draw1;

    @FXML
    private BorderPane draw2;

    @FXML
    private BorderPane grave;

    public void addCardToHbox() {
        grave.setCenter(loadCard("/controller/card.fxml", "exportpng/elfe.png"));
        cardbox.add(loadCard("/controller/card.fxml", "exportpng/elfe.png"), 0, 0);
        cardbox.add(loadCard("/controller/card.fxml", "exportpng/vampire.png"), 1, 0);
        cardbox.add(loadCard("/controller/card.fxml", "exportpng/dragon.png"), 2, 0);
    }
    public GridPane loadCard(String ressource, String imageurl) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ressource));
            GridPane card = loader.load();
            card.setMaxHeight(cardbox.getMaxHeight());
            CardController controller = loader.<CardController>getController();
            File file = new File(imageurl);
            System.out.println(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            controller.setImage(image);
            return card;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
