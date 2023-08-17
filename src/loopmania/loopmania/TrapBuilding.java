package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;
import java.util.List;

public class TrapBuilding extends Buildings{

    private double damage = 5.0;
    private boolean damageDone = false;

    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Trap", x, y);
    }

    public boolean getDamageDone() {
        return damageDone;
    }

    /*8 Attacks the enemy wehn the enemy crosses its position co-ordinates**/
    public void attackEnemy(Enemy enemy) {
            if ((enemy.getX() == this.getX() && enemy.getY() == this.getY())  && (!damageDone)) {
                enemy.setHealth(enemy.getHealth() - damage);
                damageDone = true;
            }
    }

    /**
     * performa the action and add the killed enemies..!!
     * @param enemies
     * @param killedEnemies
     * @return
     */
    public ArrayList<Enemy> performAction(List<Enemy> enemies, ArrayList<Enemy> killedEnemies) {
        for (Enemy enemy:enemies) {
            attackEnemy(enemy);
            if (enemy.getHealth() <= 0) {
                killedEnemies.add(enemy);
            }
        }

        return killedEnemies;
    }
}
