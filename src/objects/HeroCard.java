package objects;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class HeroCard extends MainCard {
    private int livePoints;
    private int attackpoints;
    private int defendpoints;

    public int getLivePoints() {
        return livePoints;
    }

    public void setLivePoints(int livePoints) {
        this.livePoints = livePoints;
    }

    public int getAttackpoints() {
        return attackpoints;
    }

    public void setAttackpoints(int attackpoints) {
        this.attackpoints = attackpoints;
    }

    public int getDefendpoints() {
        return defendpoints;
    }

    public void setDefendpoints(int defendpoints) {
        this.defendpoints = defendpoints;
    }
}