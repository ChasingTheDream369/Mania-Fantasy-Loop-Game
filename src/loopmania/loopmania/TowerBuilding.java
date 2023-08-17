package unsw.loopmania;
import javafx.scene.image.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

public class TowerBuilding extends Buildings {

    private double battleRadius = 3.0;
    private double damage = 2.0;

    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Tower", x, y);
        //this.buildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
    }

    public void attackEnemy(Enemy enemy) {
        // If an enemy in its battle radius attack
        if (Math.pow((enemy.getX() - this.getX()), 2) +  Math.pow((enemy.getY() - this.getY()), 2) < Math.pow(battleRadius, 2)){
            enemy.setHealth(enemy.getHealth() - damage);
        }
        
    }

    /**
     * perform Action, 
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
