package unsw.loopmania;

import java.util.Random;

/**
 * a basic form of enemy in the world
 */
public class BasicEnemy extends MovingEntity {
    public BasicEnemy(PathPosition position) {
        super(position);
    }

    /**
     * move the enemy
     */
    public void move(){
        int directionChoice = (new Random()).nextInt(2);
        if (directionChoice == 0){
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
        }
    }
}
