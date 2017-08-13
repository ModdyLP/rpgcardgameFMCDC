package loader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import objects.Card;

/**
 * Created by ModdyLP on 12.08.2017. Website: https://moddylp.de/
 */
public class graveyardloader {
    private ObservableList<Card> cards  = FXCollections.observableArrayList();
    private ObservableList<GridPane> cardsINST  = FXCollections.observableArrayList();

    public static graveyardloader getInstance() {
        if (instance == null) {
            instance = new graveyardloader();
        }
        return instance;
    }


    public static graveyardloader instance;


    public GridPane getGraveyard() {
        return graveyard;
    }

    public void setGraveyard(GridPane graveyard) {
        this.graveyard = graveyard;
    }

    public Card getGravecard() {
        return gravecard;
    }

    public void setGravecard(Card gravecard) {
        this.gravecard = gravecard;
    }

    private GridPane graveyard;
    private Card gravecard;

}
