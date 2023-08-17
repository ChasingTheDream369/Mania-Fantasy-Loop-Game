package unsw.loopmania;

import java.io.File;

import javafx.scene.image.Image;

public class Slug extends Enemy implements EnemyStrategy {

    private static int speed = 5;
    private GainsOnDefeat gains;

    public Slug(PathPosition position){
        super(position);
        setSpeed(speed);
        setType("Slug");
        setHealth(15);
        setDamage(1.5);
        setBattleRadius(1.2);
        setSupportRadius(1.2);
        setExperience(5);
        int rand = (int)Math.floor(Math.random()*(5-0+0)+0);
        int rand1 = (int)Math.floor(Math.random()*(7-0+0)+0);
        this.gains = new GainsOnDefeat(5, 5, items[rand], cards[rand1]);
    }

    public void enemyAttack(Players player) {
        updateAttackNum();
        if (this.getHealth() > 0) {
            attack(player);
        }
    }

    public GainsOnDefeat getGainsOnDefeat(){
        return gains;
    }

    
    

}
