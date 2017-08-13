package objects;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class MainCard {


    private int cardnummer;
    private String cardname;
    private Type cardtype;
    private String description;
    private String fileurl;
    private int uniqueNumber;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position = 0;

    public int getCardnummer() {
        return cardnummer;
    }

    public void setCardnummer(int cardnummer) {
        this.cardnummer = cardnummer;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public Type getCardtype() {
        return cardtype;
    }

    public void setCardtype(Type cardtype) {
        this.cardtype = cardtype;
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(int uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }
}
