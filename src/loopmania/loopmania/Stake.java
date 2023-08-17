package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped stake in the backend world
 */
public class Stake extends BasicItems implements WeaponStrategy{

    // Possibly look at all the issues surrounding the if enemy type and removing 
    // the repititions.
    double damageSlugZombie = 3.0;
    double damageVampire = 5.0;

    // need to add in the assumption.
    double damgeElanMuske = 6.0;
    double damageDoggie = 5.0;

    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamage("Slug", damageSlugZombie);
        setDamage("Zombie", damageSlugZombie);
        setDamage("Vampire", damageVampire);
        setDamage("ElanMuske", damageSlugZombie);
        setDamage("Doggie", damageDoggie);
        setType("Stake");
        setPrice(7.0);
    }

    @Override
    public double damageToEnemy(Enemy enemy) {
        return getDamage(enemy.getType());
    }
}
