package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import loader.AllCards;
import loader.GeneralCardLoader;
import loader.HandCardLoader;
import loader.HeroLoader;
import objects.Card;

public class HubController {

    int cardposition = 0;
    public GridPane herocard = null;

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
    private static HubController instance;


    public void initialize() {
        instance = this;
        AllCards.getInstance().loadCards();
        draw1.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Card card = AllCards.getInstance().getRandomCard();
            HandCardLoader.getInstance().addCard(card);
            HandCardLoader.getInstance().addINSTCard(card, GeneralCardLoader.loadHandCard(card, cardbox));
        });
    }
    public static HubController getInstance() {
        return instance;
    }

    public void addCardToHbox(Card card) {
        if (cardposition <= 4) {
            GridPane pane = GeneralCardLoader.loadHandCard(card, cardbox);
            HandCardLoader.getInstance().addINSTCard(card, pane);

            cardbox.add(pane, cardposition, 0);
            cardposition++;
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
