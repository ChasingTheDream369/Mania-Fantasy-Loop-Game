package unsw.loopmania;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * represents a vampire castle card in the backend game world
 */
public class ZombiePitCard extends Card {
    
    public ZombiePitCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("ZombiePitCard");
    }   
}
