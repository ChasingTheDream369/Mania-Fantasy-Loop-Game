package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import unsw.loopmania.Character;

import org.javatuples.Pair;


public class TestBuildings {


    @Test
    public void testCampfire() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(1, orderedPath);

        //Test Placing Conditions
        // Path Tile
        assertEquals(world.buildingConditions("CampfireCard", pathPosition.getX().getValue(), pathPosition.getY().getValue()), false);
        // Placing On a non-path Tile not adjacent to the Path
        assertEquals(world.buildingConditions("CampfireCard", 9, 2), true);
        // Placing on a path Tile adjacent to the path
        assertEquals(world.buildingConditions("CampfireCard", 6, 3), true);

        // Testing Boosting stats
        Character c = new Character(pathPosition);
        assertEquals(c.getDamage(), 5);
        world.setCharacter(c);
        
        Buildings campfire1 = tw.createCampfireBuilding(world, 1, 1);
        world.buildingsRole();
        assertEquals(c.getDamage(), 10);

        Buildings campfire2 = tw.createCampfireBuilding(world, 1, 3);
        world.buildingsRole();

        //within radius of two cmpfires and damage stil 10
        assertEquals(c.getDamage(), 10);

        // Move the character to get out of campfire range
        for (int i = 0; i < 4; i++) {
            c.moveDownPath();
        }
        
