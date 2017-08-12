package loader;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import objects.Card;
import objects.HeroCard;
import storage.MySQLConnector;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
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
            ResultSet rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM Karten");
            while (rs.next()) {
                if (rs.getString("type").equals("HELD")) {
                    System.out.print(rs.getString("name")+"\n");
                    addCard(new HeroCard(rs.getInt("nr"), rs.getString("name"), rs.getString("bild") + ".png", rs.getString("beschreibung"), rs.getInt("leben"), rs.getInt("verteidigung"), rs.getInt("angriff")));
                }
            }
            MySQLConnector.close(rs);
        } catch (
                Exception ex)

        {
            ex.printStackTrace();
        }
        MainController.getInstance().

                setDEFStatus();
    }
    public void splitupCards() {
        try {
            MainController.getInstance().setStatus("Verteile Karten");
            MySQLConnector.getInstance().execute("DELETE FROM cardtoplayer WHERE 1=1");
            int id = 1;
            int zahler = 0;
            HashMap<Integer, Card> splitupcards = new HashMap<>();
            ArrayList<Integer> player1 = new ArrayList<>();
            ArrayList<Integer> player2 = new ArrayList<>();
            splitupcards.putAll(cards);
            while (splitupcards.size() > 0) {
                Card card = getRandomCard(new ArrayList<>(splitupcards.values()));
                System.out.println("Verteile Karte(" + id + "): " + card.getCardnummer());
                if (id == 1) {
                    player1.add(card.getCardnummer());
                    id = 2;
                } else if (id == 2) {
                    player2.add(card.getCardnummer());
                    id = 1;
                }
                splitupcards.remove(card.getUniqueNumber());
            }
            MySQLConnector.getInstance().insertMany(player1, 1);
            MySQLConnector.getInstance().insertMany(player2, 2);
            MainController.getInstance().setStatus("Karten wurden verteilt");
            MySQLConnector.getInstance().execute("UPDATE game SET splitted = 1 WHERE id = 1");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void loadStapelfromplayer() {
        try {
            ResultSet rs = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM cardtoplayer WHERE playerid = " + GameLoader.getInstance().getSpielerid() + ";");
            while (rs.next()) {
                Card card = getCardbyID(rs.getInt("cardid"));
                addPlayCard(card);
            }
            MySQLConnector.close(rs);
            System.out.println("Stapelgröße: "+spielercards.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MainController.getInstance().setStatus("Stapel geladen");
    }
    public Card getCardbyID(int cardid) {
        for (Card card: cards.values()) {
            if (card.getCardnummer() == cardid) {
                return card;
            }
        }
        return null;
    }
    public Card getCardbyUID(int cardid) {
        for (Card card: cards.values()) {
            if (card.getUniqueNumber() == cardid) {
                return card;
            }
        }
        return null;
    }

    public ObservableMap<Integer, Card> getSpielCards() {
        return spielercards;
    }

    public Card getRandomCard(ArrayList<Card> cards) {
        Random rand = new Random();
        int value = 0;
        if (cards.size() == 1) {
            value = rand.nextInt(cards.size());
        } else if (cards.size() < 1) {
            return null;
        } else {
            value = rand.nextInt(cards.size()-1);
        }

        Card card = cards.get(value);
        int zahler1 = 1;
        while (card == null) {
            if ((value-zahler1) < cards.size()-1) {
                card = cards.get(value-zahler1);
                if (card != null) {
                    return card;
                }
            }
            if ((value-zahler1) > 0){
                card = cards.get(value-zahler1);
                if (card != null) {
                    return card;
                }
            }
            zahler1++;
            System.out.println("Zähler: "+zahler1+" || "+cards.size());
        }
        return card;
    }
}


