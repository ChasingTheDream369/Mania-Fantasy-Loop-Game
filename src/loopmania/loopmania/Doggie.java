package unsw.loopmania;

import java.io.File;

import javafx.scene.image.Image;

public class Doggie extends Enemy implements EnemyStrategy{

    private GainsOnDefeat gains;

    /**
     * creates doggie boss at position position
     * @param position
     */
    public Doggie(PathPosition position) {
        super(position);
        setHealth(30);
        setDamage(5);
        setAlive(true);
        setBattleRadius(1.2);
        setSupportRadius(1.2);
        setType("Doggie");

        int rand = (int)Math.floor(Math.random()*(5-0+0)+0);
        int rand1 = (int)Math.floor(Math.random()*(7-0+0)+0);
        this.gains = new GainsOnDefeat(50, 5, items[rand], cards[rand1]);
    }

    /**
     * Doggie attack against character and allied soldiers
     * @param player
     */
    @Override
    public void enemyAttack(Players player) {
        updateAttackNum();
        if (this.getHealth() > 0) {
            attack(player);
        }
    }

    /**
     * Reward for defeating Doggie
     * @return GainsOnDefeat
     */
    @Override
    public GainsOnDefeat getGainsOnDefeat() {
        return gains;
    }
    
}
