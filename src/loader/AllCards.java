package loader;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import objects.Card;
import objects.HeroCard;
import storage.MySQLConnector;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

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
        spielercards.put(cardnummer, card);
        cardnummer++;
    }

    public void removeCard(Card card) {
        cards.remove(card.getCardnummer());
    }

    public Card getRandomCard() {
        if (spielercards.size() != 0) {
            Card card = null;
            while (card == null) {
                int random = ThreadLocalRandom.current().nextInt(0, spielercards.size());
                card = spielercards.get(random);
            }
            System.out.println(spielercards.size()+"  "+card.getUniqueNumber());
            spielercards.remove(card.getUniqueNumber());
            return card;
        } else {
            return null;
        }
    }

    public void loadCards() {
        try {
            MainController.getInstance().setStatus("Loading Cards...");
            ResultSet rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM Karten");
            while (rs.next()) {
                System.out.print("##"+rs.getString("name")+"##");
                if (rs.getString("type").equals("HELD")) {
                    addCard(new HeroCard(rs.getInt("nr"), rs.getString("name"), rs.getString("bild") + ".png", "", rs.getInt("leben"), rs.getInt("verteidigung"), rs.getInt("angriff")));
                }
            }
        } catch (
                Exception ex)

        {
            ex.printStackTrace();
        } finally

        {
            MySQLConnector.getInstance().close();
        }
        MainController.getInstance().

                setDEFStatus();
    }

}


