package loader;

import controller.HubController;
import controller.LobbyController;
import controller.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.Lobby;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import storage.MongoDBConnector;
import utils.GeneralDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class GameLoader {
    private boolean istamzug = false;

    public String getSpielerid() {
        return spielerid;
    }

    public String spielerid = "";
    public String spielername = "";
    public String enemyspielerid = "";
    public String enemyspielername = "";
    public boolean player1 = false;
    public Lobby selectedlobby = null;
    public boolean loaded = false;
    public boolean login = false;

    public boolean authorized = false;

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
    }

    public void sendData(JSONObject jsonObject) {
        if (player1 && GameLoader.getInstance().selectedlobby != null) {
            jsonObject.put("client1", getSpielerid());
        } else if (!player1 && GameLoader.getInstance().selectedlobby != null){
            jsonObject.put("client2", getSpielerid());
        } else {
            System.out.println("Fehler");
        }
        jsonObject.put("lobbyid", selectedlobby.getUuid());
        jsonObject.put("kartegelegt",RoundLoader.getInstance().getCardcounter());
        jsonObject.put("karteattack", RoundLoader.getInstance().getAttackcounter());
        jsonObject.put("playerisdran", isIstamzug());
    }

    public void checkPlayer() {
        try {
            ArrayList<Document> documents = MongoDBConnector.getInstance().getCollectionAsList("Game");
            for (Document doc : documents) {
                if (!doc.getString("current").equals("")) {
                    HubController.getInstance().setSpielerdran("Spieler " + doc.getInteger("current") + " ist dran.");
                }
                if (doc.getInteger("player1") == 500 || doc.getInteger("player2") == 500) {
                    System.out.println("Player left the Lobby");
                    logout();
                }
                if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200 && !doc.containsKey("current")) {
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                            new Document("$set", new Document("current", spielerid)));
                }
                if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200 && doc.containsKey("current")) {
                    if (!doc.getString("current").equals("") && doc.getString("current").equals(spielerid) && !istamzug) {
                        istamzug = true;
                        HeroLoader.getInstance().loadEnemyCard();
                        HeroLoader.getInstance().loadHero();
                    }
                    HeroLoader.getInstance().checkIfCarddie();
                }
                if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200) {
                    if (doc.getInteger("splitted") == 0) {
                        AllCards.getInstance().splitupCards();
                    }
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
            ArrayList<Document> documents = MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").find(new Document("_id", new ObjectId(selectedlobby.getUuid()))).into(new ArrayList<>());
            for (Document doc : documents) {
                System.out.println("Spieler 1: " + doc.getInteger("player1") + " | Spieler2: " + doc.getInteger("player2"));
                if (doc.getInteger("player1") != 200 && doc.getInteger("player2") != 200) {
                    player1 = true;
                    MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                            new Document("$set", new Document("owner", spielerid)));
                } else if (doc.getInteger("player1") == 200 && doc.getInteger("player2") == 200) {
                    logout();
                    System.out.println("Lobby ist voll");
                }
                System.out.println("Player 1: "+player1);
            }
            updateLobby();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void updateLobby() {
        if (player1) {
            MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                    new Document("$set", new Document("player1", 200).append("player2", 0).append("player1name", spielerid)));
        } else {
            MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                    new Document("$set", new Document("player2", 200).append("player2name", spielerid)));
        }
    }

    public void logout() {
        try {
            if (start) {
                MainController.getInstance().setStatus("Logout");
                if (selectedlobby != null) {
                    if (player1) {
                        MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                                new Document("$set", new Document("player1", 500).append("current", "").append("splitted", 0)));
                        MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                                new Document("$unset", new Document("player1herocard", "").append("player1name", 0).append("player1herocardleben", "")));
                    } else {
                        MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                                new Document("$set", new Document("player2", 500).append("current", "").append("splitted", 0)));
                        MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(selectedlobby.getUuid())),
                                new Document("$unset", new Document("player2herocard", "").append("player2name", 0).append("player2herocardleben", "")));
                    }
                }
                GeneralDialog.logout();
                Platform.runLater(() -> {
                    if (MainController.getInstance().pane != null) {
                        Platform.runLater(() -> LobbyController.getInstance().lobbylist.getItems().clear());
                        MainController.getInstance().reset();
                        HubController.destroy();
                    }
                    setStart(false);
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
