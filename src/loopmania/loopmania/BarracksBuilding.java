package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;


import java.util.List;


public class BarracksBuilding extends Buildings {

    /**
     * Creates a barracaks building
     * @param x x position in the world
     * @param y y position in the world
     */
    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Barracks", x, y);
    }
    
    
    /** 
     * Checks whether the character is at the same position as the barracks
     * @return boolean
     * @param charcater
     */

    public boolean toSpawn(Character c) {
        if (c.getX() == this.getX() && c.getY() == this.getY()) {
            return true;
        }
        return false;
    }

    
    /** 
     * @param character
     * @param index the path number where the allied soldier will spawn
     * @param alliedSoldiers
     * @param orderedPath
     * @return List<AlliedSoldier>
     */
    public List<AlliedSoldier> performAction(Character character, int index, List<AlliedSoldier> alliedSoldiers, List<Pair<Integer, Integer>> orderedPath) {
        if (toSpawn(character)) {
            AlliedSoldier alliedSoldier = new AlliedSoldier(new PathPosition(index, orderedPath));
            alliedSoldiers.add(alliedSoldier);
        }

        return alliedSoldiers;
    }
}
