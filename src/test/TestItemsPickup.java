package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Character;
import unsw.loopmania.Gold;
import unsw.loopmania.HealthPotion;

public class TestItemsPickup {
    
    @Test
    public void testGoldPickup() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(1, world.getOrderedPath()));
        world.setCharacter(c);

        assertEquals(world.getGoldOnPath().size(), 0);

        List<Gold> goldSpawned = world.takeGold();

        assertTrue(goldSpawned.size() == 1 || goldSpawned.size() == 0);


        Gold g = new Gold(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        world.addGoldOnPath(g);

        //assertEquals(world.getGoldOnPath().size(), 1);

        List<Gold> goldSpawned1 = world.takeGold();
        
        //Old gold item destroyed new created
        assertTrue(goldSpawned1.size() == 1 || goldSpawned1.size() == 0);

    }

    @Test
    public void testHealthPotionPickup() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(1, world.getOrderedPath()));
        world.setCharacter(c);

        assertEquals(world.getHealthPotionOnPath().size(), 0);

        List<HealthPotion> hpSpawned = world.takeHealthPotions();

        assertTrue(hpSpawned.size() == 1 || hpSpawned.size() == 0);


        HealthPotion hp = new HealthPotion(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        world.addHealthPotionOnPath(hp);

        //assertEquals(world.getGoldOnPath().size(), 1);

        List<HealthPotion> hpSpawned1 = world.takeHealthPotions();
        
        //Old gold item destroyed new created
        assertTrue(hpSpawned1.size() == 1 || hpSpawned1.size() == 0);

    }
}
