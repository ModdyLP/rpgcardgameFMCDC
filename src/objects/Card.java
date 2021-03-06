package objects;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public interface Card {
    String getFileurl();
    String getDescription();
    String getCardname();
    int getCardnummer();
    Type getCardtype();
    int getUniqueNumber();
    void setUniqueNumber(int number);
    int getPosition();
    void setPosition(int position);
}
