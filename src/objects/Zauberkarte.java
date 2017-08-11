package objects;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class Zauberkarte extends MainCard implements Card {
    private ZauberMode mode;

    public Zauberkarte(ZauberMode mode, ZauberTarget target, int amount) {
        this.mode = mode;
        this.target = target;
        this.amount = amount;
    }

    private ZauberTarget target;
    private int amount;

    public ZauberMode getMode() {
        return mode;
    }

    public void setMode(ZauberMode mode) {
        this.mode = mode;
    }

    public ZauberTarget getTarget() {
        return target;
    }

    public void setTarget(ZauberTarget target) {
        this.target = target;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



}
