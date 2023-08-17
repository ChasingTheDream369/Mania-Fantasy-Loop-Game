package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import unsw.loopmania.*;
import unsw.loopmania.Character;

public class TestElanMuske {
    
    @Test
    public void testSpawn() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Enemy> enemies;

        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);


        c.setExperience((double) 9000);
        enemies = world.possiblySpawnEnemies(39);

        for (Enemy e : enemies) {
            assertNotEquals(e.getType(), "ElanMuske");
        }

        enemies = world.possiblySpawnEnemies(40);   
        
        for (Enemy e : enemies) {
            assertNotEquals(e.getType(), "ElanMuske");
        }

        c.setExperience((double) 10000);
        enemies = world.possiblySpawnEnemies(40);   

        for (Enemy e : enemies) {
            if (!e.getType().equals("Vampire") && !e.getType().equals("Zombie") && !e.getType().equals("Slug")) {
                assertEquals(e.getType(), "ElanMuske");
            }
        }
    }

    @Test
    public void testStats() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();

        PathPosition pos1 = new PathPosition(1, orderedPath);
        ElanMuske elon = new ElanMuske(pos1);

        world.addEnemy(elon);

        // Adjust stats later
        assertEquals(elon.getHealth(), 50);
        assertEquals(elon.getDamage(), 7);
        assertEquals(elon.getBattleRadius(), 1.2);
        assertEquals(elon.getSupportRadius(), 1,2);
    }

    @Test
    public void testHealing() {
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();

        PathPosition pos1 = new PathPosition(1, orderedPath);
        ElanMuske elon = new ElanMuske(pos1);

        world.addEnemy(elon);

        // Add enemy in its support radius
        Enemy zombie = new Zombie(new PathPosition(2, world.getOrderedPath()));
        world.addEnemy(zombie);

        zombie.setHealth(10);

        // Heal the enemy in its support radius (method name may differ during implementation)
        elon.heal(zombie);

        assertEquals(zombie.getHealth(), 15);

        // Adding an enemy outside its support radius
        Enemy zombie1 = new Zombie(new PathPosition(7, world.getOrderedPath()));
        world.addEnemy(zombie1);
        
        zombie1.setHealth(10);

        // The enemy should not be healed
        elon.heal(zombie1);
        assertEquals(zombie1.getHealth(), 10);

    }
}

