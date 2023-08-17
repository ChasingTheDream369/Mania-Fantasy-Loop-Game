package test;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.PathTile;
import unsw.loopmania.Subgoals;
import unsw.loopmania.Goal;
import unsw.loopmania.GoalCondtions;
import unsw.loopmania.Helmet;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.Zombie;
import unsw.loopmania.Vampire;
import unsw.loopmania.Slug;
import unsw.loopmania.Enemy;
import unsw.loopmania.Shield;
import unsw.loopmania.RareItems;
import unsw.loopmania.TheOneRing;

public class TestTheOneRing {
    
    TestingWorld tw = new TestingWorld();
    LoopManiaWorld world =  tw.world();

    List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();

    @Test
    public void TestIsRareItem() {
        TestRareItems rareItemsTest = new TestRareItems();
        rareItemsTest.runRareItemsUnitTests();
    }

    @Test
    public void TestTheOneRingCharacterHealth() {
        
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        Enemy e = new Zombie(pathPosition1);
        assertEquals(e.getHealth(), 17);
        world.addEnemy(e);

        // So this will instancoate by finding the slot in the unequipped rare items 
        // invenotry and then add the One ring in the unequipped rare items inventory.
        world.addTheOneRing();

        // Sets the characters health to zero and looks if we have oneing in the world and then 
        // refiils the characters health again to full.
        world.GameOver();
        
        assertEquals(100, c.getFullHealth());
        assertEquals(c.getFullHealth(), c.getHealth());
    }
       

}

