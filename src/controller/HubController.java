package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import loader.*;
import objects.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class HubController {

    int cardposition = 0;
    private ObservableMap<Integer, Boolean> cardboxplaces = FXCollections.observableHashMap();

    public GridPane getCardbox() {
        return cardbox;
    }

    public ObservableMap<Integer, Boolean> getCardboxPos() {
        return cardboxplaces;
    }

    @FXML
    private GridPane cardbox;

    @FXML
    private BorderPane hero1;

    @FXML
    private BorderPane hero2;

    @FXML
    private BorderPane draw1;

    @FXML
    private BorderPane draw2;

    @FXML
    private BorderPane grave;

    public Label getSpieler() {
        return spieler;
    }

    public void setSpieler(String spieler) {
        Platform.runLater(() -> this.spieler.setText(spieler));
    }

    @FXML
    private Label spieler;

    public Label getSpielerdran() {
        return spielerdran;
    }

    public void sendCardToGraveyard(GridPane pane, Card card) {
        Platform.runLater(() -> {
            grave.setCenter(null);
            grave.setCenter(pane);
            if (HeroLoader.getInstance().getEnemyherocard() != null && HeroLoader.getInstance().getEnemyherocard().equals(card)) {
                HeroLoader.getInstance().setEnemyherocard(null, null);
                removeEnemycard();
            } else if (HeroLoader.getInstance().getHerocard() != null && HeroLoader.getInstance().getHerocard().equals(card)) {
                HeroLoader.getInstance().setHerocard(null, null);
                removeHeroCard();
            }

        });
    }

    public void setEnemyCard(GridPane pane) {
        Platform.runLater(() -> hero2.setCenter(pane));
    }

    public void removeEnemycard() {
        Platform.runLater(() -> hero2.setCenter(null));
    }

    public void setSpielerdran(String spielerdran) {
        Platform.runLater(() -> this.spielerdran.setText(spielerdran));
    }

    @FXML
    private Label spielerdran;

    private static HubController instance;

    public static void destroy() {
        instance = null;
    }

    public void initialize() {
        instance = this;
        AllCards.getInstance().loadCards();
        cardboxplaces.put(0, true);
        cardboxplaces.put(1, true);
        cardboxplaces.put(2, true);
        cardboxplaces.put(3, true);
        draw1.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (GameLoader.getInstance().isIstamzug() && (RoundLoader.getInstance().getCardcounter() < 1)) {
                    HashMap<Integer, Card> cards = new HashMap<>();
                    cards.putAll(AllCards.getInstance().getSpielCards());
                    boolean found = false;
                    while (!found) {
                        Card card = AllCards.getInstance().getRandomCard(new ArrayList<>(cards.values()));
                        GridPane pane = GeneralCardLoader.loadHandCard(card, cardbox);
                        if (addCardToHbox(pane, card)) {
                            found = true;
                            RoundLoader.getInstance().setCardcounter(RoundLoader.getInstance().getCardcounter() + 1);
                            AllCards.getInstance().getSpielCards().remove(card.getUniqueNumber());
                        }
                    }
                } else {
                    MainController.getInstance().setStatus("Keine Karten mehr auf dem Stapel");
                    RoundLoader.getInstance().setCardcounter(RoundLoader.getInstance().getCardcounter() + 1);
                }
        });
        if (GameLoader.getInstance().getSpielerid() == 1) {
            setSpieler("Spieler 1");
        } else if (GameLoader.getInstance().getSpielerid() == 2) {
            setSpieler("Spieler 2");
        }
    }

    public static HubController getInstance() {
        return instance;
    }

    public boolean addCardToHbox(GridPane pane, Card card) {
        if (cardboxplaces.values().contains(true) && !HandCardLoader.getInstance().getAllHandcardsVa().contains(card)) {
            for (int position : cardboxplaces.keySet()) {
                if (cardboxplaces.get(position).equals(true)) {
                    HandCardLoader.getInstance().addCard(card);
                    HandCardLoader.getInstance().addINSTCard(card, pane);
                    cardbox.add(pane, position, 0);
                    cardboxplaces.put(position, false);
                    card.setPosition(position);
                    AllCards.getInstance().removePlayCard(card);
                    return true;
                }
            }
        } else {
            MainController.getInstance().setStatus("Es kann keine Karte gezogen werden");
            return true;
        }
        return false;
    }

    public void removeFromHBox(GridPane pane) {
        cardbox.getChildren().removeAll(pane);
    }

    public void setHeroCard(GridPane pane) {
        hero1.setCenter(null);
        hero1.setCenter(pane);
    }
    public void removeHeroCard() {
        hero1.setCenter(null);
    }

}
