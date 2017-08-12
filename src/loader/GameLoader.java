package loader;

import controller.MainController;
import javafx.concurrent.Task;
import storage.MySQLConnector;

import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class GameLoader {
    private boolean istamzug = false;
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
                setSpielerid();
                while(start) {
                    Thread.sleep(1000);
                    System.out.println(new Date(System.currentTimeMillis()).toString()+" Game Loop Trigger");
                }
                System.out.println("Task stopped");
                return null;
            }
        };
        Thread gamethread = new Thread(gametask);
        gamethread.start();
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
            while(set.next() && !set.isAfterLast()) {
                if (set.getInt("spieler1") != 0 && set.getInt("spieler2") == 0) {
                    MySQLConnector.getInstance().getResultofQuery("UPDATE game SET spieler2 = 200 WHERE id = 1");
                    spielerid = 2;
                } else if (set.getInt("spieler1") == 0 && set.getInt("spieler2") == 0){
                    MySQLConnector.getInstance().getResultofQuery("UPDATE game SET spieler1 = 200 WHERE id = 1");
                    spielerid = 1;
                } else {
                    MainController.getInstance().exit();
                }
            }
            MySQLConnector.getInstance().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}
