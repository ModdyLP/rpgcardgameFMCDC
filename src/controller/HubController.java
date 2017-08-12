package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import loader.*;
import objects.Card;
import storage.MySQLConnector;

import java.util.ArrayList;
import java.util.HashMap;

public class HubController {

    int cardposition = 0;
    private ObservableMap<Integer, Boolean> cardboxplaces = FXCollections.observableHashMap();
    public GridPane herocard = null;

    public GridPane getCardbox() {
        return cardbox;
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
            if (GameLoader.getInstance().isIstamzug()) {
                HashMap<Integer, Card> cards = new HashMap<>();
                cards.putAll(AllCards.getInstance().getSpielCards());
                Card card = AllCards.getInstance().getRandomCard(new ArrayList<>(cards.values()));
                if (card != null) {
                    GridPane pane = GeneralCardLoader.loadHandCard(card, cardbox);
                    addCardToHbox(pane, card);
                } else {
                    MainController.getInstance().setStatus("Keine Karten mehr auf dem Stapel");
                }
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

    public void addCardToHbox(GridPane pane, Card card) {
        if (cardboxplaces.values().contains(true)) {
            for (int position : cardboxplaces.keySet()) {
                if (cardboxplaces.get(position).equals(true)) {
                    HandCardLoader.getInstance().addCard(card);
                    HandCardLoader.getInstance().addINSTCard(card, pane);
                    AllCards.getInstance().removePlayCard(card);
                    cardbox.add(pane, position, 0);
                    cardboxplaces.put(position, false);
                    break;
                }
            }
        } else {
            MainController.getInstance().setStatus("Es kann keine Karte gezogen werden");
        }
    }
    public void removeFromHBox(GridPane pane) {
       cardbox.getChildren().removeAll(pane);
    }
    public void setHeroCard(GridPane pane) {
        if (herocard == null) {
            hero1.setCenter(pane);
            herocard = pane;
        }
    }


}
