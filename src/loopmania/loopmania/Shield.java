package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class Shield extends BasicItems implements DefenceStrategy {
    
    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamage("Slug", 0.1);
        setDamage("Zombie", 0.2);
        setDamage("Vampire", 0.3);
        setDamage("ElanMuske", 0.6);
        setDamage("Doggie", 0.5);
        setType("Shield");
        setPrice(7.0);
    }
    
    @Override
    public double defendAgainstAttack(Enemy enemy) {
        //Need to add this in the battles..!!
        if (enemy.getType().equals("Vampire")) {
            Vampire v = (Vampire) enemy;
            v.updateCrticalBite(0.6);
        }
        return getDamage(enemy.getType());
        
    }

}
