package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a vampire castle card in the backend game world
 */
public class CampfireCard extends Card {
    
    /**
     * Creates a campfire card
     * @param x
     * @param y
     */
    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("CampfireCard");
    }    
}
