package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import unsw.loopmania.Character;;

public class TestingUnequippedInventory {
    @Test
    public void testingFullInventory() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();
        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);

        Staff staff = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedItem(staff);

        c.setEquippedItems(staff);

        assertEquals(c.getEquippedItems().get(0).getType(), "Staff");
        c.removeEquippedItems(staff);

        assertEquals(c.getEquippedItems().size(), 0);

        Armour armour = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedItem(armour);

        c.setEquippedItems(armour);

        assertEquals(c.getEquippedItems().get(0).getType(), "Armour");

        
        for (int i = 0; i < 14; i++) {
            armour = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            world.addUnequippedItem(armour);
        }

        armour = new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedItem(armour);

        Helmet helmet = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedItem(helmet);

        assertEquals(world.getUnequippedItems().get(15).getType(), "Helmet");

        Shield shield = new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));

        world.addUnequippedItem(shield);

        assertEquals(world.getUnequippedItems().get(15).getType(), "Shield");

        staff = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedItem(staff);

        assertEquals(world.getUnequippedItems().get(15).getType(), "Staff");

        assertEquals(world.getEquippedInventory().size(), 0);
        
        world.equippedInventoryItemsNotify(armour, 1, 0);

        assertEquals(world.getEquippedInventory().size(), 1);

        world.removeUnequippedInventoryItemByCoordinates(1, 0);

        assertEquals(world.getUnequippedItems().size(), 15);

    }
}
