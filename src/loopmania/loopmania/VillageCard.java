package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;


/**
 * represents a vampire castle card in the backend game world
 */
public class VillageCard extends Card {
    
    public VillageCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("VillageCard");
    }    
}
