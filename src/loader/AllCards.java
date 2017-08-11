package loader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Card;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class AllCards {
    private static AllCards instance;
    private ObservableList<Card> cards  = FXCollections.observableArrayList();

    public static AllCards getInstance() {
        if (instance == null) {
            instance = new AllCards();
        }
        return instance;
    }


}
