package loader;

import controller.HubController;
import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;
import objects.Type;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class HandCardLoader {
    private static HandCardLoader instance;
    private ObservableList<Card> handcards = FXCollections.observableArrayList();
    private ObservableList<GridPane> handcardsinstance = FXCollections.observableArrayList();

    public static HandCardLoader getInstance() {
        if (instance == null) {
            instance = new HandCardLoader();
        }
        return instance;
    }

    public ObservableList<Card> getAllHandcards() {
        return handcards;
    }

    public Card getHandCardbyID(int cardid) {
        return handcards.get(cardid);
    }
    public GridPane getHandCardINSTbyID(int cardid) {
        return handcardsinstance.get(cardid);
    }

    public void addCard(Card card) {
        handcards.add(card.getCardnummer(), card);
    }

    public void removeHandcard(int cardid) {
        handcards.remove(cardid);
    }

    public void addINSTCard(Card card, GridPane pane) {
        final int[] cardid = {0};
        if (card.getCardtype().equals(Type.HELD)) {
            final ContextMenu cm = new ContextMenu();
            MenuItem legen = new MenuItem("Karte legen");
            legen.setOnAction(e -> {
                if (HubController.getInstance().herocard == null) {
                    System.out.println("Karte gelegt: " + handcards.get(cardid[0]).getCardname());
                    removeHandcard(cardid[0]);
                    HeroLoader.getInstance().setHerocard(card, handcardsinstance.get(cardid[0]));
                    HubController.getInstance().setHeroCard(pane);
                    removeINSTHandcard(cardid[0]);
                } else {
                    MainController.getInstance().setStatus("Es ist bereits eine Karte gelegt");
                }
            });

            cm.getItems().add(legen);
            handcardsinstance.add(card.getCardnummer(), pane);
            pane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                for (int i = 0; i < handcardsinstance.size(); i++) {
                    if (handcardsinstance.get(i) != null && handcardsinstance.get(i).equals(pane)) {
                        System.out.println(handcards.get(i).getCardname());
                        cardid[0] = handcards.get(i).getCardnummer();
                        cm.show(pane, event.getScreenX(), event.getScreenY());
                    }
                }
            });
        }
    }

    public void removeINSTHandcard(int cardid) {
        handcardsinstance.remove(cardid);
    }

    public void loadCards() {
        handcards.add(0, new HeroCard(0, "Elfe Vorha", "exportpng/elfe.png", "Eine Elfe", 10, 15, 10));
        handcards.add(1, new HeroCard(1, "Vampir Dracula", "exportpng/vampire.png", "Ein Vampir", 10, 15, 10));
        handcards.add(2, new HeroCard(2, "Drache Ohnezahn", "exportpng/dragon.png", "Ein Drache", 10, 15, 10));

        handcardsinstance.addListener(new ListChangeListener<GridPane>() {
            @Override
            public void onChanged(Change<? extends GridPane> c) {
                c.next();
                for (GridPane pane : c.getRemoved()) {
                    HubController.getInstance().removeFromHBox(pane);
                }
            }
        });


    }


}
