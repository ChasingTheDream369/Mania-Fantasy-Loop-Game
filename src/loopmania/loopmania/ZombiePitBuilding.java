package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;

import org.javatuples.Pair;

public class ZombiePitBuilding extends Buildings {

    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("ZombiePit", x, y);
    }

    /**Spawns a zombie when the character reaches the first path tile in the world. */
    public Enemy spawn(Character character, int pathPositionIndex, List<Pair<Integer, Integer>> orderedPath) {
        if (character.getX() == 0 && character.getY() == 0) {
            PathPosition pathPosition = new PathPosition(pathPositionIndex, orderedPath);
            Enemy enemy = new Zombie(pathPosition);
            return enemy;
        }

        return null;
    }
    
}
