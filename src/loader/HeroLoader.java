package loader;

import controller.MainController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;


/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class HeroLoader {

    private static HeroLoader instance;
    private Card herocard;
    private Card enemyherocard;
    private GridPane herocardINST;
    private GridPane enemyherocardINST;

    public static HeroLoader getInstance() {
        if (instance == null) {
            instance = new HeroLoader();
        }
        return instance;
    }
    
    public Card getHerocard() {
        return herocard;
    }

    public void setHerocard(Card herocard, GridPane gridPane) {
        this.herocard = herocard;
        this.herocardINST = gridPane;
        ContextMenu cm = new ContextMenu();
        MenuItem legen = new MenuItem("Angreifen");
        legen.setOnAction(e -> {
            if (enemyherocardINST != null) {
                System.out.println("Karte greift an"+herocard.getCardname());
            } else {
                MainController.getInstance().setStatus("Es ist keine Gegner Karte auf dem Spielfeld");
            }
        });

        cm.getItems().add(legen);
        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            System.out.println(herocard.getCardname());
            cm.show(gridPane, event.getScreenX(), event.getScreenY());
        });
    }

    public Card getEnemyherocard() {
        return enemyherocard;
    }
    public void checkIfCarddie() {
        if (((HeroCard) enemyherocard).getLivePoints() <= 0) {

        }
    }

    public void setEnemyherocard(Card enemyherocard, GridPane gridPane) {
        this.enemyherocard = enemyherocard;
        this.enemyherocardINST = gridPane;
    }
}
