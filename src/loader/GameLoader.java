package loader;

import controller.HubController;
import controller.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.GridPane;
import objects.Card;
import objects.HeroCard;
import storage.MySQLConnector;
import utils.GeneralDialog;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
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

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    private boolean start = true;
    private static GameLoader loade;
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

    private void checkPlayer() {
        try {
            ResultSet set = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM game");
            while (set.next()) {
                if (set.getInt("spielerdran") != 0) {
                    HubController.getInstance().setSpielerdran("Spieler "+set.getInt("spielerdran")+" ist dran.");
                }
                if (set.getInt("spieler1") == 500 && spielerid == 2) {
                    MainController.getInstance().exit();
                } else if (set.getInt("spieler2") == 500 && spielerid == 1) {
                    MainController.getInstance().exit();
                }
                if (set.getInt("spieler1") == 200 && set.getInt("spieler2") == 200 && set.getInt("spielerdran") == 0) {
                    MySQLConnector.getInstance().execute("UPDATE game SET spielerdran = 1 WHERE id = 1");
                }
                if (set.getInt("spieler1") == 200 && set.getInt("spieler2") == 200 && set.getInt("spielerdran") != 0) {
                    if (set.getInt("spielerdran") == spielerid) {
                        istamzug = true;
                        HeroLoader.getInstance().loadEnemyCard();
                        HeroLoader.getInstance().loadHero();
                        HeroLoader.getInstance().checkIfCarddie();
                    }
                }
                if (set.getInt("spieler1") == 200 && set.getInt("spieler2") == 200) {
                    if (HandCardLoader.getInstance().getAllHandcards().size() == 0 && set.getInt("splitted") == 1) {
                        System.out.println("Lade Handkarten");
                        Platform.runLater(() -> {
                            AllCards.getInstance().loadStapelfromplayer();
                            HashMap<Integer, Card> cards = new HashMap<>();
                            cards.putAll(AllCards.getInstance().getSpielCards());
                            for (int i = 0; i < 2; i++) {
                                Card card = AllCards.getInstance().getRandomCard(new ArrayList<>(cards.values()));
                                if (card != null) {
                                    GridPane pane = GeneralCardLoader.loadHandCard(card, HubController.getInstance().getCardbox());
                                    HubController.getInstance().addCardToHbox(pane, card);
                                    AllCards.getInstance().removePlayCard(card);
                                } else {
                                    MainController.getInstance().setStatus("Keine Karten mehr auf dem Stapel");
                                }
                            }
                        });
                    }
                }
            }
            MySQLConnector.close(set);
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
            ResultSet set = MySQLConnector.getInstance().getResultofQuery("SELECT * FROM game");
            while (set != null && set.next()) {
                System.out.println("Spieler 1: "+set.getInt("spieler1")+" | Spieler2: "+set.getInt("spieler2"));
                if (set.getInt("spieler1") != 200) {
                    MySQLConnector.getInstance().execute("UPDATE game SET spieler1 = 200, spieler2 = 0 WHERE id = 1");
                    spielerid = 1;
                    HubController.getInstance().setSpieler("Du: Spieler 1");
                } else if (set.getInt("spieler2") != 200) {
                    MySQLConnector.getInstance().execute("UPDATE game SET spieler2 = 200 WHERE id = 1");
                    spielerid = 2;
                    HubController.getInstance().setSpieler("Du: Spieler 2");
                } else {
                    MainController.getInstance().exit();
                    System.out.println("Alle Spieler sind da");
                }
            }
            MySQLConnector.close(set);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void logout() {
        try {
            if (spielerid == 1) {
                MySQLConnector.getInstance().execute("UPDATE game SET spieler1 = 500, spielerdran = 0, splitted = 0 WHERE id = 1");
                MySQLConnector.getInstance().execute("UPDATE `Spieler1` SET nr = '0',name = '0', leben = '0'");
            } else if (spielerid == 2) {
                MySQLConnector.getInstance().execute("UPDATE game SET spieler2 = 500, spielerdran = 0, splitted = 0 WHERE id = 1");
                MySQLConnector.getInstance().execute("UPDATE `Spieler2` SET nr = '0',name = '0', leben = '0'");
            } else {
                System.out.println("No Player ID");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
