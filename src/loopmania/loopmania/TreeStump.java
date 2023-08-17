package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import java.io.File;

/**Rare item with the specefic damage and defence points */
public class TreeStump extends RareItems{


    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("TreeStump");
        setDefence("Slug", 0.3);
        setDefence("Zombie", 0.3);
        setDefence("Vampire", 0.3);
        setDefence("Doggie", 0.8);
        setDefence("ElanMuske", 0.8);

        setDamage("Slug", 0.0);
        setDamage("Zombie", 0.0);
        setDamage("Vampire", 0.0);
        setDamage("Doggie", 0.0);
        setDamage ("ElanMuske", 0.0);

        setRefillable(false);
    }


 
}
