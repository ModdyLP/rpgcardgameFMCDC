package loader;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;
import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import objects.Card;
import objects.HeroCard;
import org.bson.Document;
import org.bson.types.ObjectId;
import storage.MongoDBConnector;
import storage.MySQLConnector;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class AllCards {
    private static AllCards instance;
    private int cardnummer = 0;

    private ObservableMap<Integer, Card> cards = FXCollections.observableHashMap();
    private ObservableMap<Integer, Card> spielercards = FXCollections.observableHashMap();

    public static AllCards getInstance() {
        if (instance == null) {
            instance = new AllCards();
        }
        return instance;
    }

    public Collection<Card> getCards() {
        return cards.values();
    }

    public void clear() {
        cards.clear();
        spielercards.clear();
    }

    public void addCard(Card card) {
        card.setUniqueNumber(cardnummer);
        cards.put(cardnummer, card);
        cardnummer++;
    }

    public void addPlayCard(Card card) {
        spielercards.put(card.getUniqueNumber(), card);
    }

    public void removeCard(Card card) {
        cards.remove(card.getCardnummer());
    }

    public void removePlayCard(Card card) {
        spielercards.remove(card.getCardnummer());
    }

    public void loadCards() {
        try {
            MainController.getInstance().setStatus("Loading Cards...");
            ArrayList<Document> documents = MongoDBConnector.getInstance().getCollectionAsList("Karten");
            for (Document doc : documents) {
                if (doc.getString("type").equals("HELD")) {
                    System.out.println("Load Card: "+doc.getString("name"));
                    addCard(new HeroCard(doc.getInteger("kartenid"), doc.getString("name"), doc.getString("bild") + ".png", doc.getString("beschreibung"), doc.getInteger("leben"), doc.getInteger("verteidigung"), doc.getInteger("angriff")));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MainController.getInstance().setDEFStatus();
    }

    public void splitupCards() {
        try {
            MainController.getInstance().setStatus("Verteile Karten");
            HashMap<Integer, Card> splitupcards = new HashMap<>();
            int id = 1;
            int zahler = 0;
            MongoDBConnector.getInstance().getMongoDatabase().getCollection("CardToPlayer").drop();
            ArrayList<WriteModel<Document>> player1 = new ArrayList<>();
            ArrayList<WriteModel<Document>> player2 = new ArrayList<>();
            splitupcards.putAll(cards);
            while (splitupcards.size() > 0) {
                Card card = getRandomCard(new ArrayList<>(splitupcards.values()));
                System.out.println("Verteile Karte(" + id + "): " + card.getCardnummer());
                if (id == 1) {
                    player1.add(new InsertOneModel<>(new Document("playername", "player1")
                            .append("playerid", new ObjectId("599550c53d6c11d66470145c"))
                            .append("lobby", new ObjectId("599550c53d6c00d66470145c"))
                            .append("cardid", card.getCardnummer())));
                    id = 2;
                } else if (id == 2) {
                    player2.add(new InsertOneModel<>(new Document("playername", "player2")
                            .append("playerid", new ObjectId("599550c53d6c22d66470145c"))
                            .append("lobby", new ObjectId("599550c53d6c00d66470145c"))
                            .append("cardid", card.getCardnummer())));
                    id = 1;
                }
                splitupcards.remove(card.getUniqueNumber());
            }
            BulkWriteResult bulkWriteResult = MongoDBConnector.getInstance().getMongoDatabase().getCollection("CardToPlayer").bulkWrite(player1);
            BulkWriteResult bulkWriteResult2 = MongoDBConnector.getInstance().getMongoDatabase().getCollection("CardToPlayer").bulkWrite(player2);
            if (bulkWriteResult != null && bulkWriteResult2 != null) {
                MainController.getInstance().setStatus("Karten wurden verteilt");
            } else {
                MainController.getInstance().setStatus("Karten wurden nicht verteilt");
            }
            MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                    new Document("$set", new Document("splitted", 1)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadStapelfromplayer() {
        try {
            ArrayList<Document> documents = MongoDBConnector.getInstance().getMongoDatabase().getCollection("CardToPlayer").find(new Document("playername", "player1")).into(new ArrayList<>());
            for (Document doc : documents) {
                Card card = getCardbyID(doc.getInteger("cardid"));
                addPlayCard(card);
            }
            System.out.println("Stapelgröße: " + spielercards.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MainController.getInstance().setStatus("Stapel geladen");
    }

    public Card getCardbyID(int cardid) {
        for (Card card : cards.values()) {
            if (card.getCardnummer() == cardid) {
                return card;
            }
        }
        return null;
    }

    public Card getCardbyUID(int cardid) {
        for (Card card : cards.values()) {
            if (card.getUniqueNumber() == cardid) {
                return card;
            }
        }
        return null;
    }

    public ObservableMap<Integer, Card> getSpielCards() {
        return spielercards;
    }

    private Card randomcard(ArrayList<Card> cards) {
        Random rand = new Random();
        int value = 0;
        if (cards.size() == 1) {
            value = rand.nextInt(cards.size());
        } else if (cards.size() < 1) {
            return null;
        } else {
            value = rand.nextInt(cards.size() - 1);
        }

        Card card = cards.get(value);
        int zahler1 = 1;
        while (card == null) {
            if ((value - zahler1) < cards.size() - 1) {
                card = cards.get(value - zahler1);
                if (card != null) {
                    return card;
                }
            }
            if ((value - zahler1) > 0) {
                card = cards.get(value - zahler1);
                if (card != null) {
                    return card;
                }
            }
            zahler1++;
            System.out.println("Zähler: " + zahler1 + " || " + cards.size());
        }
        return card;
    }
    public Card getRandomCard(ArrayList<Card> cardList) {
        Card card = null;
        int tries = 0;
        while (card == null) {
            card = AllCards.getInstance().randomcard(cardList);
            tries++;
        }
        System.out.println("Random Card("+tries+"): "+card.getCardname()+" "+card.getCardnummer());
        return card;
    }
}


