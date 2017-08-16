package loader;

import controller.CardController;
import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;

import java.io.File;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class GeneralCardLoader {
    public static GridPane loadHandCard(Card karte, GridPane pane) {
        try {
            HeroCard herokarte = (HeroCard) karte;
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/card.fxml"));
            GridPane card = loader.load();
            card.setMaxHeight(pane.getMaxHeight());
            CardController controller = loader.getController();
            File file = new File(herokarte.getFileurl());
            System.out.println(herokarte.getFileurl());
            if (!file.exists()) {
                file = new File(MainController.class.getResource("/Fragezeichen.png").getPath());
            }
            System.out.println(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            controller.setImage(image);
            controller.setDescription(herokarte.getDescription());
            controller.setCardname(herokarte.getCardname(), herokarte.getCardnummer());
            controller.setAttackpoints(herokarte.getAttackpoints());
            controller.setLivepoints(herokarte.getLivePoints(), herokarte.getCardnummer());
            controller.setDefendpoints(herokarte.getDefendpoints());
            return card;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void clearall() {
        AllCards.getInstance().clear();
        HandCardLoader.getInstance().clear();
    }
}
