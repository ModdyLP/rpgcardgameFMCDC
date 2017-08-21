package objects;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class HeroCard extends MainCard implements Card{
    private int livePoints;
    private int attackpoints;
    private int defendpoints;
    private int maxleben;

    public int getMaxleben() {
        return maxleben;
    }

    private void setMaxleben(int maxleben) {
        this.maxleben = maxleben;
    }

    public int getLivePoints() {
        return livePoints;
    }

    public void setLivePoints(int livePoints) {
        if (getController() != null) {
            getController().setLivepoints(livePoints, getCardnummer());
            this.livePoints = livePoints;
        } else {
            System.out.println("Controller is not set: "+getCardname());
        }
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

    public HeroCard(int cardnummer, String cardname, String imageurl, String description, int livePoints, int defendpoints, int attackpoints) {
        this.attackpoints = attackpoints;
        this.defendpoints = defendpoints;
        this.livePoints = livePoints;

        setCardnummer(cardnummer);
        setCardname(cardname);
        setFileurl(imageurl);
        setCardtype(Type.HELD);
        setDescription(description);
        setMaxleben(livePoints);
    }
}
