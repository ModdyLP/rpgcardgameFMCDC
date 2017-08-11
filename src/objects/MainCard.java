package objects;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class MainCard {
    private String cardname;
    private Type cardtype;
    private String description;
    private String fileurl;

    
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
}
