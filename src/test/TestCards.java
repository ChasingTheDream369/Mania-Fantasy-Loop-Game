package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.Test;
import org.javatuples.Pair;
import unsw.loopmania.*;
import unsw.loopmania.Character;


public class TestCards {


    /**
     * Assure the creation and deleting of cards is
     * done correctly
     */
    @Test
    public void testCardCreation() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(1, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        // Check that CampfireCard is being Created correctly
        tw.createCampfireCard(world);
        assertEquals(world.getCards().size(), 1);

        // Check that card is removed upon creating building
        world.convertCardToBuildingByCoordinates("CampfireCard", 0, 0, 1, 1);
        assertEquals(world.getCards().size(), 0);

        // Check that TrapCard is being Created correctly
        tw.createTrapCard(world);
        assertEquals(world.getCards().size(), 1);

        // Check that card is removed upon creating building
        world.convertCardToBuildingByCoordinates("TrapCard", 0, 0, 0, 1);
        assertEquals(world.getCards().size(), 0);

        // Check that VampireCastleCard is being Created correctly
        tw.createVampireCastleCard(world);
        assertEquals(world.getCards().size(), 1);

        // Check that card is removed upon creating building
        world.convertCardToBuildingByCoordinates("VampireCastleCard", 0, 0, 1, 1);
        assertEquals(world.getCards().size(), 0);

        // Check that ZombiePit card is being Created correctly
        tw.createZombiePitCard(world);
        assertEquals(world.getCards().size(), 1);

        // Check that card is removed upon creating building
        world.convertCardToBuildingByCoordinates("ZombiePitCard", 0, 0, 1, 1);
        assertEquals(world.getCards().size(), 0);

        // Check that Tower card is being Created correctly
        tw.createTowerCard(world);
        assertEquals(world.getCards().size(), 1);

        // Check that Village card is removed upon creating building
        world.convertCardToBuildingByCoordinates("TowerCard", 0, 0, 1, 1);
        assertEquals(world.getCards().size(), 0);

        // Check that card is being Created correctly
        tw.createTrapCard(world);
        assertEquals(world.getCards().size(), 1);

        // Check that Barracks card is removed upon creating building
        world.convertCardToBuildingByCoordinates("TrapCard", 0, 0, 0, 1);
        assertEquals(world.getCards().size(), 0);
    }

    @Test
    public void testCardInventoryFull() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(1, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);
        
        // Load Tower Card and check that the first card in the world is tower card
        tw.createTowerCard(world);
        Card card = world.getCards().get(0);
        assertEquals(card.getType(), "TowerCard");

        // Make inventory Full
        int width = world.getWidth();
        for (int i = 0; i < width - 1; i++) {
            tw.createTrapCard(world);
        }  
        assertEquals(world.getCards().size(), 18);
        assertEquals(c.getGold(), 15);
        
        // Check inventory capped at world width and the first card removed
        tw.createTrapCard(world);
        assertEquals(world.getCards().size(), 18);
        assertNotEquals(world.getCards().get(0).getType(), "TowerCard");
        
        //Get 5 gold as Reward
        assertEquals(c.getGold(), 20);

        tw.createTowerCard(world);
        tw.createBarracksCard(world);
        tw.createCampfireCard(world);
        tw.createVampireCastleCard(world);
        tw.createVillageCard(world);
        tw.createZombiePitCard(world);
    }
}