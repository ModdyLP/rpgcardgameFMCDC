package loader;

import javafx.scene.layout.GridPane;
import objects.Card;

/**
 * Created by ModdyLP on 11.08.2017. Website: https://moddylp.de/
 */
public class HeroLoader {

    private static HeroLoader instance;
    private Card herocard;
    private Card enemyherocard;
    private GridPane herocardINST;
    private GridPane enemyherocardINST;

    public static HeroLoader getInstance() {
        if (instance == null) {
            instance = new HeroLoader();
        }
        return instance;
    }
    
    public Card getHerocard() {
        return herocard;
    }

    public void setHerocard(Card herocard, GridPane gridPane) {
        this.herocard = herocard;
        this.herocardINST = gridPane;
    }

    public Card getEnemyherocard() {
        return enemyherocard;
    }

    public void setEnemyherocard(Card enemyherocard, GridPane gridPane) {
        this.enemyherocard = enemyherocard;
        this.enemyherocardINST = gridPane;
    }
}