        world.buildingsRole();
        assertEquals(c.getDamage(), 5);
    }

    @Test
    public void TestVampireRunsAwayFromCampfire() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        
        Character c = new Character(pathPosition);
        world.setCharacter(c);

        // Creating the campfire building
        Buildings campfire1 = tw.createCampfireBuilding(world, 1, 1);

        // Testing that vampire runs away from the campfire.
        Enemy enemy = new Vampire(new PathPosition(5, orderedPath));
        world.addEnemy(enemy);

        world.buildingsRole();
        assertEquals(((Vampire) enemy).getReversed(), false);
        assertEquals(((Vampire) enemy).getReverseDirection(), false);

        // Moving the enemy inside the campfire's radius
        enemy.move();
        world.buildingsRole();
        assertEquals(((Vampire) enemy).getReversed(), true);

        world.buildingsRole();
        assertEquals(((Vampire) enemy).getReversed(), true);

        enemy.move();
        enemy.move();
        enemy.move();
        world.buildingsRole();
        assertEquals(((Vampire) enemy).getReversed(), false);
    }

    @Test
    public void testVampireCastle() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(0, orderedPath);

        assertEquals(world.buildingConditions("VampireCastleCard", pathPosition.getX().getValue(), pathPosition.getY().getValue()), false);
        assertEquals(world.getCards().size(), 0);
        assertEquals(world.buildingConditions("VampireCastleCard", 1, 1), true);

        //Buildings vampCastle = tw.createVampireCastleBuilding(world, 1, 1);
        
        VampireCastleCard vcc = new VampireCastleCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(vcc);
        assertEquals(world.getCards().size(), 1);
        Buildings vampCastle = world.convertCardToBuildingByCoordinates("VampireCastleCard", 0, 0, 1, 1);

        assertEquals(world.getCards().size(), 0);
        Character c = new Character(pathPosition);
        world.setCharacter(c);

        List<Enemy> enemies;

        enemies = world.possiblySpawnEnemies(1);
        for (Enemy e : enemies) {
            assertNotEquals(e.getType(), "Vampire");
        }


        enemies = world.possiblySpawnEnemies(5);
        int count = 0;
        for (Enemy e : enemies) {
            if (!e.getType().equals("Slug")) {
                //Checking the vampie is spawned in the adjacent path tile
                assertEquals(e.getType(), "Vampire");
                assertEquals(e.getX(), 1);
                assertEquals(e.getY(), 0);
                count++;
            }
        }
        assertEquals(count, 1);

    }

    @Test
    public void testBarracks() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(1, orderedPath);

        assertEquals(world.buildingConditions("BarracksCard", pathPosition.getX().getValue(), pathPosition.getY().getValue()), true);
        assertEquals(world.buildingConditions("BarracksCard", 1, 1), false);


        Character c = new Character(pathPosition);
        world.setCharacter(c);

        
        Buildings Barracks = tw.createBarrackBuilding(world, pathPosition.getX().getValue(), pathPosition.getY().getValue()); 
        
        assertEquals(world.getAlliedSoldiers().size(), 0);

        world.buildingsRole();

        assertEquals(world.getAlliedSoldiers().size(), 1);
        
        AlliedSoldier as = world.getAlliedSoldiers().get(0);
        assertEquals(as.getX(), pathPosition.getX().getValue());
        assertEquals(as.getY(), pathPosition.getY().getValue());

        c.moveDownPath();
        world.buildingsRole();
        assertEquals(world.getAlliedSoldiers().size(), 1);

        c.moveDownPath();
        assertFalse(((BarracksBuilding) Barracks).toSpawn(c));

    }

    @Test
    public void testTrap() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);

        assertEquals(world.buildingConditions("TrapCard", pathPosition1.getX().getValue(), pathPosition1.getY().getValue()), true);
        assertEquals(world.buildingConditions("TrapCard", 1, 1), false);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        Enemy e = new Zombie(pathPosition1);
        assertEquals(e.getHealth(), 17);
        world.addEnemy(e);

        Buildings trapBuildings = tw.createTrapBuilding(world, pathPosition1.getX().getValue(), pathPosition1.getY().getValue());


        assertEquals(trapBuildings.shouldExist().getValue(), true);
        world.buildingsRole();

        assertEquals(e.getHealth(), 12);
        assertEquals(trapBuildings.shouldExist().getValue(), false);

        e.setHealth(4);
        Buildings trapBuildings1 = tw.createTrapBuilding(world, pathPosition1.getX().getValue(), pathPosition1.getY().getValue());
        world.buildingsRole();
        assertEquals(e.shouldExist().getValue(), false);

    }

    @Test
    public void testZombiePit() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        assertEquals(world.buildingConditions("ZombiePitCard", pathPosition1.getX().getValue(), pathPosition1.getY().getValue()), false);
        assertEquals(world.buildingConditions("ZombiePitCard", 1, 1), true);
        assertEquals(world.buildingConditions("ZombiePitCard", 18, 1), false);

        Buildings zombiePit = tw.createZombiePitBuilding(world, 1, 1);

        c.moveDownPath();
        List<Enemy> enemies;
        enemies = world.possiblySpawnEnemies(0);

        for (Enemy e : enemies) {
            assertNotEquals(e.getType(), "Zombie");
        }

        c.moveUpPath();
        enemies = world.possiblySpawnEnemies(1);
        int count = 0;
        for (Enemy e : enemies) {
            if (!e.getType().equals("Slug")) {
                assertEquals(e.getType(), "Zombie");
                assertEquals(e.getX(), 1);
                assertEquals(e.getY(), 0);
                count++;
            }
        }

        assertEquals(count, 1);
    }

    @Test
    public void testVillage() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);

        assertEquals(world.buildingConditions("VillageCard", pathPosition1.getX().getValue(), pathPosition1.getY().getValue()), true);
        assertEquals(world.buildingConditions("VillageCard", 1, 1), false);

        Character c = new Character(pathPosition);
        world.setCharacter(c);
        c.setHealth(50);

        Buildings village = tw.createVillageBuilding(world, pathPosition1.getX().getValue(), pathPosition1.getY().getValue());

        
        assertEquals(c.getHealth(), 50);
        c.moveDownPath();
        world.buildingsRole();

        assertEquals(c.getHealth(), 100);
    }

    @Test
    public void testTower() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(0, orderedPath);
        PathPosition pathPosition1 = new PathPosition(1, orderedPath);
        PathPosition pathPosition2 = new PathPosition(2, orderedPath);

        Character c = new Character(pathPosition);
        world.setCharacter(c);

        assertEquals(world.buildingConditions("TowerCard", pathPosition1.getX().getValue(), pathPosition1.getY().getValue()), false);
        assertEquals(world.buildingConditions("TowerCard", 1, 1), true);
        assertEquals(world.buildingConditions("TowerCard", 18, 1), false);

        Enemy e1 = new Zombie(pathPosition1);
        Enemy e2 = new Vampire(pathPosition2);

        world.addEnemy(e1);
        world.addEnemy(e2);

        TowerCard tc = new TowerCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(tc);
        Buildings tower = world.convertCardToBuildingByCoordinates("TowerCard", 0, 0, 1, 1);

        assertEquals(e1.getHealth(), 17);
        assertEquals(e2.getHealth(), 30);

        world.buildingsRole();
        
        assertEquals(e1.getHealth(), 15);
        assertEquals(e2.getHealth(), 28);

        //Making vampire move out of tower's range
        for (int i = 0; i < 7; i++) {
            world.buildingsRole();
            e2.moveUpPath();
        }

        assertEquals(e1.getHealth(), 1);

        world.buildingsRole();
        
        // Zombie Dead
        assertEquals(world.enemies.size(), 1);

        assertNotEquals(e2.getHealth(), 14);

    }

    @Test
    public void testAdjacentTile() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world =  tw.world();

        assertEquals(world.getAdjacentPathTile(8, 0), 9);
        assertEquals(world.getAdjacentPathTile(0, 13), 52);
        assertEquals(world.getAdjacentPathTile(4, 0), 5);
    }

}

