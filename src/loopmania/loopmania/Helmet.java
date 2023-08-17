package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class Helmet extends BasicItems implements DefenceStrategy {  

    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("Helmet");
        setDamage("Slug", 0.2);
        setDamage("Zombie", 0.3);
        setDamage("Vampire", 0.4);
        setDamage("ElanMuske", 0.5);
        setDamage("Doggie", 0.5);

        setPrice(5.0);
    }
    
    /**
     * defened against the attack of enemy.
     */

    @Override
    public double defendAgainstAttack(Enemy enemy) {
        return getDamage(enemy.getType());
    }

}   
