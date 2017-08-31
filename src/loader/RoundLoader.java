package loader;

import org.bson.Document;
import org.bson.types.ObjectId;
import storage.MongoDBConnector;

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

    public boolean checkRoundOver() {
        if (getCardcounter() == 1 && getAttackcounter() == 1) {
            GameLoader.getInstance().setIstamzug(false);
            if (GameLoader.getInstance().player1) {
                MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(GameLoader.getInstance().selectedlobby.getUuid())),
                        new Document("$set", new Document("current", 2)));
            } else {
                MongoDBConnector.getInstance().getMongoDatabase().getCollection("Game").updateOne(new Document("_id", new ObjectId(GameLoader.getInstance().selectedlobby.getUuid())),
                        new Document("$set", new Document("current", 1)));
            }
            cardcounter = 0;
            attackcounter = 0;
            return true;
        }
        return false;
    }

    public static RoundLoader getInstance() {
        if (instance == null) {
            instance = new RoundLoader();
        }
        return instance;
    }

}
