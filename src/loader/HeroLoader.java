package loader;

import controller.HubController;
import controller.MainController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;
import org.bson.Document;
import org.bson.types.ObjectId;
import storage.MongoDBConnector;
import utils.Utils;

import java.util.ArrayList;


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
                System.out.println("Karte greift an: "+herocard.getCardname());
                ((HeroCard) enemyherocard).setLivePoints(((HeroCard) enemyherocard).getLivePoints() - Utils.runden(((HeroCard) herocard).getAttackpoints(), ((HeroCard) enemyherocard).getDefendpoints()));
                if (GameLoader.getInstance().player1) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(GameLoader.getInstance().selectedlobby.getUuid())),
                            new Document("$set", new Document("player2herocardleben", ((HeroCard) enemyherocard).getLivePoints())));
                } else {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(GameLoader.getInstance().selectedlobby.getUuid())),
                            new Document("$set", new Document("player1herocardleben", ((HeroCard) enemyherocard).getLivePoints())));
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
                cm.show(gridPane, event.getScreenX(), event.getScreenY());
            }
        });
        HubController.getInstance().setHeroCard(gridPane);
    }

    public Card getEnemyherocard() {
        return enemyherocard;
    }
    public void checkIfCarddie() {
        if (enemyherocardINST != null && herocard != null) {
            System.out.println("ENEMY: " + ((HeroCard) enemyherocard).getLivePoints() + "HERO: " + ((HeroCard) herocard).getLivePoints());
            if (((HeroCard) enemyherocard).getLivePoints() <= 0) {
                HubController.getInstance().sendCardToGraveyard(enemyherocardINST, enemyherocard);
                graveyardloader.getInstance().setGraveyard(enemyherocardINST);
                graveyardloader.getInstance().setGravecard(enemyherocard);
            }
            if (((HeroCard) herocard).getLivePoints() <= 0) {
                graveyardloader.getInstance().setGraveyard(herocardINST);
                graveyardloader.getInstance().setGravecard(herocard);
                HubController.getInstance().sendCardToGraveyard(herocardINST, herocard);
            }
        }
    }
    public void loadEnemyCard() {
        try {
            ArrayList<Document> documents = MongoDBConnector.getInstance().getCollectionAsList("Game");
            for (Document doc : documents) {
                if (GameLoader.getInstance().player1) {
                    if (doc.getInteger("player2herocard") != null && doc.getInteger("player2herocard") != 0) {
                        HeroCard card = (HeroCard) AllCards.getInstance().getCardbyID(doc.getInteger("player2herocard"));
                        card.setLivePoints(doc.getInteger("player2herocardleben"));
                        GridPane pane = GeneralCardLoader.loadHandCard(card, HubController.getInstance().getCardbox());
                        setEnemyherocard(card, pane);
                    }
                } else  {
                    if (doc.getInteger("player1herocard") != null && doc.getInteger("player1herocard") != 0) {
                        HeroCard card = (HeroCard) AllCards.getInstance().getCardbyID(doc.getInteger("player1herocard"));
                        card.setLivePoints(doc.getInteger("player1herocardleben"));
                        GridPane pane = GeneralCardLoader.loadHandCard(card, HubController.getInstance().getCardbox());
                        setEnemyherocard(card, pane);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void loadHero() {
        if (herocard != null && herocardINST != null) {
            try {
                ArrayList<Document> documents = MongoDBConnector.getInstance().getCollectionAsList("Game");
                for (Document doc : documents) {
                    if (GameLoader.getInstance().player1) {
                        if (doc.getInteger("player1herocard") == herocard.getCardnummer()) {
                            ((HeroCard) herocard).setLivePoints(doc.getInteger("player1herocardleben"));
                            System.out.println("Karte sync " + ((HeroCard) herocard).getController().getLivepoints() + "  " + ((HeroCard) herocard).getCardnummer());
                        } else {
                            System.out.println("Karte passt nicht zur Nummer: " + herocard.getCardnummer() + " " + doc.getInteger("player1herocard"));
                        }
                    } else  {
                        if (doc.getInteger("player2herocard") == herocard.getCardnummer()) {
                            ((HeroCard) herocard).setLivePoints(doc.getInteger("player2herocardleben"));
                            System.out.println("Karte sync " + ((HeroCard) herocard).getController().getLivepoints() + "  " + ((HeroCard) herocard).getCardnummer());
                        } else {
                            System.out.println("Karte passt nicht zur Nummer: " + herocard.getCardnummer() + " " + doc.getInteger("player2herocard"));
                        }
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
        HubController.getInstance().setEnemyCard(gridPane);
    }
}
