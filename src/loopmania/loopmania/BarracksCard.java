package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a vampire castle card in the backend game world
 */
public class BarracksCard extends Card {
    
    /**
     * Creates a barrack card
     * @param x
     * @param y
     */
    public BarracksCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("BarracksCard");
    }    
}
