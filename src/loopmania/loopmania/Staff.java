package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import java.io.File;
import javafx.scene.image.Image;

public class Staff extends BasicItems implements WeaponStrategy{
    
    double damage = 1.0;
    double damageElan = 3.0;
    double damamgeDoggie = 2.0;
    // Haven't dealt with how long the trance is??.
    private int trance;
    private int numOfAttack;

    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        trance = (int)Math.floor(Math.random()*(5-2+1)+2);
        setDamage("Slug", damage);
        setDamage("Zombie", damage);
        setDamage("Vampire", damage);
        setDamage("ElanMuske", damageElan);
        setDamage("Doggie", damamgeDoggie);
        setType("Staff");
        setPrice(5.0);
    }

    @Override
    public double damageToEnemy(Enemy enemy) {
        updateAttackNum();
        if(numOfAttack%trance == 0){

            enemy.changeToAlliedSoldier();
        }
        return getDamage(enemy.getType());
        
    }
    
    private void updateAttackNum(){
        numOfAttack += 1;
    }
    
}
