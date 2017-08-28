package loader;

import controller.HubController;
import controller.LobbyController;
import controller.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.GridPane;
import objects.Card;
import org.bson.Document;
import org.bson.types.ObjectId;
import storage.MongoDBConnector;
import utils.GeneralDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class GameLoader {
    private boolean istamzug = false;

    public int getSpielerid() {
        return spielerid;
    }

    private int spielerid = 0;

    public void setStart(boolean start) {
        this.start = start;
    }

    private boolean start = false;
    private static GameLoader instance;

    public static GameLoader getInstance() {
        if (instance == null) {
            instance = new GameLoader();
        }
        return instance;
    }


    public void gameloop() {
        Task<Void> gametask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    setSpielerid();
                    if (spielerid == 1) {
                        AllCards.getInstance().splitupCards();
                    }
                    while (start) {
                        Thread.sleep(1000);
                        checkPlayer();
                        RoundLoader.getInstance().checkRoundOver();
                    }
                    System.out.println("Task stopped");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };
        Thread gamethread = new Thread(gametask);
        gamethread.start();
    }

    public HashMap<String, String> sendData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("playerid", String.valueOf(GameLoader.getInstance().getSpielerid()));
        map.put("kartegelegt", String.valueOf(RoundLoader.getInstance().getCardcounter()));
        map.put("karteattack", String.valueOf(RoundLoader.getInstance().getAttackcounter()));
        map.put("playerisdran", String.valueOf(GameLoader.getInstance().isIstamzug()));
        return map;
    }

    private void checkPlayer() {
        try {
            ArrayList<Document> documents = MongoDBConnector.getInstance().getCollectionAsList("Game");
            for (Document doc : documents) {
                if (doc.getInteger("current") != 0) {
                    HubController.getInstance().setSpielerdran("Spieler " + doc.getInteger("current") + " ist dran.");
                }
                if (doc.getInteger("player1") == 500 && spielerid == 2) {
                    logout();
                } else if (doc.getInteger("player2") == 500 && spielerid == 1) {
                    logout();
                }
                if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200 && doc.getInteger("current") == 0) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$set", new Document("current", 1)));
                }
                if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200 && doc.getInteger("current") != 0) {
                    if (doc.getInteger("current") == spielerid && !istamzug) {
                        istamzug = true;
                        HeroLoader.getInstance().loadEnemyCard();
                        HeroLoader.getInstance().loadHero();
                    }
                    HeroLoader.getInstance().checkIfCarddie();
                }
                if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200) {
                    if (HandCardLoader.getInstance().getAllHandcards().size() == 0 && doc.getInteger("splitted") == 1) {
                        System.out.println("Lade Handkarten");
                        Platform.runLater(() -> {
                            AllCards.getInstance().loadStapelfromplayer();
                            HashMap<Integer, Card> cards = new HashMap<>();
                            cards.putAll(AllCards.getInstance().getSpielCards());
                            int counter = 0;
                            while (counter < 2) {
                                boolean found = false;
                                while (!found) {
                                    Card card = AllCards.getInstance().getRandomCard(new ArrayList<>(cards.values()));
                                    GridPane pane = GeneralCardLoader.loadHandCard(card, HubController.getInstance().getCardbox());
                                    if (HubController.getInstance().addCardToHbox(pane, card)) {
                                        found = true;
                                        counter++;
                                        AllCards.getInstance().getSpielCards().remove(card.getUniqueNumber());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public boolean isIstamzug() {
        return istamzug;
    }

    public void setIstamzug(boolean istamzug) {
        this.istamzug = istamzug;
    }

    public void setSpielerid() {
        try {
            ArrayList<Document> documents = MongoDBConnector.getInstance().getCollectionAsList("Game");
            for (Document doc : documents) {
                System.out.println("Spieler 1: " + doc.getInteger("player1") + " | Spieler2: " + doc.getInteger("player2"));
                if (doc.getInteger("player1") != 200) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$set", new Document("player1", 200).append("player2", 0)));
                    spielerid = 1;
                    HubController.getInstance().setSpieler("Du: Spieler 1");
                } else if (doc.getInteger("player2") != 200) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$set", new Document("player2", 200)));
                    spielerid = 2;
                    HubController.getInstance().setSpieler("Du: Spieler 2");
                } else {
                    logout();
                    System.out.println("Lobby ist voll");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void logout() {
        try {
            if (start) {
                MainController.getInstance().setStatus("Logout");
                if (spielerid == 1) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$set", new Document("player1", 500).append("current", 0).append("splitted", 0)));
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$unset", new Document("player1herocard", "").append("player1herocardleben", "")));
                } else if (spielerid == 2) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$set", new Document("player2", 500).append("current", 0).append("splitted", 0)));
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId("599550c53d6c00d66470145c")),
                            new Document("$unset", new Document("player2herocard", "").append("player2herocardleben", "")));
                } else {
                    System.out.println("Exit without tracked User ID");
                }
                GeneralDialog.logout();
                Platform.runLater(() -> {
                    if (MainController.getInstance().pane != null) {
                        MainController.getInstance().reset();
                        HubController.destroy();
                    }
                    GameLoader.getInstance().setStart(false);
                });
            }
            Platform.runLater(() -> LobbyController.getInstance().lobbylist.getItems().clear());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
