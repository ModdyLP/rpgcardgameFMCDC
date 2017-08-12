package loader;

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

    public static RoundLoader getInstance() {
        if (instance == null) {
            instance = new RoundLoader();
        }
        return instance;
    }

}
