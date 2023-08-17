package unsw.loopmania;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends BasicItems implements WeaponStrategy {
    
    private final double damage = 3.0;

    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamage("Slug", damage);
        setDamage("Zombie", damage);
        setDamage("Vampire", damage);
        setDamage("Doggie", damage);
        setDamage("ElanMuske", damage);
        setType("Sword");
        setPrice(5.0);
    }
    
    @Override
    public double damageToEnemy(Enemy enemy) {
        return getDamage(enemy.getType());
    }

}
