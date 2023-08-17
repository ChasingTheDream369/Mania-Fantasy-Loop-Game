package unsw.loopmania;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class HealthPotion extends EssentialItems implements EssentialItemsStrategy, PathItemStrategy {

    public HealthPotion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("HealthPotion");
        setPrice(8.0);
    }

    /**
     * Refills the health of the character.
     */

    @Override
    public void essentialStrategy(Character character) {
        character.refillHealth();
    }

    @Override
    public void pathItemAdd(Character character) {
        character.refillHealth();
    }
}
