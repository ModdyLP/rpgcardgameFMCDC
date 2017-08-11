package loader;

import javafx.concurrent.Task;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class GameLoader {
    private boolean istamzug = false;
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
                while(start) {

                }
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




}
