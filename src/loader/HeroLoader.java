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
                if (GameLoader.getInstance().getSpielerid() == 1) {
                    MySQLConnector.getInstance().execute("UPDATE `Spieler2` SET leben = '"+((HeroCard) enemyherocard).getLivePoints()+"'");
                } else if (GameLoader.getInstance().getSpielerid() == 2) {
                    MySQLConnector.getInstance().execute("UPDATE `Spieler1` SET leben = '"+((HeroCard) enemyherocard).getLivePoints()+"'");
                }
            } else {
                MainController.getInstance().setStatus("Es ist keine Gegner Karte auf dem Spielfeld");
                System.out.println("Karte greift an"+herocard.getCardname());
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
        if (enemyherocardINST != null && herocard != null) {
            System.out.println("ENEMY: " + ((HeroCard) enemyherocard).getLivePoints() + "HERO: " + ((HeroCard) herocard).getLivePoints());
            if (((HeroCard) enemyherocard).getLivePoints() <= 0) {
                HubController.getInstance().sendCardToGraveyard(enemyherocardINST);
            }
            if (((HeroCard) herocard).getLivePoints() <= 0) {
                HubController.getInstance().sendCardToGraveyard(herocardINST);
            }
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
                if (rs.getInt("nr") != 0) {
                    HeroCard card = (HeroCard) AllCards.getInstance().getCardbyID(rs.getInt("nr"));
                    card.setLivePoints(rs.getInt("leben"));
                    GridPane pane = GeneralCardLoader.loadHandCard(card, HubController.getInstance().getCardbox());
                    HubController.getInstance().setEnemyCard(pane);
                    setEnemyherocard(card, pane);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void loadHero() {
        if (herocard != null) {
            try {
                ResultSet rs = null;
                if (GameLoader.getInstance().getSpielerid() == 1) {
                    rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM `Spieler1` WHERE kartenpos = 1");
                } else if (GameLoader.getInstance().getSpielerid() == 2) {
                    rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM `Spieler2` WHERE kartenpos = 1");
                }
                while (rs != null && rs.next()) {
                    if (rs.getInt("nr") == herocard.getCardnummer()) {
                        ((HeroCard) herocard).setLivePoints(rs.getInt("leben"));
                        System.out.println("Karte sync "+((HeroCard) herocard).getLivePoints()+"  "+((HeroCard) herocard).getCardnummer());
                    } else {
                        System.out.println("Karte passt nicht zur Nummer: " + herocard.getCardnummer() + " " + rs.getInt("nr"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setEnemyherocard(Card enemyherocard, GridPane gridPane) {
        this.enemyherocard = enemyherocard;
        this.enemyherocardINST = gridPane;
    }
}
