package unsw.loopmania;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class Gold extends EssentialItems{

    public Gold(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("Gold");
    }

    /**
     * Character gets 5 gold points every he gets gold.
     */

    @Override
    public void essentialStrategy(Character character) {
        character.ReceiveGoldItems(5);
    }
    
}
