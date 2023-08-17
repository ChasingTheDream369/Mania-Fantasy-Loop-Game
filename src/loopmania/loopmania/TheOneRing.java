package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import java.io.File;

//Rare items are a part of the world conifguration file.
public class TheOneRing extends RareItems {

    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("TheOneRing");
        setDamage("Slug", 0.0);
        setDamage("Zombie", 0.0);
        setDamage("Vampire", 0.0);
        setDamage("Doggie", 0.0);
        setDamage("ElanMuske", 0.0);

        setDefence("Slug", 0.0);
        setDefence("Zombie", 0.0);
        setDefence("Vampire", 0.0);
        setDefence("Doggie", 0.0);
        setDefence ("ElanMuske", 0.0);

        setRefillable(true);
    }

    
    
}
