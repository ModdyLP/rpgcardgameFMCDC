package loader;

import controller.CardController;
import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import objects.Card;

import java.io.File;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class GeneralCardLoader {
    public static GridPane loadHandCard(Card karte, GridPane cardbox) {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/controller/card.fxml"));
            GridPane card = loader.load();
            card.setMaxHeight(cardbox.getMaxHeight());
            CardController controller = loader.getController();
            File file = new File(karte.getFileurl());
            System.out.println(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            controller.setImage(image);
            controller.setDescription(karte.getDescription());
            controller.setCardname(karte.getCardname());
            return card;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
