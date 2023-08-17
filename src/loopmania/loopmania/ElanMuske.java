package unsw.loopmania;

import java.io.File;

import javafx.scene.image.Image;

public class ElanMuske extends Enemy {

    private GainsOnDefeat gains;

    /**
     * The Constructor for ElanMuske take PathPosition and 
     * have High damage and health and incrases the price
     * of DoggieCoin when Elan is Spawn and Decreases the Price 
     * when Elan is defeat.
     * @param position
     */

    public ElanMuske(PathPosition position) {
        super(position);
        setHealth(50);
        setDamage(7);
        setAlive(true);
        setBattleRadius(1.2);
        setSupportRadius(1.2);
        setType("ElanMuske");

        DoggieCoin dc = DoggieCoin.getDoggieCoin();
        if(dc != null){
            dc.increasePrice();
        }
        int rand = (int)Math.floor(Math.random()*(5-0+0)+0);
        int rand1 = (int)Math.floor(Math.random()*(7-0+0)+0);
        this.gains = new GainsOnDefeat(5, 5, items[rand], cards[rand1]);
    }

    /**
     * Attacking the player.
     */

    @Override
    public void enemyAttack(Players player) {
        updateAttackNum();
        if (this.getHealth() > 0) {
            attack(player);
        }      
    }

    @Override
    public GainsOnDefeat getGainsOnDefeat() {
        return gains;
    }

    /**
     * heal Other enemies when Elan spawns.
     * @param enemy
     */

    public void heal(Enemy enemy){
        
        if ((Math.pow((getX()-enemy.getX()), 2) +  Math.pow((getY()-enemy.getY()), 2) < Math.pow((enemy.getSupportRadius()), 2)) && (!enemy.isAlliedSoldier())){
            double currentHealth = enemy.getHealth();
            enemy.setHealth(currentHealth+5);
        }
        
    }
    
}
