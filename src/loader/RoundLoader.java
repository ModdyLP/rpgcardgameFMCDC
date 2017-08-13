package loader;

import objects.HeroCard;
import storage.MySQLConnector;

/**
 * Created by ModdyLP on 12.08.2017. Website: https://moddylp.de/
 */
public class RoundLoader {

    private int cardcounter = 0;
    private int attackcounter = 0;

    private static RoundLoader instance;

    public int getCardcounter() {
        return cardcounter;
    }

    public void setCardcounter(int cardcounter) {
        this.cardcounter = cardcounter;
    }

    public int getAttackcounter() {
        return attackcounter;
    }

    public void setAttackcounter(int attackcounter) {
        this.attackcounter = attackcounter;
    }

    public void checkRoundOver() {
        System.out.println(getCardcounter()+"   "+getAttackcounter());
        if (getCardcounter() == 1 && getAttackcounter() == 1) {
            GameLoader.getInstance().setIstamzug(false);
            if (GameLoader.getInstance().getSpielerid() == 1) {
                MySQLConnector.getInstance().execute("UPDATE game SET spielerdran = 2 WHERE id = 1");
            } else if (GameLoader.getInstance().getSpielerid() == 2) {
                MySQLConnector.getInstance().execute("UPDATE game SET spielerdran = 1 WHERE id = 1");
            }
            cardcounter = 0;
            attackcounter = 0;
            HeroLoader.getInstance().loadEnemyCard();
        }
    }

    public static RoundLoader getInstance() {
        if (instance == null) {
            instance = new RoundLoader();
        }
        return instance;
    }

}
