package unsw.loopmania;


import javafx.beans.property.SimpleIntegerProperty;

public class Armour extends BasicItems implements DefenceStrategy {
    
    /**
     * Creates an armour
     * @param x
     * @param y
     */
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamage("Slug", 0.5);
        setDamage("Zombie", 0.5);
        setDamage("Vampire", 0.5);
        setDamage("ElanMuske", 0.5);
        setDamage("Doggie", 0.5);
        setType("Armour");
        setPrice(7.0);
    }
    
    
    /** 
     * Returns the damage,the given enemy does against armour
     * @param enemy
     * @return double
     */
    @Override
    public double defendAgainstAttack(Enemy enemy) {
        return getDamage(enemy.getType());
    }
}
