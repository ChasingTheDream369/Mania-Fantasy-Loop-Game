package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class VillageBuilding extends Buildings {

    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Village", x, y);
    }

    /**
     * Refills the character health when the cahracter crosses the building.
     * @param character
     */
    public void refillCharacterHealth(Character character) {
        if (character.getX() == this.getX() && character.getY() == this.getY()) {
            character.setHealth(100);
        }
    }
}
