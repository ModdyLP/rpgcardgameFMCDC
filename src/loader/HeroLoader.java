package loader;

import controller.HubController;
import controller.MainController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;
import storage.MySQLConnector;
import utils.GeneralDialog;
import utils.Utils;

import java.sql.ResultSet;


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
                ((HeroCard) enemyherocard).setLivePoints(((HeroCard) enemyherocard).getLivePoints() - Utils.runden(((HeroCard) herocard).getAttackpoints(), ((HeroCard) enemyherocard).getDefendpoints()));
            } else {
                MainController.getInstance().setStatus("Es ist keine Gegner Karte auf dem Spielfeld");
            }
            RoundLoader.getInstance().setAttackcounter(RoundLoader.getInstance().getAttackcounter()+1);
        });

        cm.getItems().add(legen);
        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (RoundLoader.getInstance().getAttackcounter() < 1) {
                System.out.println(herocard.getCardname());
                cm.show(gridPane, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public Card getEnemyherocard() {
        return enemyherocard;
    }
    public void checkIfCarddie() {
        if (((HeroCard) enemyherocard).getLivePoints() <= 0) {
            HubController.getInstance().sendCardToGraveyard(enemyherocardINST);
        }
        if (((HeroCard) herocard).getLivePoints() <= 0) {
            HubController.getInstance().sendCardToGraveyard(herocardINST);
        }
    }
    public void loadEnemyCard() {
        try {
            ResultSet rs = null;
            if (GameLoader.getInstance().getSpielerid() == 1) {
                rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM `Spieler2` WHERE kartenpos = 1");
            } else if (GameLoader.getInstance().getSpielerid() == 2) {
                rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM `Spieler1` WHERE kartenpos = 1");
            }
            while (rs != null && rs.next()) {
                HubController.getInstance().setEnemyCard(GeneralCardLoader.loadHandCard(new HeroCard(rs.getInt("nr"), rs.getString("name"), rs.getString("bild") + ".png", "", rs.getInt("leben"), rs.getInt("verteidigung"), rs.getInt("angriff")), HubController.getInstance().getCardbox()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setEnemyherocard(Card enemyherocard, GridPane gridPane) {
        this.enemyherocard = enemyherocard;
        this.enemyherocardINST = gridPane;
    }
}
