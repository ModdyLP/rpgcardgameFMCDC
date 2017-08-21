package loader;

import controller.HubController;
import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;
import objects.Type;
import org.bson.Document;
import org.bson.types.ObjectId;
import storage.MongoDBConnector;

import java.util.Collection;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class HandCardLoader {
    private static HandCardLoader instance;
    private ObservableMap<Integer, Card> handcards = FXCollections.observableHashMap();
    private ObservableMap<Integer, GridPane> handcardsinstance = FXCollections.observableHashMap();

    public void clear() {
        handcardsinstance.clear();
        handcards.clear();
    }

    public static HandCardLoader getInstance() {
        if (instance == null) {
            instance = new HandCardLoader();
        }
        return instance;
    }

    public Collection<GridPane> getAllHandcards() {
        return handcardsinstance.values();
    }
    public Collection<Card> getAllHandcardsVa() {
        return handcards.values();
    }

    public Card getHandCardbyID(int cardid) {
        return handcards.get(cardid);
    }

    public GridPane getHandCardINSTbyID(int cardid) {
        return handcardsinstance.get(cardid);
    }

    public void addCard(Card card) {
        handcards.put(card.getUniqueNumber(), card);
    }

    public void removeHandcard(int cardid) {
        HubController.getInstance().getCardboxPos().put(handcards.get(cardid).getPosition(), true);
        handcards.remove(cardid);
    }

    public void addINSTCard(Card card, GridPane pane) {
        final int[] cardid = {0};
        if (card.getCardtype().equals(Type.HELD)) {
            final ContextMenu cm = new ContextMenu();
            MenuItem legen = new MenuItem("Karte legen");
            legen.setOnAction(e -> {
                if (HeroLoader.getInstance().getHerocard() == null) {
                    System.out.println("Karte gelegt: " + handcards.get(cardid[0]).getCardname());
                    HeroCard heroCard = (HeroCard) card;
                    if (GameLoader.getInstance().getSpielerid() == 1) {
                        MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                                new Document("$set", new Document("player1herocard", card.getCardnummer()).append("player1herocardleben", heroCard.getLivePoints())));
                    } else if (GameLoader.getInstance().getSpielerid() == 2) {
                        MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                                new Document("$set", new Document("player2herocard", card.getCardnummer()).append("player2herocardleben", heroCard.getLivePoints())));
                    }
                    removeHandcard(cardid[0]);
                    HeroLoader.getInstance().setHerocard(card, pane);
                    removeINSTHandcard(cardid[0]);
                } else {
                    MainController.getInstance().setStatus("Es ist bereits eine Karte gelegt: ");
                }
            });

            cm.getItems().add(legen);
            showCM(cm, pane, cardid);
        } else if (card.getCardtype().equals(Type.ZAUBER)) {
            final ContextMenu cm = new ContextMenu();
            MenuItem legen = new MenuItem("Karte benutzen");
            legen.setOnAction(e -> {
                System.out.println("Karte benutzt: " + handcards.get(cardid[0]).getCardname());
                removeHandcard(cardid[0]);
                removeINSTHandcard(cardid[0]);
            });
            cm.getItems().add(legen);
            showCM(cm, pane, cardid);
        }
        handcardsinstance.put(card.getUniqueNumber(), pane);
    }

    public void removeINSTHandcard(int cardid) {
        handcardsinstance.remove(cardid);
    }

    public void loadCards() {
        handcardsinstance.addListener((MapChangeListener<Integer, GridPane>) change -> {
            if (change.wasRemoved()) {
                HubController.getInstance().removeFromHBox(change.getValueRemoved());
            }
        });


    }

    public void showCM(ContextMenu cm, GridPane pane, int[] cardid) {
        try {
            pane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (GameLoader.getInstance().isIstamzug()) {
                    for (int key : handcardsinstance.keySet()) {
                        if (handcardsinstance.get(key) != null && handcardsinstance.get(key).equals(pane)) {
                            cardid[0] = handcards.get(key).getUniqueNumber();
                            cm.show(pane, event.getScreenX(), event.getScreenY());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
