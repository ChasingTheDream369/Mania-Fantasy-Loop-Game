package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;

public class CampfireBuilding extends Buildings {

    private double supportRadius = 3.0;
    private static int activeCampfires = 0;
    private boolean isActive = false;
    //private static boolean damageBoosted = false;

    /**
     * Create a campfire building
     * @param x
     * @param y
     */
    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Campfire", x, y);
        //this.supportRadius = 5;
    }

    public double getSupportRadius() {
        return supportRadius;
    }

    /**
     * Returns all the campfires which have the character in their radius.
     * @return
     */
    public int getActiveCampfires() {
        return activeCampfires;
    }

    /**
     * Decreases the number of active campfires
     */
    public void decreaseActiveCampfires() {
        CampfireBuilding.activeCampfires --;
    }
    
    /**
     * Increases the number of active campfires
     */
    public void increaseActiveCampfires() {
        CampfireBuilding.activeCampfires ++;
    }

    /**
     * Activates the campfire
     * @param isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Boosts the charcater's attack when it is within radius of one campfire.
     * Also turns away all vampires from its radius.
     * @param character
     * @param enemies
     * @return list af all the enemies affected by its actions
     */
    public List<Enemy> performAction(Character character, List<Enemy> enemies) {
        if (Math.pow((character.getX() - getX()), 2) +  Math.pow((character.getY() - getY()), 2) < Math.pow(getSupportRadius(), 2)) {   

            if (getActiveCampfires() == 0) {
                increaseActiveCampfires();
                character.setDamage(character.getDamage() * 2);
                setIsActive(true);
            } 
        
        } else {
            if (getIsActive()) {
                setIsActive(false);
                decreaseActiveCampfires();
                character.setDamage(character.getDamage() / 2);
            }
        }
        
        for (Enemy e:enemies) {
            if (e.getType().equals("Vampire")) {
                ((Vampire) e).changeDirection(this);
            }
        }

        return enemies;
    }
}
