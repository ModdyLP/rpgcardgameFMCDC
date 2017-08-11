package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import loader.GeneralCardLoader;
import loader.HandCardLoader;
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
