package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

import org.javatuples.Pair;
/**
 * a basic form of building in the world
 */
public class VampireCastleBuilding extends Buildings {

    
    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("vampiresCastleBuilding", x, y);
    }

    /**Spawns a vampire when the character reaches the first path tile in the world. and in the cycle is a 
     * modulus 5.
    */
    public Enemy spawn(int currentCycle, Character character, int pathPositionIndex, List<Pair<Integer, Integer>> orderedPath) {
        if (currentCycle % 5 == 0 && character.getX() == 0 && character.getY() == 0) {
            PathPosition pathPosition = new PathPosition(pathPositionIndex, orderedPath);
            Enemy enemy = new Vampire(pathPosition);
            return enemy;
        }

        return null;
    }
}
