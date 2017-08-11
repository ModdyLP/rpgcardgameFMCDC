package loader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Card;
import objects.HeroCard;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class AllCards {
    private static AllCards instance;

    private ObservableList<Card> cards  = FXCollections.observableArrayList();
    private ObservableList<Card> spielercards  = FXCollections.observableArrayList();

    public static AllCards getInstance() {
        if (instance == null) {
            instance = new AllCards();
        }
        return instance;
    }
    public ObservableList<Card> getCards() {
        return cards;
    }

    public void setCards(ObservableList<Card> cards) {
        this.cards = cards;
    }
    public void addCard(Card card) {
        cards.add(card.getCardnummer(), card);
    }
    public void removeCard(Card card) {
        cards.remove(card.getCardnummer());
    }
    public Card getRandomCard() {
        if (spielercards.size() != 0) {
            Card card = spielercards.get((int) (Math.random() * spielercards.size()));
            spielercards.remove(card);
            return card;
        } else {
            return null;
        }
    }
    public void loadCards() {
        addCard(new HeroCard(0, "Elfe Vorha", "exportpng/elfe.png", "Eine Elfe", 10, 15, 10));
        addCard(new HeroCard(1, "Vampir Dracula", "exportpng/vampire.png", "Ein Vampir", 10, 15, 10));
        addCard(new HeroCard(2, "Drache Ohnezahn", "exportpng/dragon.png", "Ein Drache", 10, 15, 10));

        spielercards.addAll(getCards());
    }





}
