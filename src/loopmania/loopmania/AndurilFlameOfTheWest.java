package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import java.io.File;

//Rare items are a part of the world conifguration file.
public class AndurilFlameOfTheWest extends RareItems implements WeaponStrategy, DefenceStrategy, RefillStrategy  {

    private double basicEnemyDamage = 3.0;
    private double bossesDamage = 3*(basicEnemyDamage);
    
    /**
     * Creates Anduril Flame of the west
     * @param x
     * @param y
     */
    public AndurilFlameOfTheWest(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("AndurilFlameOfTheWest");
        setDamage("Slug", basicEnemyDamage);
        setDamage("Zombie", basicEnemyDamage);
        setDamage("Vampire", basicEnemyDamage);
        setDamage("Doggie", bossesDamage);
        setDamage("ElanMuske", bossesDamage);

        setDefence("Slug", 0.0);
        setDefence("Zombie", 0.0);
        setDefence("Vampire", 0.0);
        setDefence("Doggie", 0.0);
        setDefence ("ElanMuske", 0.0);
        setRefillable(false);
    }

    public double getBossesDamage() {
        return this.bossesDamage;
    }
}
