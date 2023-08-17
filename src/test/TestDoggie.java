package test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.Test;

import org.javatuples.Pair;

import unsw.loopmania.Character;
import unsw.loopmania.*;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;


public class TestDoggie {

    @Test
    public void testStats() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();

        PathPosition pos1 = new PathPosition(1, orderedPath);
        Doggie boss = new Doggie(pos1);

        world.addEnemy(boss);

        // Adjust stats later
        assertEquals(boss.getHealth(), 30);
        assertEquals(boss.getDamage(), 5);
        assertEquals(boss.getBattleRadius(), 1.2);
        assertEquals(boss.getSupportRadius(), 1,2);
    }

    @Test
    public void testStun() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();

        PathPosition pos1 = new PathPosition(1, orderedPath);
        PathPosition pos2 = new PathPosition(2, orderedPath);
        Doggie boss = new Doggie(pos1);

        world.addEnemy(boss);

        Character c = new Character(pos2);
        world.setCharacter(c);

        //Test after the character has been stunned        
        //boss.stun(c);
        c.characterAttack(boss);
        c.setDamage(10);

        // No Reduction in Doggie's health
        assertEquals(boss.getHealth(), 30);
    }

    @Test
    public void testSpawn() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);

        List<Enemy> enemies;
        enemies = world.possiblySpawnEnemies(19);

        for (Enemy e : enemies) {
            assertNotEquals(e.getType(), "Doggie");
        }

        enemies = world.possiblySpawnEnemies(20);   
        
        for (Enemy e : enemies) {
            if (!e.getType().equals("Vampire") && !e.getType().equals("Zombie") && !e.getType().equals("Slug")) {
                assertEquals(e.getType(), "Doggie");
            }
        }
    }
 }